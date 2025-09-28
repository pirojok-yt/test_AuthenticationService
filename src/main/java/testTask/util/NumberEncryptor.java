package testTask.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class NumberEncryptor implements AttributeConverter<String, String> {

    @Value("${cards.encryption.key}")
    private String KEY = "1234567890123456";

    private static final String ALGORITHM = "AES";

    private final SecretKeySpec secretKey;

    public NumberEncryptor() {
        this.secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
    }

    @Override
    public String convertToDatabaseColumn(String cardNumber) {
        if (cardNumber == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(cardNumber.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Could not encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Could not decrypt card number", e);
        }
    }
}
