package testTask.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import testTask.domain.User;
import testTask.domain.enums.Role;
import testTask.repository.UserRepository;
import testTask.service.CardService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardService cardService;

    @Override
    public void run(String... args) {
        Optional<User> existAdmin = userRepository.findByUsername("admin");
        if (existAdmin.isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234567"))
                    .role(Role.ADMIN)
                    .build();
            admin = userRepository.save(admin);
            cardService.create(admin.getId());
            log.info("Default admin was created");
        }

        Optional<User> existUser = userRepository.findByUsername("user");
        if (existUser.isEmpty()) {
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1234567"))
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);
            cardService.create(user.getId());
            log.info("Default user was created");
        }
    }
}