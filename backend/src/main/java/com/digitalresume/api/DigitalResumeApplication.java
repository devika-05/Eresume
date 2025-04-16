package com.digitalresume.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DigitalResumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalResumeApplication.class, args);
    }
}