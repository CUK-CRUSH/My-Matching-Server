package Dino.Duett.domain.profile.enums;

import Dino.Duett.domain.profile.exception.ProfileException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum MbtiType {
    ISFP("ISFP"),
    ISFJ("ISFJ"),
    ISTP("ISTP"),
    ISTJ("ISTJ"),
    INFP("INFP"),
    INFJ("INFJ"),
    INTP("INTP"),
    INTJ("INTJ"),
    ESFP("ESFP"),
    ESFJ("ESFJ"),
    ESTP("ESTP"),
    ESTJ("ESTJ"),
    ENFP("ENFP"),
    ENFJ("ENFJ"),
    ENTP("ENTP"),
    ENTJ("ENTJ"),
    NONE("NONE");

    private final String value;
    public static MbtiType findByMbtiType(final String code) {
        return Arrays.stream(MbtiType.values())
                .filter(v -> v.getValue().equals(code))
                .findAny()
                .orElseThrow(ProfileException.MbtiTypeNotFoundException::new);
    }
}
