package com.formation.tasksecurity.services;

import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }

    public Optional<TaskEntity> findById(UUID id) {
        return this.taskRepository.findById(id);
    }

    // TODO: à modifier
    public TaskEntity save(TaskEntity taskEntity) {
        return this.taskRepository.save(taskEntity);
    }

    // TODO: à modifier
    public TaskEntity update(TaskEntity taskEntity) {
        return this.taskRepository.save(taskEntity);
    }

    public void deleteById(UUID id) {
        this.taskRepository.deleteById(id);
    }
}
