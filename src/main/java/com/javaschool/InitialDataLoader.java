package com.javaschool;

import com.javaschool.entity.user.Role;
import com.javaschool.entity.user.User;
import com.javaschool.repository.RoleRepository;
import com.javaschool.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup || userRepository.findAll().iterator().hasNext()) {
            return;
        }

        Role userRole = createRoleIfNotFound("USER");
        Role adminRole = createRoleIfNotFound("ADMIN");

        createUserIfNotFound("user@java.com", passwordEncoder.encode("user@@"), userRole);
        createUserIfNotFound("admin@java.com", passwordEncoder.encode("admin@"), adminRole);

        alreadySetup = true;
    }

    @Transactional
    void createUserIfNotFound(String email, String password, Role role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setRoles(Collections.singleton(role));
            user.setActive(true);
            user.setPassword(password);
            user.setEmail(email);
            userRepository.save(user);
        }
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role = roleRepository.save(role);
        }
        System.out.printf("ROLE "+role);
        return role;
    }
}
