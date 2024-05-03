package com.javaschool.mapstruct;

import com.javaschool.dto.user.RegisterDTO;
import com.javaschool.dto.user.UserDTO;
import com.javaschool.entity.user.Role;
import com.javaschool.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role",source = "user")
    UserDTO userDto(User user);

    @Mapping(target = "roles", ignore = true)
    User registerDtoToEntity(RegisterDTO registerDTO);

    default Set<String> roleToString(User user){
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default List<UserDTO> userDtoList(List<User> userList) {
        return userList.stream()
                .map(this::userDto)
                .collect(Collectors.toList());
    }

}
