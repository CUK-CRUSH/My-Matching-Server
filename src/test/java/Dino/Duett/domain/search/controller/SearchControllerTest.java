package Dino.Duett.domain.search.controller;

import Dino.Duett.config.EnvBean;
import Dino.Duett.config.login.jwt.JwtTokenProvider;
import Dino.Duett.domain.member.entity.Member;
import Dino.Duett.domain.music.repository.MusicRepository;
import Dino.Duett.domain.search.dto.VideoResponse;
import Dino.Duett.domain.search.service.SearchService;
import Dino.Duett.utils.TestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Disabled
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

    @Test
    @DisplayName("유튜브 검색 테스트")
    public void searchVideoTest() throws Exception {
        // given
        final String GET_API = "/api/v1/search/video";

        Member member = testUtil.createMember();

        // when & then
        mockMvc.perform(get("/api/v1/search/video")
                        .header("Authorization", "Bearer " + testUtil.createAccessToken(member.getId()))
                        .param("q", "test")
                        .param("maxResults", "8"))
                .andExpect(status().isOk());

    }
}