package com.formation.tasksecurity.dtos;

import com.formation.tasksecurity.enums.TypeRoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserByMeDto extends UserDto {

    private TypeRoleEnum role;

    public UserByMeDto(String firstName, String lastName, String email, TypeRoleEnum role) {
        super(firstName, lastName, email);
        this.role = role;
    }

    public UserByMeDto() {    }
}
