package testTask.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testTask.domain.dto.CardDto;
import testTask.service.CardService;
import testTask.service.UserService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User panel")
public class UserController {
    private final CardService cardService;
    private final UserService userService;

    @Operation(summary = "Request block from user")
    @GetMapping("/block")
    public ResponseEntity<CardDto> requestBlock(@RequestBody @Valid CardDto cardDto) {
        return ResponseEntity.ok(cardService.requestBlock(userService.getCurrentUserId(), cardDto));
    }

    @Operation(summary = "Return user's cards")
    @GetMapping("/cards/{page}")
    public ResponseEntity<List<CardDto>> getCards(@PathVariable("page") int page) {
        return ResponseEntity.ok(userService.getUserCards(userService.getCurrentUserId(), page));
    }

    @Operation(summary = "Check balance on card")
    @GetMapping("/checkBalance")
    public ResponseEntity<Map<String, String>> checkBalance(@RequestBody @Valid CardDto cardDto) {
        BigDecimal res = cardService.getBalance(userService.getCurrentUserId(), cardDto);
        Map<String, String> response = new HashMap<>();
        response.put("Your balance" , res.toString());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Transfer money between cards")
    @PutMapping("/trasfer")
    public ResponseEntity<?> transfer(
            @RequestBody @Valid CardDto firstCard,
            @RequestBody @Valid CardDto secondCard,
            @RequestBody String amount
    ) {
        cardService.transfer(userService.getCurrentUserId(), firstCard, secondCard, new BigDecimal(amount));
        return ResponseEntity.ok().build();
    }

}