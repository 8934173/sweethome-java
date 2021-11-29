package com.sweethome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class SweethomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweethomeApplication.class, args);
    }

}
