package com.formation.tasksecurity.controllers;

import com.formation.tasksecurity.configurations.MyUserDetails;
import com.formation.tasksecurity.dtos.UserByAdminDto;
import com.formation.tasksecurity.dtos.UserByMeDto;
import com.formation.tasksecurity.dtos.UserDto;
import com.formation.tasksecurity.exceptions.ResponseHandler;
import com.formation.tasksecurity.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserByAdminDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseHandler> findByEmail(@RequestParam(required = false) String email, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        UserDto userDto = userService.findByEmail(myUserDetails)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur avec cet email!"));
        ResponseHandler responseHandler = new ResponseHandler(HttpStatus.OK.value(), "OK", null, List.of(userDto));
        return new ResponseEntity<ResponseHandler>(responseHandler, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/other")
    public UserByAdminDto findOtherByEmail(@RequestParam String email) {
        return userService.findByEmailAdmin(email)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur avec cet email!"));
    }

    @PutMapping("/me")
    public UserByMeDto update(@RequestBody UserDto userDto, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userService.update(userDto, myUserDetails);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserByAdminDto updateByAdmin(@PathVariable Long id ,@RequestBody UserByAdminDto userDto, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userService.updateByAdmin(id, userDto, myUserDetails);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
