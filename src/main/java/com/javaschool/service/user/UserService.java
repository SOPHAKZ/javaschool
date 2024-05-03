package com.javaschool.service.user;

import com.javaschool.dto.user.LoginDTO;
import com.javaschool.dto.user.RegisterDTO;
import com.javaschool.dto.user.UserDTO;
import com.javaschool.entity.user.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {
    UserDTO register(RegisterDTO registerDTO) throws MessagingException;
    String login(LoginDTO loginDTO);
    boolean verifyOtp(String email, int otp);
    User findByEmail(String email);
    User findById(Long id);
    List<User> findAllUsers();
    void resendOtp(String email);
}
