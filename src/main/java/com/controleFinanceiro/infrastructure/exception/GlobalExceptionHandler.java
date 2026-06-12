package com.controleFinanceiro.infrastructure.exception;

import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.controleFinanceiro.application.dto.response.ApiResponse;
import com.controleFinanceiro.application.dto.response.ErrorDetail;
import com.controleFinanceiro.application.exception.ApplicationException;
import com.controleFinanceiro.domain.exception.ConflictException;
import com.controleFinanceiro.domain.exception.DomainException;
import com.controleFinanceiro.domain.exception.NotFoundException;
import com.controleFinanceiro.domain.shared.ErrorCodes;
import com.controleFinanceiro.domain.shared.MessageKeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Environment env;
    private final MessageSource messageSource;

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ErrorDetail.of(ex.getErrorCode(), resolve(ex.getMessage(), ex.getArgs()), 404, req.getRequestURI())));
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ErrorDetail.of(ex.getErrorCode(), resolve(ex.getMessage(), ex.getArgs()), 409, req.getRequestURI())));
    }

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ApiResponse<Void>> handleDomain(DomainException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.error(ErrorDetail.of(ex.getErrorCode(), resolve(ex.getMessage(), ex.getArgs()), 422, req.getRequestURI())));
    }

    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ApiResponse<Void>> handleApplication(ApplicationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorDetail.of(ex.getErrorCode(), resolve(ex.getMessage(), ex.getArgs()), 400, req.getRequestURI())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> handleBeanValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorDetail.of(ErrorCodes.ERRO_VALIDACAO, message, 400, req.getRequestURI())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ApiResponse<Void>> handleNoResource(NoResourceFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ErrorDetail.of(ErrorCodes.RECURSO_NAO_ENCONTRADO, ex.getMessage(), 404, req.getRequestURI())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ErrorDetail.of(ErrorCodes.ACESSO_NEGADO, resolve(MessageKeys.SEGURANCA_ACESSO_NEGADO, null), 403, req.getRequestURI())));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error on {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        var message = env.acceptsProfiles(Profiles.of("prod"))
                ? resolve(MessageKeys.ERRO_INTERNO, null)
                : ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorDetail.of(ErrorCodes.ERRO_INTERNO, message, 500, req.getRequestURI())));
    }

    private String resolve(String key, Object[] args) {
        String effectiveKey = key != null ? key : "";
        return messageSource.getMessage(effectiveKey, args, LocaleContextHolder.getLocale());
    }
}
