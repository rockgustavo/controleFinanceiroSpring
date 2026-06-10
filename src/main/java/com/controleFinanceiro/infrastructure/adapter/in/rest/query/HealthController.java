package com.controleFinanceiro.infrastructure.adapter.in.rest.query;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        String dbStatus;
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            dbStatus = "ok";
        } catch (Exception e) {
            dbStatus = "error";
        }

        var data = new LinkedHashMap<String, Object>();
        data.put("status", "ok");
        data.put("db", dbStatus);

        var response = new LinkedHashMap<String, Object>();
        response.put("success", true);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
