package com.formation.tasksecurity.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tache")
@EntityListeners(AuditingEntityListener.class)
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100 )
    private String title;

    @Column(nullable = false)
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
