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
        signUpReq.setCode(verificationCode);

        // MockMultipartFile 객체를 생성하여 파일을 만듭니다.
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Profile Image Content".getBytes()
        );

        // when, then
        testReporter.publishEntry(mockMvc.perform(
                    multipart("/api/v1/sign-up")
                            .file(profileImage)
                            .param("phoneNumber", signUpReq.getPhoneNumber())
                            .param("code", verificationCode)
                            .param("nickname", signUpReq.getNickname())
                            .param("kakaoId", signUpReq.getKakaoId())
                            .param("sex", signUpReq.getSex())
                            .param("birth", signUpReq.getBirth())
                            .param("location", signUpReq.getLocation()[0] + "," + signUpReq.getLocation()[1])
                            .param("comment", signUpReq.getComment())
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString());
    }
}
