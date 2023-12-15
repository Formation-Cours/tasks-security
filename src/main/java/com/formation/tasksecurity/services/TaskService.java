package com.formation.tasksecurity.services;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.dtos.TaskDto;
import com.formation.tasksecurity.dtos.TaskMapper;
import com.formation.tasksecurity.dtos.TaskRequestDto;
import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.enums.TypeRoleEnum;
import com.formation.tasksecurity.repositories.TaskRepository;
import com.formation.tasksecurity.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskEntity> findAll() {
        return this.taskRepository.findAll();
    }

    public TaskDto findById(UUID id, MyUserDetails userDetails) {
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // L'utilisateur est un admin, on lui attribue la tâche
            return this.taskRepository.findById(id).map(TaskMapper::TaskEntityToTaskDTOWithUserAdminDTOMapper).orElseThrow(
                    () -> new RuntimeException("Task not found")
            );
        }
        return this.taskRepository.findByIdByUser(id).map(TaskMapper::TaskEntityToTaskWithUserDTOMapper).orElseThrow(
                () -> new RuntimeException("Task not found")
        );
    }

    public TaskDto update(TaskRequestDto taskRequestDto, MyUserDetails userDetails) {
        UserEntity userEntity = this.userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        Optional<TaskEntity> taskEntity = taskRepository.findById(taskRequestDto.getId());
        Optional<UserEntity> userOpt = userRepository.findByEmail(taskRequestDto.getUser().getEmail());

        if (userEntity.getRole().equals(TypeRoleEnum.ADMIN) && taskEntity.isPresent() && userOpt.isPresent()) {
            taskEntity.map(task -> {
                task.setTitle(taskRequestDto.getTitle());
                task.setDescription(taskRequestDto.getDescription());
                task.setDone(taskRequestDto.isDone());
                task.setUser(userOpt.get());
                return taskRepository.save(task);
            });
            return TaskMapper.TaskEntityToTaskDTOWithUserAdminDTOMapper(this.taskRepository.save(taskEntity.get()));
        }
        if (taskEntity.isPresent() && userOpt.isPresent() && userEntity.getId().equals(userOpt.get().getId())) {
            taskEntity.map(task -> {
                task.setTitle(taskRequestDto.getTitle());
                task.setDescription(taskRequestDto.getDescription());
                task.setDone(taskRequestDto.isDone());
                task.setUser(userOpt.get());
                return taskRepository.save(task);
            });
            return TaskMapper.TaskEntityToTaskWithUserDTOMapper(this.taskRepository.save(taskEntity.get()));
        }
        throw new RuntimeException("Task not found");
    }


    // Méthode pour sauvegarder une tâche
    // Elle fait 2 requêtes SQL, mais nous gardons l'abstraction de Spring Data JPA
    public TaskDto save(TaskDto taskDto, MyUserDetails userDetails) {
        UserEntity userEntity = this.userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        TaskEntity taskEntity = TaskMapper.TaskDtoToTaskEntityMapper(taskDto);
        taskEntity.setUser(userEntity);
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // L'utilisateur est un admin, on lui attribue la tâche
            return TaskMapper.TaskEntityToTaskDTOWithUserAdminDTOMapper(this.taskRepository.save(taskEntity));
        }
        // L'utilisateur n'est pas un admin, on lui attribue la tâche
        return TaskMapper.TaskEntityToTaskWithUserDTOMapper(this.taskRepository.save(taskEntity));
    }

    // TODO: à modifier
    public TaskEntity update(TaskEntity taskEntity) {
        return this.taskRepository.save(taskEntity);
    }

    public void deleteById(UUID id, MyUserDetails userDetails) {
        UserEntity userEntity = this.userRepository.findById(userDetails.getUser().getId()).orElseThrow();
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);
        if (userEntity.getRole().equals(TypeRoleEnum.ADMIN) && taskEntity.isPresent()) {
            this.taskRepository.deleteById(id);
            return;
        }
        if (taskEntity.isPresent() && taskEntity.get().getUser().getId().equals(userEntity.getId())) {
            this.taskRepository.deleteById(id);
            return;
        }
        throw new RuntimeException("Task not found");
    }

    public List<TaskDto> getTasksBasedOnRole(MyUserDetails userDetails) {
        log.info("userDetails: {}", userDetails);
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // L'utilisateur est un admin, récupère toutes les tâches
            List<TaskEntity> taskEntities = taskRepository.findAll();
            return taskEntities.stream()
                    .map(TaskMapper::TaskEntityToTaskDTOWithUserAdminDTOMapper)
                    .toList();
        } else {
            // L'utilisateur n'est pas un admin, récupère seulement ses tâches
            return taskRepository.findAllByUser()
                    .stream()
                    .map(TaskMapper::TaskEntityToTaskWithUserDTOMapper)
                    .toList();
        }
    }


}
