package com.formation.tasksecurity.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TaskRequestDto extends TaskDto{
    private UUID id;
    private UserDto user;

    public TaskRequestDto(String title, String description, boolean done, UUID id, UserDto user) {
        super(title, description, done);
        this.id = id;
        this.user = user;
    }

}
