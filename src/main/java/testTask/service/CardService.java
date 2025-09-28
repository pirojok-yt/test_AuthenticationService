package testTask.service;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testTask.domain.Card;
import testTask.domain.User;
import testTask.domain.dto.CardDto;
import testTask.domain.enums.Status;
import testTask.exception.AccessDeniedException;
import testTask.exception.NotFoundException;
import testTask.mapper.CardMapper;
import testTask.repository.CardRepository;
import testTask.repository.UserRepository;
import testTask.util.NumberCache;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final NumberCache cache;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public CardDto create(long ownerId) {
        String number = cache.getNumber();
        User owner = userService.getById(ownerId);

        while(cardRepository.findByNumber(number).isPresent()) {
            number = cache.getNumber();
        }

        Card card = Card.builder()
                .owner(owner)
                .number(number)
                .last4Numbers(number.substring(12))
                .balance(new BigDecimal("0.00"))
                .status(Status.ACTIVE).build();
        Card newCard = cardRepository.save(card);
        owner.getCards().add(newCard);
        userRepository.save(owner);
        return cardMapper.toDto(newCard);
    }

    @Transactional
    public void delete(long userId, long cardId) {
        Card card = validate(userId, cardId);
        User owner = card.getOwner();
        owner.getCards().remove(card);
        cardRepository.delete(getById(cardId));
        userRepository.save(owner);
    }

    @Transactional
    public CardDto requestBlock(long userId, CardDto cardDto) {
        Card card = validate(userId, cardDto.cardId());
        card.setStatus(Status.BLOCKED);
        return cardMapper.toDto(cardRepository.save(card));
    }

    public BigDecimal getBalance(long userId, CardDto cardDto) {
        Card card = validate(userId, cardDto.cardId());
        return card.getBalance();
    }

    @Transactional
    public void transfer(long userId, CardDto firstCard, CardDto secondCard, BigDecimal amount) {
        Card first = validate(userId, firstCard.cardId());
        Card second = validate(userId, secondCard.cardId());

        if(first.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Not enough balance on card with id: " + firstCard.cardId());
        }

        first.setBalance(first.getBalance().subtract(amount));
        second.setBalance(second.getBalance().add(amount));

        cardRepository.save(first);
        cardRepository.save(second);
    }

    public Card getById(long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException("Couldn't find card with number: " + cardId));
    }

    //for admins
    @Transactional
    public void changeStatus(long cardId, Status status) {
        Card card = getById(cardId);
        card.setStatus(status);
        cardRepository.save(card);
    }

    @Transactional
    public BigDecimal changeBalance(long cardId, BigDecimal amount) {
        Card card = getById(cardId);
        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);
        return card.getBalance();
    }

    //if user and card exist and user has card
    private Card validate(long userId, long cardId) {
        User user = userService.getById(userId);
        Card card = getById(cardId);

        if(card.getStatus() == Status.BLOCKED) {
            throw new AccessDeniedException("This card is blocked");
        } else if(card.getStatus() == Status.EXPIRED) {
            throw new AccessDeniedException("This card is expired");
        } else if(!user.getCards().contains(card)) {
            throw new AccessDeniedException("Access denied for user with id: " + userId);
        }

        return card;
    }

    // for expiring cards
    @Scheduled(cron = "${cards.expire-check}")
    public void expireEntities() {
        cardRepository.expireEntities(LocalDateTime.now());
    }
}
