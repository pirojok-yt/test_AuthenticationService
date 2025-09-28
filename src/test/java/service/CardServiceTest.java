package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import testTask.domain.Card;
import testTask.domain.User;
import testTask.domain.dto.CardDto;
import testTask.domain.enums.Status;
import testTask.exception.NotFoundException;
import testTask.mapper.CardMapper;
import testTask.repository.CardRepository;
import testTask.repository.UserRepository;
import testTask.service.CardService;
import testTask.service.UserService;
import testTask.util.NumberCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Spy
    private CardMapper cardMapper;
    @Mock
    private NumberCache cache;
    @Mock
    private UserService userService;

    @InjectMocks
    private CardService cardService;

    private static final long id = 123;
    private static final String number = "0000111122223333";

    @Test
    void createTestSuccess() {
        when(cache.getNumber()).thenReturn(number);
        when(userService.getById(id)).thenReturn(User.builder().cards(new ArrayList<>()).build());
        when(cardRepository.findByNumber(number)).thenReturn(Optional.empty());
        Card card = Card.builder()
                .id(0)
                .owner(User.builder().build())
                .number(number)
                .last4Numbers(number.substring(12))
                .balance(new BigDecimal("0.00"))
                .status(Status.ACTIVE).build();
        when(cardRepository.save(any())).thenReturn(card);
        assertEquals(new CardDto(0, number.replace("000011112222", "************")), cardService.create(id));
    }

    @Test
    void requestBlockTest() {
        Card card = createCard();
        User owner = createOwner(List.of(card));
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(userService.getById(id)).thenReturn(owner);
        Card blockedCard = createCard();
        blockedCard.setStatus(Status.BLOCKED);
        when(cardRepository.save(blockedCard)).thenReturn(blockedCard);
        assertEquals(new CardDto(id, number.replace("000011112222", "************")), cardService.requestBlock(id, new CardDto(id, number)));
    }

    @Test
    void getBalanceTest() {
        Card card = createCard();
        User owner = createOwner(List.of(card));
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(userService.getById(id)).thenReturn(owner);
        assertEquals(new BigDecimal("1.00"), cardService.getBalance(id, new CardDto(id, number.replace("000011112222", "************"))));
    }

    @Test
    void transferTest() {
        Card card = createCard();
        Card card2 = createCard();
        card2.setId(id + 1);

        User owner = User.builder().id(id).cards(List.of(card, card2)).build();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.findById(id + 1)).thenReturn(Optional.of(card2));
        when(userService.getById(id)).thenReturn(owner);

        cardService.transfer(id, new CardDto(id, number), new CardDto(id + 1, number), new BigDecimal("0.5"));

        verify(cardRepository, times(1)).save(card);
        verify(cardRepository, times(1)).save(card2);

        assertEquals(new BigDecimal("0.50"), card.getBalance());
        assertEquals(new BigDecimal("1.50"), card2.getBalance());
    }

    @Test
    void transferTestNotEnoughBalance() {
        Card card = createCard();
        card.setBalance(new BigDecimal("0.00"));
        Card card2 = createCard();
        card2.setId(id + 1);

        User owner = User.builder().id(id).cards(List.of(card, card2)).build();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardRepository.findById(id + 1)).thenReturn(Optional.of(card2));
        when(userService.getById(id)).thenReturn(owner);

        assertThrows(
                IllegalArgumentException.class,
                () -> cardService
                        .transfer(id, new CardDto(id, number), new CardDto(id + 1, number), new BigDecimal("0.5")
                ));
    }

    @Test
    void getByIdTest() {
        Card card = createCard();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        assertEquals(card, cardService.getById(id));
    }

    @Test
    void getByIdTestNonExistent() {
        when(cardRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cardService.getById(id));
    }

    @Test
    void changeStatusTest() {
        Card card = createCard();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        cardService.changeStatus(id, Status.EXPIRED);
        verify(cardRepository, times(1)).save(card);
        assertEquals(Status.EXPIRED, card.getStatus());
    }

    @Test
    void changeBalanceTest() {
        Card card = createCard();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        cardService.changeBalance(card.getId(), new BigDecimal("20.00"));
        assertEquals(new BigDecimal("21.00"), card.getBalance());
    }

    private Card createCard() {
        return Card.builder()
                .id(id)
                .owner(User.builder().cards(List.of()).build())
                .number(number)
                .last4Numbers(number.substring(12))
                .balance(new BigDecimal("1.00"))
                .status(Status.ACTIVE)
                .build();
    }
    private User createOwner(List<Card> cards) {
        return User.builder().id(id).cards(cards).build();
    }
}
