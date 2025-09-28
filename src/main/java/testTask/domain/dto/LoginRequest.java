package testTask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Schema(description = "username")
        @Size(min = 2, max = 32, message = "Username must be between 2 and 32 characters")
        @NotBlank(message = "Username cannot be blank")
        String username,
        @Schema(description = "password", example = "123456")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
        String password) {
}
