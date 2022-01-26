package com.vedubox.commonlibraries.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class UtilConfig {



    @Bean
    public AntPathMatcher antPathMatcher() {
        var antPathMatcher = new AntPathMatcher();
        antPathMatcher.setCaseSensitive(false);
        return antPathMatcher;
    }
}
