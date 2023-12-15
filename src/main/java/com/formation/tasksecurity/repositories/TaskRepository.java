package com.formation.tasksecurity.repositories;

import com.formation.tasksecurity.entities.TaskEntity;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

//    @NonNull
//    @Query(value = "SELECT t FROM TaskEntity t ?#{hasRole('ROLE_ADMIN') ? '': 'WHERE t.user.email = ' + principal.username}")
//    List<TaskEntity> findAll();

    @Query(value = "SELECT t FROM TaskEntity t WHERE t.user.email = ?#{principal.username}")
    List<TaskEntity> findAllByUser();

    List<TaskEntity> findByUserId(Long userId);

    @Query(value = "SELECT t FROM TaskEntity t WHERE t.id = :id AND t.user.email = ?#{principal.username}")
    Optional<TaskEntity> findByIdByUser(@Param("id") UUID id);
}
