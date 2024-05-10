package Dino.Duett.utils;

import Dino.Duett.domain.signup.dto.SignUpReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {
    public SignUpReq makeSignUpReq() {
        SignUpReq signUpReq = new SignUpReq();
        MockMultipartFile multipartFile = new MockMultipartFile("profileImage", "profileImage.jpg", "image/jpeg", "profileImage".getBytes());

        signUpReq.setPhoneNumber("01012345678");
        signUpReq.setCode("code");
        signUpReq.setNickname("nickname");
        signUpReq.setKakaoId("kakaoId");
        signUpReq.setBirth("1997-10-31");
        signUpReq.setComment("comment");
        signUpReq.setLocation(new int[]{1, 1});
        signUpReq.setSex("male");
        signUpReq.setProfileImage(multipartFile);

        return signUpReq;
    }
}
