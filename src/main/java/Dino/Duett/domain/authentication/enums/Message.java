package Dino.Duett.domain.authentication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    // Error Message
    INVALID_VERIFICATION_CODE("verification code is invalid");

    // Success Message

    private final String message;
}
