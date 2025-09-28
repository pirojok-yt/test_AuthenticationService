package testTask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testTask.domain.Card;
import testTask.domain.dto.CardDto;
import testTask.domain.dto.SignupRequest;
import testTask.domain.enums.Role;
import testTask.domain.enums.Status;
import testTask.service.CardService;
import testTask.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin panel")
public class AdminController {
    private final CardService cardService;
    private final UserService userService;

    @PostMapping("/createUser")
    @Operation(summary = "Create user account")
    public ResponseEntity<Void> createUser(@RequestBody SignupRequest signupRequest) {
        userService.create(signupRequest, Role.USER);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/createAdmin/{userId}")
    @Operation(summary = "Create an admin account")
    public ResponseEntity<Void> createAdmin(@RequestBody SignupRequest signupRequest) {
        userService.create(signupRequest, Role.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/getCards/{userId}/{page}")
    @Operation(summary = "block a card")
    public ResponseEntity<List<CardDto>> getCardsFromUser(@PathVariable("userId") long userId, @PathVariable("page") int page) {
        return ResponseEntity.ok(userService.getUserCards(userId, page));
    }

    @PatchMapping("/changeBalance/{cardId}")
    @Operation(summary = "change card's balance")
    public ResponseEntity<BigDecimal> changeCardBalance(@PathVariable("cardId") long cardId, @RequestBody BigDecimal amount) {
        return ResponseEntity.ok(cardService.changeBalance(cardId, amount));
    }

    @PostMapping("/createCard/{userId}")
    @Operation(summary = "Create card for user with id")
    public ResponseEntity<CardDto> createCard(@PathVariable("userId") long userId) {
        CardDto created = cardService.create(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/block/{cardId}")
    @Operation(summary = "block a card")
    public ResponseEntity<Void> blockCard(@PathVariable("cardId") long cardId) {
        cardService.changeStatus(cardId, Status.BLOCKED);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate/{cardId}")
    @Operation(summary = "activate a card")
    public ResponseEntity<Void> activateCard(@PathVariable("cardId") long cardId) {
        cardService.changeStatus(cardId, Status.ACTIVE);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{userId}/{cardId}")
    @Operation(summary = "delete a card")
    public ResponseEntity<Void> deleteCard(@PathVariable("userId") long userId, @PathVariable("cardId") long cardId) {
        cardService.delete(userId, cardId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{cardId}")
    @Operation(summary = "Get a card")
    public ResponseEntity<Card> getCard(@PathVariable("cardId") long cardId) {
        return ResponseEntity.ok(cardService.getById(cardId));
    }

}
