package Dino.Duett.domain.authentication.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class CheckMemberDto {
    private final boolean exists;
}
