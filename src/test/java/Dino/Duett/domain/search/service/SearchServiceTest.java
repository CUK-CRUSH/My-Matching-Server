package Dino.Duett.domain.search.service;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.search.dto.VideoResponse;
import Dino.Duett.domain.search.exception.YoutubeException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Disabled
class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @MockBean
    private EnvBean envBean;

    @MockBean
    private YouTube youtube;

    private YouTube.Search.List search;


    @BeforeEach
    void setUp() throws IOException {
        SearchResultSnippet snippet = new SearchResultSnippet();
        snippet.setTitle("Video 1");
        snippet.setChannelId("1");
        snippet.setChannelTitle("Channel 1");
        snippet.setDescription("Description 1");
        SearchResult searchResult = new SearchResult();
        searchResult.setSnippet(snippet);
        SearchListResponse searchListResponse = new SearchListResponse();
        searchListResponse.setItems(Collections.singletonList(searchResult));

        search = Mockito.mock(YouTube.Search.List.class);
        Mockito.when(search.execute()).thenReturn(searchListResponse);
        Mockito.when(youtube.search().list(any())).thenReturn(search);
    }

    @Test
    @DisplayName("유튜브 비디오 검색 테스트")
    void searchVideos() throws IOException {
        // given
        Mockito.when(envBean.getYoutubeKeyMaxSize()).thenReturn(3);
        Mockito.when(envBean.getYoutubeApiKey1()).thenReturn("API_KEY_1");

        // when
        List<VideoResponse> result = searchService.searchVideos("test", 5L);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Video 1", result.get(0).getTitle());
    }

    @Test
    @Disabled
    @DisplayName("유튜브 API 할당량 소진 테스트")
    void searchVideos_ApiQuotaExceeded() throws IOException {

        // given
        HttpHeaders headers = new HttpHeaders().set("Content-Type", "application/json");
        GoogleJsonResponseException exception = (GoogleJsonResponseException) new GoogleJsonResponseException.Builder(
                403, "Forbidden", headers).build();

        Mockito.when(search.execute()).thenThrow(exception);
        Mockito.when(envBean.getYoutubeKeyMaxSize()).thenReturn(3);
        Mockito.when(envBean.getYoutubeApiKey1()).thenReturn("API_KEY_1");
        Mockito.when(envBean.getYoutubeApiKey2()).thenReturn("API_KEY_2");
        Mockito.when(envBean.getYoutubeApiKey3()).thenReturn("API_KEY_3");

        // when & then
        assertThrows(YoutubeException.YoutubeApiRequestLimitExceeded.class, () -> {
            searchService.searchVideos("test", 5L);
        });
    }
}