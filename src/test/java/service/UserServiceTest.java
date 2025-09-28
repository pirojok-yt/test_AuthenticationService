package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import testTask.domain.User;
import testTask.domain.dto.SignupRequest;
import testTask.domain.enums.Role;
import testTask.mapper.CardMapper;
import testTask.repository.CardRepository;
import testTask.repository.UserRepository;
import testTask.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CardRepository cardRepository;
    @Spy
    private CardMapper cardMapper;

    @InjectMocks
    private UserService userService;

    private static final long id = 123;
    private static final String username = "Konstantin";
    private static final String password = "123456";

    @Test
    void getByIdTest() {
        User user = createUser();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        assertEquals(user, userService.getById(id));
    }

    @Test
    void createTest() {
        SignupRequest request = new SignupRequest(username, password);
        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        User savedUser = User.builder().password(null).username(username).role(Role.USER).build();
        userService.create(request, Role.USER);
        verify(repository, times(1)).save(savedUser);
    }

    private User createUser() {
        return User.builder().id(id).username(username).password(password).cards(List.of()).role(Role.USER).build();
    }
}
