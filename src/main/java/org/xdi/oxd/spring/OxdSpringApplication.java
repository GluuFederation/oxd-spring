package org.xdi.oxd.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OxdSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(OxdSpringApplication.class, args);
    }
}
