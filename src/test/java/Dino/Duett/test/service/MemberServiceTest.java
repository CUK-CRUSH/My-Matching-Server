package Dino.Duett.test.service;

import Dino.Duett.domain.member.dto.MemberDto;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.member.repository.RoleRepository;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.utils.TestUtil;
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
    @Mock
    private RoleRepository roleRepository;

    @Test
    @DisplayName("member 생성 테스트")
    public void createMemberTest() {
        // given
        Member inputMember = TestUtil.createMember();

        given(memberRepository.existsByPhoneNumber(inputMember.getPhoneNumber())).willReturn(false);
        given(memberRepository.existsByKakaoId(inputMember.getKakaoId())).willReturn(false);
        given(roleRepository.findByName(any())).willReturn(mock());
        given(memberRepository.save(any(Member.class))).will(invocation -> {
            Member argMember = invocation.getArgument(0);
            return Member.builder()
                    .id(1L)
                    .phoneNumber(argMember.getPhoneNumber())
                    .kakaoId(argMember.getKakaoId())
                    .coin(0)
                    .state(MemberState.ACTIVE)
                    .build();
        });

        // when
        Member retMember = memberService.createMember(inputMember.getPhoneNumber(), inputMember.getKakaoId());

        // then
        assertThat(retMember.getId()).isEqualTo(1L);
        assertThat(retMember.getPhoneNumber()).isEqualTo(inputMember.getPhoneNumber());
        assertThat(retMember.getKakaoId()).isEqualTo(inputMember.getKakaoId());
    }

    @Test
    @DisplayName("member dto 생성 테스트")
    public void makeMemberDtoTest() {
        // given
        Member member = TestUtil.createMember();

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
