package com.formation.tasksecurity.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthenticationDTO(
        @NotNull(message = "L'email ne peut pas être vide")
        @Email(message = "L'email doit être valide", regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
        String email,

        @NotNull(message = "Le mot de passe ne peut pas être vide")
        @Size(min = 8, max = 64, message = "Le mot de passe doit contenir entre 8 et 64 caractères")
        String password
) {
}
