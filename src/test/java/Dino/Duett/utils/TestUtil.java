package Dino.Duett.utils;

import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.entity.Role;
import Dino.Duett.domain.member.enums.MemberState;
import Dino.Duett.domain.member.enums.RoleName;
import Dino.Duett.domain.signup.dto.SignUpReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    public static final String MEMBER_PHONE_NUMBER = "01012345678";
    public static final String MEMBER_KAKAO_ID = "kakaoId";
    public static final String MEMBER_NICKNAME = "nickname";

    /**
     * 테스트용 회원가입 요청 생성
     * @return SignUpReq
     * */
    public static SignUpReq makeSignUpReq() {
        SignUpReq signUpReq = new SignUpReq();
        MockMultipartFile multipartFile = new MockMultipartFile("profileImage", "profileImage.jpg", "image/jpeg", "profileImage".getBytes());

        signUpReq.setPhoneNumber(MEMBER_PHONE_NUMBER);
        signUpReq.setCode("code");
        signUpReq.setNickname(MEMBER_NICKNAME);
        signUpReq.setKakaoId(MEMBER_KAKAO_ID);
        signUpReq.setSex("male");
        signUpReq.setBirth("1997-10-31");
        signUpReq.setLocation(new double[]{1.1, 2.2});
        signUpReq.setProfileImage(multipartFile);
        signUpReq.setComment("comment");

        return signUpReq;
    }

    /**
     * 테스트용 멤버 생성. id 1의 role 사용해서 만듦. 멤버 id는 생성되지 않음.
     * @return Member
    * */
    public static Member createMember() {
        // 역할 생성
        Role role = Role.builder()
                .id(1L)
                .name(RoleName.USER.name())
                .build();

        return Member.builder()
                .phoneNumber(MEMBER_PHONE_NUMBER)
                .kakaoId(MEMBER_KAKAO_ID)
                .coin(0)
                .state(MemberState.ACTIVE)
                .role(role)
                .build();
    }
}
