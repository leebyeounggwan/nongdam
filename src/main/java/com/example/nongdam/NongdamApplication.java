package com.example.nongdam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NongdamApplication {

    public static void main(String[] args) {
        SpringApplication.run(NongdamApplication.class, args);
    }

}
