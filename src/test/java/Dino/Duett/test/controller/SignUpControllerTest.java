package Dino.Duett.test.controller;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.signup.dto.SignUpReq;
import Dino.Duett.gmail.GmailReader;
import Dino.Duett.utils.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.will;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SignUpController 테스트")
public class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    VerificationCodeManager verificationCodeManager;
    @Autowired
    ObjectMapper objectMapper;
    // 실제 GmailReader를 사용하지 않고 Mocking
    @MockBean
    private GmailReader gmailReader;

    @Test
    @DisplayName("회원가입 테스트")
    public void signUpTest(TestReporter testReporter) throws Exception {
        // given
        String verificationCode = verificationCodeManager.generateVerificationCode(TestUtil.MEMBER_PHONE_NUMBER);
        // GmailReader에서 validate() 메서드에서 예외가 발생하지 않게 하기 위해 Mocking
        doAnswer(invocation -> null).when(gmailReader).validate(anyString(), anyString());

        SignUpReq signUpReq = TestUtil.makeSignUpReq();

        // MockMultipartFile 객체를 생성하여 파일을 만듭니다.
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Profile Image Content".getBytes()
        );

        // 다른 필드들은 문자열로 설정합니다.
//        MockMultipartFile phoneNumber = new MockMultipartFile("phoneNumber", "", "text/plain", signUpReq.getPhoneNumber().getBytes());
//        MockMultipartFile code = new MockMultipartFile("code", "", "text/plain", verificationCode.getBytes());
//        MockMultipartFile nickname = new MockMultipartFile("nickname", "", "text/plain", signUpReq.getNickname().getBytes());
//        MockMultipartFile kakaoId = new MockMultipartFile("kakaoId", "", "text/plain", signUpReq.getKakaoId().getBytes());
//        MockMultipartFile sex = new MockMultipartFile("sex", "", "text/plain", signUpReq.getSex().getBytes());
//        MockMultipartFile birth = new MockMultipartFile("birth", "", "text/plain", signUpReq.getBirth().getBytes());
//        MockMultipartFile location = new MockMultipartFile("location", "", "application/json", objectMapper.writeValueAsBytes(signUpReq.getLocation()));
//        MockMultipartFile comment = new MockMultipartFile("comment", "", "text/plain", signUpReq.getComment().getBytes());

        // when, then
        testReporter.publishEntry(mockMvc.perform(
                    multipart("/api/v1/sign-up")
                            .file(profileImage)
//                            .param("phoneNumber", signUpReq.getPhoneNumber())
//                            .param("code", verificationCode)
//                            .param("nickname", signUpReq.getNickname())
//                            .param("kakaoId", signUpReq.getKakaoId())
//                            .param("sex", signUpReq.getSex())
//                            .param("birth", signUpReq.getBirth())
//                            .param("location", Arrays.toString(signUpReq.getLocation()))
//                            .param("comment", signUpReq.getComment()))
                )
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString());
    }
}
