package testTask.util;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardGenerator {

    private static final SecureRandom RNG = new SecureRandom();

    @Value("${cards.number.bin}")
    private String bin;
    @Value("${cards.number.length}")
    private int length;

    public List<String> generateCardNumbers(int length) {
        List<String> numbers = new ArrayList<>();
        for(int i = 0; i< length; i++) {
            numbers.add(generateCardNumber());
        }
        return numbers;
    }

    // Generate a card number for given bin
    public String generateCardNumber() {
        int randomDigitsCount = length - bin.length() - 1; // last digit reserved for Luhn
        StringBuilder number = new StringBuilder(bin);
        for (int i = 0; i < randomDigitsCount; i++) {
            number.append(RNG.nextInt(10));
        }
        int checkDigit = calculateLuhnCheckDigit(number.toString());
        number.append(checkDigit);
        return number.toString();
    }

    // Luhn validating
    private int calculateLuhnCheckDigit(String numberWithoutCheckDigit) {
        int sum = 0;
        boolean alternate = true;
        for (int i = numberWithoutCheckDigit.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(numberWithoutCheckDigit.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        int mod = sum % 10;
        return (10 - mod) % 10;
    }
}

