package com.javaschool.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
    private boolean isStudent;
    private Set<String> role;
}
