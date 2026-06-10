package com.controleFinanceiro.infrastructure.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.ArrayList;
import java.util.Locale;

@Configuration
public class MessageConfig {

    @Bean
    MessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasename("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    @Bean
    LocaleResolver localeResolver() {
        var resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("pt"));
        var supported = new ArrayList<Locale>();
        supported.add(Locale.forLanguageTag("pt"));
        supported.add(Locale.ENGLISH);
        supported.add(Locale.forLanguageTag("es"));
        resolver.setSupportedLocales(supported);
        return resolver;
    }
}
