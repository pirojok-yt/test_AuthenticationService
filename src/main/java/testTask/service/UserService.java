package testTask.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import testTask.domain.dto.CardDto;
import testTask.domain.dto.SignupRequest;
import testTask.domain.User;
import testTask.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testTask.exception.NotFoundException;
import testTask.mapper.CardMapper;
import testTask.repository.CardRepository;
import testTask.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    @Value("${cards.per-page}")
    private int CARDS_PER_PAGE;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public User getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        long id = getByUsername(username).getId();
        log.info("{}", id);
        return id;
    }

    @Transactional
    public void create(SignupRequest request, Role role) {
        String username = request.username();
        Optional<User> existingUser = repository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(String.format("User with the username: '%s' already exists.", username));
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = User.builder().password(hashedPassword).username(username).role(role).build();
        repository.save(user);
    }

    public List<CardDto> getUserCards(long userId, int page) {
        Pageable pageable = PageRequest.of(page, CARDS_PER_PAGE, Sort.by("id").descending());
        return cardRepository.findAllByOwnerId(userId, pageable).map(cardMapper::toDto).stream().toList();
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}