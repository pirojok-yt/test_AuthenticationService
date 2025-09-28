package testTask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CardDto(
        @Schema(description = "owner id", example = "123456")
        @Min(value = 0, message = "owner id should be positive")
        long cardId,
        @NotEmpty
        @Size(min = 16, max = 16, message = "number should be 16 digits")
        String number
) {
}
