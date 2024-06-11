package Dino.Duett.config.security;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.gmail.GmailReader;
import Dino.Duett.gmail.exception.GmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthMemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final VerificationCodeManager verificationCodeManager;
    private final GmailReader gmailReader;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthMember loadUserByUsername(String phoneNumber) throws MemberException, GmailException {
        Member member = memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(MemberException.MemberNotFoundException::new);
        // 로그인 인증 코드
        String verificationCode = verificationCodeManager.getCode(phoneNumber);
        // gmail 인증 코드
        gmailReader.validate(phoneNumber, verificationCode);

        return AuthMember.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .verificationCode(passwordEncoder.encode(verificationCode))
                .kakaoId(member.getKakaoId())
                .role(member.getRole().getName())
                .build();
    }
}
