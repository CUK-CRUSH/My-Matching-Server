package Dino.Duett.test.controller;


import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.utils.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @DisplayName("로그인 테스트")
    public void loginTest(TestReporter testReporter) throws Exception {
        // given
        Member member = memberService.createMember(TestUtil.MEMBER_PHONE_NUMBER, TestUtil.MEMBER_KAKAO_ID);
        String code = verificationCodeManager.generateVerificationCode(TestUtil.MEMBER_PHONE_NUMBER);
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
}
