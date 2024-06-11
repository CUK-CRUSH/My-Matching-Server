package Dino.Duett.domain.tag.enums;

import Dino.Duett.domain.tag.exception.TagException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum PriorityType {
    NONE("없음",0),
    STANDARD("일반",1),
    FEATURED("강조",2);
    private final String value;
    private final int key;

    public static PriorityType findByPriorityType(final int code) {
        return Arrays.stream(PriorityType.values())
                .filter(v -> v.getKey() == code)
                .findAny()
                .orElseThrow(TagException.TagTypeNotFoundException::new);
    }
}
