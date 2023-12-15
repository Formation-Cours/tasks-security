package com.formation.tasksecurity.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotNull(message = "Le titre est obligatoire")
    @Size(min = 2, max = 100, message = "Le titre doit contenir entre 2 et 100 caractères")
    protected String title;

    @NotNull(message = "La description est obligatoire")
    @Size(min = 2, max = 255, message = "La description doit contenir entre 2 et 255 caractères")
    protected String description;

    protected boolean done;
}
