package Dino.Duett.domain.signup.service;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.authentication.dto.VerificationCodeDto;
import Dino.Duett.domain.member.dto.MemberDto;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.domain.signup.dto.SignUpReq;
import Dino.Duett.domain.signup.dto.SignUpRes;
import Dino.Duett.gmail.GmailReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {
    private final VerificationCodeManager verificationCodeManager;
    private final GmailReader gmailReader;
    private final MemberService memberService;

    // 회원가입
    public SignUpRes signUp(SignUpReq signUpReq) throws ResponseStatusException {
        // redis, gmail 인증 코드 확인
        verificationCodeManager.verifyCode(signUpReq.getPhoneNumber(), signUpReq.getCode());
        gmailReader.validate(signUpReq.getPhoneNumber(), signUpReq.getCode());
        // 회원가입 처리
        Member member = memberService.createMember(signUpReq.getPhoneNumber(), signUpReq.getKakaoId());
        MemberDto memberDto = memberService.makeMemberDto(member);
        // todo: 프로필 생성
        // profileService.createProfile(signUpReq);

        // redis 인증 코드 삭제
        verificationCodeManager.deleteCode(signUpReq.getPhoneNumber());
        // todo: gmail 인증 코드 삭제
        // gmailReader.deleteCode(signUpReq.getPhoneNumber());

        return SignUpRes.builder()
                .member(memberDto)
                .build();
    }

    // 인증 코드 요청
    public VerificationCodeDto requestCode(String phoneNumber) {
        String code = verificationCodeManager.generateVerificationCode(phoneNumber);
        return VerificationCodeDto.builder()
                .code(code)
                .build();
    }
}
