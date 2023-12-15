package com.formation.tasksecurity.dtos;

import com.formation.tasksecurity.enums.TypeRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserByAdminDto extends UserDto {
    private TypeRoleEnum role;
}
