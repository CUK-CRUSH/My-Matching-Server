package Dino.Duett.gmail.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    // Error Message
    INVALID_CONTENT_TYPE("Invalid content type"),
    EMAIL_VALIDATION_FAILED("Email validation failed"),
    NO_MESSAGES_FOUND("No messages found");

    // Success Message

    private final String message;
}
