package Dino.Duett.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    // Error Message
    DUPLICATE_PHONE_NUMBER("phone number is already in use"),
    DUPLICATE_KAKAO_ID("kakao id is already in use");

    // Success Message

    private final String message;
}
