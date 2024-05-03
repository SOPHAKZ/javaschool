package com.javaschool.dto.user;

import lombok.Data;

@Data
public class RegisterDTO {
    private String firstName;
    private String LastName;
    private String email;
    private String password;
    private boolean isStudent;
}
