package com.formation.tasksecurity.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TaskResponseDto extends TaskRequestDto{
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskResponseDto(String title, String description, boolean done, UUID id, UserDto user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(title, description, done, id, user);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
