package Dino.Duett.test.controller;

import Dino.Duett.utils.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController 테스트")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증코드 요청 테스트")
    public void requestVerificationCodeTest(TestReporter testReporter) throws Exception {
        // given
        String phoneNumber = TestUtil.MEMBER_PHONE_NUMBER;
        // when, then
        testReporter.publishEntry(mockMvc.perform(
                get("/api/v1/authentication/code")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.code").exists())
                .andReturn().getResponse().getContentAsString());
    }
}
