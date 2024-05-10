package Dino.Duett.domain.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class VerificationCodeDto {
    @NotBlank
    private String code;
}
