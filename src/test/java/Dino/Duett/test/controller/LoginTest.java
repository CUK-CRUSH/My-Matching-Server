package Dino.Duett.test.controller;


import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.global.exception.ErrorCode;
import Dino.Duett.gmail.GmailReader;
import Dino.Duett.gmail.exception.GmailException;
import Dino.Duett.utils.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("로그인 테스트")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private VerificationCodeManager verificationCodeManager;

    @MockBean
    private GmailReader gmailReader;

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest(TestReporter testReporter) throws Exception {
        // given
        Member member = memberService.createMember(TestUtil.MEMBER_PHONE_NUMBER, TestUtil.MEMBER_KAKAO_ID);
        String code = verificationCodeManager.generateVerificationCode(member.getPhoneNumber());
        doAnswer(invocation -> null).when(gmailReader).validate(member.getPhoneNumber(), code);

        // when
        testReporter.publishEntry(mockMvc.perform(
                post("/login")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("phoneNumber", TestUtil.MEMBER_PHONE_NUMBER)
                        .param("verificationCode", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn().getResponse().getContentAsString());
        // then
    }

    @Test
    @DisplayName("로그인 실패 테스트 - Redis에 저장된 코드와 입력한 코드가 다를 때")
    public void loginFailTest(TestReporter testReporter) throws Exception {
        // given
        Member member = memberService.createMember(TestUtil.MEMBER_PHONE_NUMBER, TestUtil.MEMBER_KAKAO_ID);
        String code = verificationCodeManager.generateVerificationCode(member.getPhoneNumber());
        doAnswer(invocation -> null).when(gmailReader).validate(member.getPhoneNumber(), code);

        // when
        testReporter.publishEntry(mockMvc.perform(
                post("/login")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("phoneNumber", TestUtil.MEMBER_PHONE_NUMBER)
                        .param("verificationCode", "wrong code"))
                .andExpect(status().is(ErrorCode.LOGIN_FAILED.getCode()))
                .andReturn().getResponse().getContentAsString());
        // then
    }
}
