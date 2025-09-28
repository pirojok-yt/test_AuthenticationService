package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import testTask.domain.dto.CustomUserDetails;
import testTask.domain.LoginAttempt;
import testTask.domain.User;
import testTask.domain.dto.LoginRequest;
import testTask.domain.dto.LoginResponse;
import testTask.domain.dto.SignupRequest;
import testTask.domain.enums.Role;
import testTask.repository.LoginAttemptRepository;
import testTask.service.AuthService;
import testTask.service.UserService;
import testTask.util.jwt.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private LoginAttemptRepository repository;

    @InjectMocks
    private AuthService authService;

    private static final String username = "Konstantin";
    private static final boolean success = true;
    private static final String password = "123456";
    private static final LoginAttempt login = LoginAttempt.builder().username(username).success(success).build();

    @Test
    void addLoginAttemptTest() {
        authService.addLoginAttempt(username, success);
        verify(repository, times(1)).save(login);
    }

    @Test
    void findLoginAttemptsTest() {
        when(repository.findAllByUsername(username)).thenReturn(List.of(login));
        assertEquals(authService.findLoginAttempts(username), List.of(login));
    }

    @Test
    void signup() {
        SignupRequest signupRequest = new SignupRequest(username, "123456");
        authService.signup(signupRequest);
        verify(userService, times(1)).create(signupRequest, Role.USER);
    }

    @Test
    void loginTestFail() {
        LoginRequest loginRequest = new LoginRequest(username, password);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void loginTestSuccess() {
        User user = User.builder().id(1).build();
        LoginRequest loginRequest = new LoginRequest(username, password);
        when(jwtUtil.generateToken(new CustomUserDetails(1, null, null, null))).thenReturn("1234567");
        when(userService.getByUsername(username)).thenReturn(user);
        assertEquals(new LoginResponse(username, "1234567"), authService.login(loginRequest));
    }
}
