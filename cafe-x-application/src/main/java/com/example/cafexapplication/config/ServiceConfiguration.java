package com.example.cafexapplication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public class ServiceConfiguration {

    @Getter @Setter
    private String currency;


}
