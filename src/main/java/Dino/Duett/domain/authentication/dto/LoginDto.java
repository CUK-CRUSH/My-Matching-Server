package Dino.Duett.domain.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String verificationCode;
}
