package testTask.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "username")
        String username,
        @Schema(description = "JWT token")
        String token) {

}
