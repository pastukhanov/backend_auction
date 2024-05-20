package org.luxeloot.backend.dataloaders;

import org.luxeloot.backend.models.Role;
import org.luxeloot.backend.models.User;
import org.luxeloot.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@PropertySource("classpath:application.yaml")
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    public UserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Boolean seedData = Boolean.valueOf(env.getProperty("data.seed-data"));
        if (seedData) {
            loadUserData();
        } else {
            System.out.println("Seed-data param is set to false, LotsDataLoader will not run.");
        }


    }

    private void loadUserData() {
        var admin = User.builder()
                .firstname("admin")
                .lastname("admin")
                .logo("default.png")
                .email("admin@luxeloot.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        User user1 = User.builder()
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("password"))
                .firstname("John")
                .lastname("Doe")
                .logo("default.png")
                .role(Role.USER)
                .build();

        userRepository.save(user1);

        var user2 = User.builder()
                .firstname("user")
                .lastname("test")
                .logo("default.png")
                .email("user@luxeloot.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.USER)
                .build();

        userRepository.save(user2);
    }
}
