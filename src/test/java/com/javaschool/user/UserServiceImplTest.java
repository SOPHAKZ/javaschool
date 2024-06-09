package com.javaschool.user;

import com.javaschool.dto.user.LoginDTO;
import com.javaschool.dto.user.RegisterDTO;
import com.javaschool.dto.user.UserDTO;
import com.javaschool.entity.user.Role;
import com.javaschool.entity.user.User;
import com.javaschool.entity.user.UserOtp;
import com.javaschool.exception.ApiException;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.UserMapper;
import com.javaschool.repository.OptRepository;
import com.javaschool.repository.RoleRepository;
import com.javaschool.repository.UserRepository;
import com.javaschool.security.JwtTokenProvider;
import com.javaschool.service.user.SendEmailService;
import com.javaschool.service.user.UserServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OptRepository optRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @Spy
    private SendEmailService sendEmailService;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterDTO registerDTO;
    private User user;
    private UserOtp userOtp;
    private Role role;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("password");

        user = new User();
        user.setEmail("test@example.com");
        user.setId(1L);

        userOtp = new UserOtp();
        userOtp.setUser(user);
        userOtp.setOneTimePassword(1234);
        userOtp.setExpiration(new Date(System.currentTimeMillis() + 300000));

        role = new Role();
        role.setName("USER");

        authentication = mock(Authentication.class);
    }

    @Test
    void testRegister_UserAlreadyRegistered() {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> userService.register(registerDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User already registered", exception.getMessage());
    }

    @Test
    void testRegister_Success() throws MessagingException, MessagingException {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(userMapper.registerDtoToEntity(registerDTO)).thenReturn(user);
        when(roleRepository.findByName("USER")).thenReturn(role);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userDto(user)).thenReturn(new UserDTO());

        UserDTO result = userService.register(registerDTO);

        assertNotNull(result);
        verify(userRepository).save(user);
        verify(optRepository).save(any(UserOtp.class));
    }

    @Test
    void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("token");

        String token = userService.login(loginDTO);

        assertEquals("token", token);
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testVerifyOtp_Success() {
        when(optRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(userOtp));
        when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.verifyOtp(user.getEmail(), userOtp.getOneTimePassword());

        assertTrue(result);
        assertTrue(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void testVerifyOtp_Expired() {
        userOtp.setExpiration(new Date(System.currentTimeMillis() - 300000));

        when(optRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(userOtp));

        boolean result = userService.verifyOtp(user.getEmail(), userOtp.getOneTimePassword());

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User result = userService.findByEmail(user.getEmail());

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.findById(user.getId());

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.findById(user.getId()));
        assertEquals("User", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(user.getId().toString(), exception.getFieldValue());
    }

    @Test
    void testFindAllUsers_Success() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void testResendOtp_Success() throws MessagingException {
        when(optRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(userOtp));

        userService.resendOtp(user.getEmail());

        verify(optRepository).save(any(UserOtp.class));
        verify(sendEmailService).sendEmail(eq(user.getEmail()), anyInt());
    }

    @Test
    void testResendOtp_NotFound() {
        when(optRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.resendOtp(user.getEmail()));
        assertEquals("UserOtp", exception.getResourceName());
        assertEquals("email", exception.getResourceFiled());
        assertEquals(user.getEmail(), exception.getFieldValue());
    }
}
