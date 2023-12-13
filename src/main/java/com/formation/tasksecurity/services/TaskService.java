package com.formation.tasksecurity.services;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }

    public List<TaskEntity> findAll(UserEntity userEntity) {
        return this.taskRepository.findAllByUser(userEntity);
    }

    public Optional<TaskEntity> findById(UUID id) {
        return this.taskRepository.findById(id);
    }

    // TODO: à modifier
    public TaskEntity save(TaskEntity taskEntity) {
//        this.taskRepository.saveByUserID(
//                UUID.randomUUID(),
//                taskEntity.getTitle(),
//                taskEntity.getDescription(),
//                taskEntity.isDone(),
//                taskEntity.getUser().getId()
//        );
        return taskEntity;
    }

    // TODO: à modifier
    public TaskEntity update(TaskEntity taskEntity) {
        return this.taskRepository.save(taskEntity);
    }

    public void deleteById(UUID id) {
        this.taskRepository.deleteById(id);
    }

    public List<TaskEntity> getTasksBasedOnRole(MyUserDetails userDetails) {
        log.info("userDetails: {}", userDetails);
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // L'utilisateur est un admin, récupère toutes les tâches
            return taskRepository.findAll();
        } else {
            // L'utilisateur n'est pas un admin, récupère seulement ses tâches
            return taskRepository.findByUserId(userDetails.getUser().getId());
        }
    }
}
