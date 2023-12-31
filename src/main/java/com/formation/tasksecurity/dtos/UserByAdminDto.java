package com.formation.tasksecurity.dtos;

import com.formation.tasksecurity.enums.TypeRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserByAdminDto extends UserDto {

    private Long id;
    private TypeRoleEnum role;

    public UserByAdminDto(Long id, String firstName, String lastName, String email, TypeRoleEnum role) {
        super(firstName, lastName, email);
        this.id = id;
        this.role = role;
    }

    public UserByAdminDto() {    }
}
