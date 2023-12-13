package com.formation.tasksecurity.services;

import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> register(UserEntity userEntity) {
        if (userRepository.findByEmail(userEntity.getEmail()).isEmpty()) {
            return Optional.of(userRepository.save(userEntity));
        }
        return Optional.empty();
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
