package com.formation.tasksecurity.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tache")
@EntityListeners(AuditingEntityListener.class)
@Data
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    @NotNull(message = "Le titre est obligatoire")
    @Size(min = 2, max = 100, message = "Le titre doit contenir entre 2 et 100 caractères")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "La description est obligatoire")
    @Size(min = 2, max = 255, message = "La description doit contenir entre 2 et 255 caractères")
    private String description;

    @Column(nullable = false)
    private boolean done;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private UserEntity user;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(updatable = true, insertable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
