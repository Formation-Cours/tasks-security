package com.formation.tasksecurity.dtos;

import com.formation.tasksecurity.entities.TaskEntity;

public class TaskMapper {

    public static TaskDto TaskEntityToTaskDTOWithUserAdminDTOMapper(TaskEntity task) {
        UserByAdminDto userByAdminDto = new UserByAdminDto();
        userByAdminDto.setFirstName(task.getUser().getFirstName());
        userByAdminDto.setLastName(task.getUser().getLastName());
        userByAdminDto.setEmail(task.getUser().getEmail());
        userByAdminDto.setRole(task.getUser().getRole());
        return setTaskDto(task, userByAdminDto);
    }

    public static TaskDto TaskEntityToTaskWithUserDTOMapper(TaskEntity task) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(task.getUser().getFirstName());
        userDto.setLastName(task.getUser().getLastName());
        userDto.setEmail(task.getUser().getEmail());
        return setTaskDto(task, userDto);
    }




    private static TaskDto setTaskDto(TaskEntity task, UserDto userDto) {
        return new TaskResponseDto(
                task.getTitle(),
                task.getDescription(),
                task.isDone(),
                task.getId(),
                userDto,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public static TaskEntity TaskDtoToTaskEntityMapper(TaskDto taskDto) {
        return new TaskEntity(
                null,
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.isDone(),
                null,
                null,
                null
        );
    }
}
