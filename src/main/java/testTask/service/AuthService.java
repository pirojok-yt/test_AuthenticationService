package testTask.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testTask.domain.dto.CustomUserDetails;
import testTask.domain.User;
import testTask.domain.dto.LoginRequest;
import testTask.domain.dto.LoginResponse;
import testTask.domain.dto.SignupRequest;
import testTask.domain.LoginAttempt;
import testTask.domain.enums.Role;
import testTask.util.jwt.JwtUtil;
import testTask.repository.LoginAttemptRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptRepository repository;

    @Transactional
    public void addLoginAttempt(String username, boolean success) {
        LoginAttempt loginAttempt = LoginAttempt.builder().username(username).success(success).build();
        repository.save(loginAttempt);
    }

    public List<LoginAttempt> findLoginAttempts(String username) {
        return repository.findAllByUsername(username);
    }

    public void signup(SignupRequest request) {
        userService.create(request, Role.USER);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        } catch (BadCredentialsException e) {
            addLoginAttempt(request.username(), false);
            throw e;
        }

        User user = userService.getByUsername(request.username());
        String token = jwtUtil
                .generateToken(new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getRole()));

        addLoginAttempt(request.username(), true);
        return new LoginResponse(request.username(), token);
    }
}