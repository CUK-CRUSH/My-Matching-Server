package Dino.Duett.test.controller;

import Dino.Duett.domain.signup.dto.SignUpReq;
import Dino.Duett.utils.TestUtil;
import org.junit.jupiter.api.Disabled;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SignUpController 테스트")
public class SignUpControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtil testUtil;

    @Test
    @Disabled  // todo: 현재 스프링 시큐리티 적용이 안되어 있음. 스프링 시큐리티 적용 후 테스트 진행
    @DisplayName("회원가입 테스트")
    public void signUpTest(TestReporter testReporter) throws Exception {
        // given
        SignUpReq signUpReq = testUtil.makeSignUpReq();

        // when, then
        testReporter.publishEntry(mockMvc.perform(
                    post("/api/v1/sign-up")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .content(signUpReq.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }
}
