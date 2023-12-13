package com.formation.tasksecurity.repositories;

import com.formation.tasksecurity.entities.TaskEntity;
import com.formation.tasksecurity.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    List<TaskEntity> findAllByUser(UserEntity userEntity);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tache (id, title, description, done, created_at, user_id) VALUES (:id, :title, :description, :done, NOW() , :userId)", nativeQuery = true)
    void saveByUserID(
            @Param("id") UUID id,
            @Param("title") String title,
            @Param("description") String description,
            @Param("done") boolean done,
            @Param("userId") Long userId);

    List<TaskEntity> findByUserId(Long userId);
}
