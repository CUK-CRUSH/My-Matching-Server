package Dino.Duett.domain.signup.dto;

import Dino.Duett.domain.member.dto.MemberDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SignUpRes {
    @NotNull
    private MemberDto member;
}
