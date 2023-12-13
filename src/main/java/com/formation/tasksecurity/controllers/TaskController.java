package com.formation.tasksecurity.controllers;

import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.services.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskEntity> findAll() {
        return taskService.findAll();
    }
}
