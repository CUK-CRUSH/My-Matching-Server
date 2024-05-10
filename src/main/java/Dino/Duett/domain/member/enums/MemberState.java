package Dino.Duett.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberState {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String state;
}
