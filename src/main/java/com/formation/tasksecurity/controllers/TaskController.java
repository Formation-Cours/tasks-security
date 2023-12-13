package com.formation.tasksecurity.controllers;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.repositories.UserRepository;
import com.formation.tasksecurity.services.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskEntity> findAll(@AuthenticationPrincipal MyUserDetails userDetails) {
        log.info("userDetails: {}", userDetails.getAuthorities());
        return taskService.getTasksBasedOnRole(userDetails);
    }

    @PostMapping
    public TaskEntity save(@Valid @RequestBody TaskEntity taskEntity, @AuthenticationPrincipal MyUserDetails userDetails) {
        return taskService.save(taskEntity, userDetails);
    }
}
