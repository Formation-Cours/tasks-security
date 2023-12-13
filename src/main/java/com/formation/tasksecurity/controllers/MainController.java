package com.formation.tasksecurity.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MainController {

    @GetMapping("/hello")
    public Map<String, String> home() {
        return Map.of("message", "Hello World!");
    }
}
