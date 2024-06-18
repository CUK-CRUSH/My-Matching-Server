package Dino.Duett.domain.search.controller;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.member.repository.MemberRepository;
import Dino.Duett.domain.music.repository.MusicRepository;
import Dino.Duett.domain.search.service.SearchService;
import Dino.Duett.utils.TestUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EnvBean envBean;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MusicRepository musicRepository;
    @MockBean
    private SearchService searchService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유튜브 검색 테스트 - 성공")
    public void searchVideoTest() throws Exception {
        // given
        final String GET_API = "/api/v1/search/video";

        Member member = testUtil.createMember();
        memberRepository.save(member);

        // when
        MvcResult result = mockMvc.perform(get(GET_API)
                        .header("Authorization", "Bearer " + testUtil.createAccessToken(member.getId()))
                        .param("q", "test")
                        .param("maxResults", "8"))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Response Body: " + responseBody);
    }

    @Test
    @DisplayName("유튜브 검색 테스트 - 미 로그인 시 권한없음")
    public void searchVideoTestFailed() throws Exception {
        // given
        final String GET_API = "/api/v1/search/video";

        // when & then
        mockMvc.perform(get(GET_API)
                        .param("q", "test")
                        .param("maxResults", "8"))
                .andExpect(status().isUnauthorized());
    }
}