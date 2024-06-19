package Dino.Duett.domain.member.service;

import Dino.Duett.domain.authentication.dto.CheckMemberDto;
import Dino.Duett.domain.member.dto.MemberDto;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.entity.Role;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.enums.RoleName;
import Dino.Duett.domain.member.exception.MemberException;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.member.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    // 멤버 엔티티 생성
    @Transactional
    public Member createMember(String phoneNumber, String kakaoId) throws MemberException {
        // 중복 체크
        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new MemberException.DuplicatePhoneNumberException();
        }
        if (memberRepository.existsByKakaoId(kakaoId)) {
            throw new MemberException.DuplicateKakaoIdException();
        }
        Role role = roleRepository.findByName(RoleName.USER.name())
                .orElseThrow(MemberException.RoleNotFoundException::new);

        // 멤버 생성
        Member member = Member.builder()
                .phoneNumber(phoneNumber)
                .kakaoId(kakaoId)
                .coin(0)
                .state(MemberState.ACTIVE)
                .role(role)
                .build();

        return memberRepository.save(member);
    }

    // 멤버 dto 생성
    @Transactional
    public MemberDto makeMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .phoneNumber(member.getPhoneNumber())
                .kakaoId(member.getKakaoId())
                .coin(member.getCoin())
                .state(member.getState().getState())
                .role(member.getRole().getName())
                .build();
    }

    public CheckMemberDto existsByPhoneNumber(String phoneNumber) {
        return CheckMemberDto.of(memberRepository.existsByPhoneNumber(phoneNumber));
    }
}
