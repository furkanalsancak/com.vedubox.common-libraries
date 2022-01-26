package com.vedubox.commonlibraries.config.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "noauth-url")
public class NoAuthURLConfigData {

    private String[] gets;
    private String[] posts;
}