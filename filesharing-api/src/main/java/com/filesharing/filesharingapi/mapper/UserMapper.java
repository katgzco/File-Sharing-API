package com.filesharing.filesharingapi.mapper;

import com.filesharing.filesharingapi.dto.UserDto;
import com.filesharing.filesharingapi.entity.User;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto userToUserDTO(User user);
    User userDTOToUser(UserDto userDto);
}