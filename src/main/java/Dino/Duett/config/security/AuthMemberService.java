package Dino.Duett.config.security;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthMemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final VerificationCodeManager verificationCodeManager;

    public AuthMember loadUserByMemberId(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        return AuthMember.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .kakaoId(member.getKakaoId())
                .role(member.getRole().getName())
                .build();
    }
    @Override
    public AuthMember loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        // 로그인 인증 코드
        String verificationCode = verificationCodeManager.getCode(phoneNumber);

        return AuthMember.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .verificationCode(verificationCode)
                .kakaoId(member.getKakaoId())
                .role(member.getRole().getName())
                .build();
    }
}
