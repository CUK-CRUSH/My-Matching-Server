package Dino.Duett.test.service;

import Dino.Duett.domain.member.dto.MemberDto;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.domain.signup.dto.SignUpReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("member 생성 테스트")
    public void createMemberTest() {
        // given
        String phoneNumber = "01012345678";
        String kakaoId = "kakaoId";

        given(memberRepository.existsByPhoneNumber(phoneNumber)).willReturn(false);
        given(memberRepository.existsByKakaoId(kakaoId)).willReturn(false);
        given(memberRepository.save(any(Member.class))).will(invocation -> {
            Member member = invocation.getArgument(0);
            member.setId(1L);
            return member;
        });

        // when
        Member member = memberService.createMember(phoneNumber, kakaoId);

        // then
        assertThat(member.getId()).isEqualTo(1L);
        assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(member.getKakaoId()).isEqualTo(kakaoId);
    }

    @Test
    @DisplayName("member dto 생성 테스트")
    public void makeMemberDtoTest() {
        // given
        Member member = Member.builder()
                .id(1L)
                .phoneNumber("01012345678")
                .kakaoId("kakaoId")
                .coin(0)
                .state(MemberState.ACTIVE)
                .build();

        // when
        MemberDto memberDto = memberService.makeMemberDto(member);

        // then
        assertThat(memberDto.getId()).isEqualTo(member.getId());
        assertThat(memberDto.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
        assertThat(memberDto.getKakaoId()).isEqualTo(member.getKakaoId());
        assertThat(memberDto.getCoin()).isEqualTo(member.getCoin());
        assertThat(memberDto.getState()).isEqualTo(member.getState().getState());
    }

}
