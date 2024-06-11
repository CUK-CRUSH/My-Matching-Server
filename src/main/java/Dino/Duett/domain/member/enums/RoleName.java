package Dino.Duett.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleName {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    ;

    private final String roleName;
}
