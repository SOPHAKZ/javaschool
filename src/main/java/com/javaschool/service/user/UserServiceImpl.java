package com.javaschool.service.user;

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
import com.javaschool.service.SendEmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private OptRepository optRepository;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private SendEmailService emailService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final OptRepository optRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final SendEmailService sendEmailService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    @Transactional
    public UserDTO register(RegisterDTO registerDTO) throws MessagingException {

        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"User already registered");
        }
        User user = userMapper.registerDtoToEntity(registerDTO);

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        User save = userRepository.save(user);

        // Save Otp
        int otp = generateOTP();

        UserOtp userOtp = new UserOtp();
        userOtp.setUser(save);
        userOtp.setOneTimePassword(otp);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        userOtp.setExpiration(calendar.getTime());

        optRepository.save(userOtp);
        System.out.println("TIME : "+ LocalDateTime.now());
        // send email code
//        sendEmailService.sendEmail(registerDTO.getEmail(), otp);
        CompletableFuture.runAsync(() -> {
            try {
                sendEmailService.sendEmail(registerDTO.getEmail(), otp);
                System.out.println("EMAIL  : "+ LocalDateTime.now());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }, executorService);

        return userMapper.userDto(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public boolean verifyOtp(String email, int otp) {
        Optional<UserOtp> userOtpOptional = optRepository.findByUserEmail(email);
        if (userOtpOptional.isPresent()) {
            UserOtp userOtp = userOtpOptional.get();

            if (userOtp.getExpiration().before(new Date())) {
                return false;
            }
            if (userOtp.getOneTimePassword() == otp) {
                User user = userOtp.getUser();
                user.setActive(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
       return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User","id",id.toString()));

    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void resendOtp(String email) {
        Optional<UserOtp> userOtpOptional = optRepository.findByUserEmail(email);
        if(!userOtpOptional.isPresent()){
            throw new ResourceNotFoundException("UserOtp","email",email);
        }
        UserOtp userOtp = userOtpOptional.get();

        int otp = generateOTP();

        userOtp.setOneTimePassword(otp);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,5);
        userOtp.setExpiration(calendar.getTime());
        optRepository.save(userOtp);
        CompletableFuture.runAsync(() ->{
          try {
              sendEmailService.sendEmail(email,otp);
          }catch (MessagingException e){
              e.printStackTrace();
          }
        },executorService);

    }

    private int generateOTP() {

        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }
}
