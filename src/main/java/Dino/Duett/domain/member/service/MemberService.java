package Dino.Duett.domain.member.service;

import Dino.Duett.domain.member.dto.MemberDto;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static Dino.Duett.domain.member.enums.Message.DUPLICATE_PHONE_NUMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 멤버 엔티티 생성
    public Member createMember(String phoneNumber, String kakaoId) {
        // 중복 체크
        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DUPLICATE_PHONE_NUMBER.getMessage());
        }
        if (memberRepository.existsByKakaoId(kakaoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DUPLICATE_PHONE_NUMBER.getMessage());
        }

        // 멤버 생성
        Member member = Member.builder()
                .phoneNumber(phoneNumber)
                .kakaoId(kakaoId)
                .coin(0)
                .state(MemberState.ACTIVE)
                .build();

        return memberRepository.save(member);
    }

    // 멤버 dto 생성
    public MemberDto makeMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .kakaoId(member.getKakaoId())
                .coin(member.getCoin())
                .state(member.getState().getState())
                .build();
    }
}
