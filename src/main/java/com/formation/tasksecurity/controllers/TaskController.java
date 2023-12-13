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
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            log.info("Je suis un admin");
            return taskService.findAll();
        }
        log.info("Je ne suis pas un admin");
        return taskService.findAll(userDetails.getUser());
    }

    @PostMapping
    public TaskEntity save(@Valid @RequestBody TaskEntity taskEntity, @AuthenticationPrincipal MyUserDetails userDetails) {
        taskEntity.setUser(userDetails.getUser());
        return taskService.save(taskEntity);
    }
}
