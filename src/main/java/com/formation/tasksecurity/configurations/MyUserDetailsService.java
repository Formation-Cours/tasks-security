package com.formation.tasksecurity.configurations;

import com.formation.tasksecurity.entities.UserEntity;
import com.formation.tasksecurity.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> emailUser = userRepository.findByEmail(username);
        if (emailUser.isPresent()) {
            return new MyUserDetails(emailUser.get());
        }
        throw new UsernameNotFoundException("Aucun utilisateur avec cet email!");
    }
}
