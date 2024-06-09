package com.javaschool.controller;

import com.javaschool.dto.user.LoginDTO;
import com.javaschool.dto.user.RegisterDTO;
import com.javaschool.dto.user.UserDTO;

import com.javaschool.mapstruct.UserMapper;
import com.javaschool.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @Operation(summary = "get user information by user id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id){
        UserDTO userDTO = userMapper.userDto(userService.findById(id));

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/email={email}")
    @Operation(summary = "get user information by user email")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("email") String email){
        UserDTO userDTO = userMapper.userDto(userService.findByEmail(email));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/")
    @Operation(summary = "get all users information")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUser(){
        List<UserDTO> userDTOList = userMapper.userDtoList(userService.findAllUsers());

        return ResponseEntity.ok(userDTOList);
    }
    @PostMapping("/register")
    @Operation(summary = "register user")
    public ResponseEntity<UserDTO> createUser(@RequestBody  RegisterDTO registerDTO) throws MessagingException {
        return ResponseEntity.ok(userService.register(registerDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "login user")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        String token = userService.login(loginDTO);

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/verify")
    @Operation(summary = "verify email by code otp ")
    public ResponseEntity<?> verifyEmail(@RequestParam(value = "email") String email,
                                         @RequestParam(value = "otp") int otp){
        boolean verified = userService.verifyOtp(email, otp);

        return ResponseEntity.ok(verified);
    }

    @PostMapping("/resend")
    @Operation(summary = "resend new opt by email")
    public ResponseEntity<Void> resendEmailOTP(@RequestParam(value = "email") String email){
        userService.resendOtp(email);

        return ResponseEntity.ok().build();
    }

}
