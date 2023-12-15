package com.formation.tasksecurity.services;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.dtos.UserByAdminDto;
import com.formation.tasksecurity.dtos.UserByMeDto;
import com.formation.tasksecurity.dtos.UserDto;
import com.formation.tasksecurity.dtos.UserMapper;
import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.repositories.TaskRepository;
import com.formation.tasksecurity.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Secured("ROLE_ADMIN")
    public List<UserByAdminDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::userEntityToUserByAdminDtoMapper)
                .toList();
    }

    public Optional<UserByAdminDto> findByEmailAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::userEntityToUserByAdminDtoMapper);
    }

    public Optional<UserDto> findByEmail(MyUserDetails myUserDetails) {
        return userRepository.findByEmail(myUserDetails.getUsername())
                .map(UserMapper::userEntityToUserByMeDtoMapper);
    }

    @Secured("ROLE_ADMIN")
    public UserByAdminDto updateByAdmin(Long id, UserByAdminDto userDto, MyUserDetails myUserDetails) {
        Optional<UserEntity> userId = userRepository.findById(id);
        if (userId.isEmpty()) {
            throw new RuntimeException("Aucun utilisateur avec cet id!");
        }
        // s'il y a un changement d'email, on vérifie qu'il n'existe pas déjà
        // sinon on met à jour l'utilisateur
        Optional<UserEntity> userParam = userRepository.findByEmail(userDto.getEmail());
        UserEntity userEntity = userId.get();
        if (userParam.isPresent() && !userDto.getEmail().equals(userEntity.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà!");
        }
        userEntity.setEmail(userDto.getEmail());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setRole(userDto.getRole());
        userRepository.save(userEntity);
        return UserMapper.userEntityToUserByAdminDtoMapper(userEntity);
    }

    public UserByMeDto update(UserDto userDto, MyUserDetails myUserDetails) {
        Optional<UserEntity> userParam = userRepository.findByEmail(myUserDetails.getUsername());
        if (userParam.isPresent()) {
            UserEntity userEntity = userParam.get();
            userEntity.setFirstName(userDto.getFirstName());
            userEntity.setLastName(userDto.getLastName());
            userEntity.setEmail(userDto.getEmail());
            userRepository.save(userEntity);
            return UserMapper.userEntityToUserByMeDtoMapper(userEntity);
        }
        throw new RuntimeException("Aucun utilisateur avec cet email!");
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
