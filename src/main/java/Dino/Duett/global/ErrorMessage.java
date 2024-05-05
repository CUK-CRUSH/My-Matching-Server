package Dino.Duett.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INVALID_CONTENT_TYPE("Invalid content type"),
    EMAIL_VALIDATION_FAILED("Email validation failed"),
    NO_MESSAGES_FOUND("No messages found");

    private final String message;
}
