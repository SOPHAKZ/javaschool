package com.javaschool.user;

import com.javaschool.dto.user.RegisterDTO;
import com.javaschool.entity.user.Role;
import com.javaschool.entity.user.User;
import com.javaschool.entity.user.UserOtp;
import com.javaschool.exception.ApiException;
import com.javaschool.repository.OptRepository;
import com.javaschool.repository.RoleRepository;
import com.javaschool.repository.UserRepository;
import com.javaschool.security.JwtTokenProvider;
import com.javaschool.service.SendEmailService;
import com.javaschool.service.user.UserServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private OptRepository optRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private SendEmailService sendEmailService;

    @Mock
    private UserOtp userOtp;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_Success() throws MessagingException {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setFirstName("John");
        registerDTO.setLastName("Doe");
        registerDTO.setPassword(passwordEncoder.encode("password"));

        // Mock the behavior of userRepository.save to return a non-null User object
        when(userRepository.save(any())).thenReturn(new User());

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        // Mock the behavior of roleRepository.findByName("USER")
        Role userRole = new Role();
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        String result = String.valueOf(userService.register(registerDTO));

        assertEquals("User Register successfully", result);
        verify(sendEmailService, times(1)).sendEmail(eq("test@example.com"), anyInt());
    }

    @Test
    void register_UserAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(ApiException.class, () -> userService.register(registerDTO));
    }

    @Test
    void verifyOtp_ValidOtp() {
        when(optRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(userOtp));
        when(userOtp.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000)); // Set future expiration
        when(userOtp.getOneTimePassword()).thenReturn(1234);

        assertTrue(userService.verifyOtp("test@example.com", 1234));
    }

    @Test
    void verifyOtp_ExpiredOtp() {
        when(optRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(userOtp));
        when(userOtp.getExpiration()).thenReturn(new Date(System.currentTimeMillis() - 10000)); // Set past expiration

        assertFalse(userService.verifyOtp("test@example.com", 1234));
    }
    @Test
    void getUserByEmail_UserExists() {
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User actualUser = userService.findByEmail(email);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByEmail_UserNotFound() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        User actualUser = userService.findByEmail(email);

        assertEquals(null, actualUser);
    }

    @Test
    void getUserById_UserExists() {
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findById(userId);

        assertEquals(expectedUser, actualUser);
    }


    @Test
    void getAllUsers_UsersExist() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(new User());
        expectedUsers.add(new User());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), actualUsers.get(i));
        }
    }

    @Test
    void getAllUsers_NoUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> actualUsers = userService.findAllUsers();

        assertEquals(0, actualUsers.size());
    }
}
