package com.formation.tasksecurity.controllers;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.dtos.TaskDto;
import com.formation.tasksecurity.dtos.TaskRequestDto;
import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.repositories.UserRepository;
import com.formation.tasksecurity.services.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> findAll(@AuthenticationPrincipal MyUserDetails userDetails) {
        return taskService.getTasksBasedOnRole(userDetails);
    }

    @PostMapping
    public TaskDto save(@Valid @RequestBody TaskDto taskDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        return taskService.save(taskDto, userDetails);
    }

    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable UUID id, @AuthenticationPrincipal MyUserDetails userDetails) {
        return taskService.findById(id, userDetails);
    }

    @PutMapping("/{id}")
    public TaskDto update(@PathVariable UUID id, @Valid @RequestBody TaskRequestDto taskDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        taskDto.setId(id);
        return taskService.update(taskDto, userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id, @AuthenticationPrincipal MyUserDetails userDetails) {
        taskService.deleteById(id, userDetails);
        return ResponseEntity.ok("Task deleted");
    }
}
