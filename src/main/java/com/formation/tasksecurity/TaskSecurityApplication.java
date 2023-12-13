package com.formation.tasksecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity
public class TaskSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskSecurityApplication.class, args);
    }

}
