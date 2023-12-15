package com.formation.tasksecurity.dtos;

import com.formation.tasksecurity.entities.UserEntity;

public class UserMapper {

//    public static UserEntity UserDtoToUserEntityMapper(UserDto userDto) {
//        return new UserEntity(
//                null,
//                userDto.getFirstName(),
//                userDto.getLastName(),
//                userDto.getEmail(),
//                null,
//                null,
//                null
//        );
//    }

    public static UserByAdminDto userEntityToUserByAdminDtoMapper(UserEntity userEntity) {
        return new UserByAdminDto(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }

    public static UserDto userEntityToUserDtoMapper(UserEntity userEntity) {
        return new UserDto(
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName()
        );
    }

    public static UserByMeDto userEntityToUserByMeDtoMapper(UserEntity userEntity) {
        return new UserByMeDto(
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getRole()
        );
    }
}
