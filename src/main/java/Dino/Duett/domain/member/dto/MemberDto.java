package Dino.Duett.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String phoneNumber;
    private String kakaoId;
    private Integer coin;
    private String state;
}
