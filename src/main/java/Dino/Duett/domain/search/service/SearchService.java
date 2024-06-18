package Dino.Duett.domain.search.service;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.search.dto.VideoResponse;
import Dino.Duett.domain.search.exception.YoutubeException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static Dino.Duett.global.enums.LimitConstants.YOUTUBE_SEARCH_LIMIT;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final EnvBean envBean;
    private static int youtubeKeyCurrentIndex = 0;

    /** @param index 유튜브 API 키 인덱스
     * @return 유튜브 API 키
     */
    private String getYoutubeApiKey(final int index) throws IllegalArgumentException {
        return switch (index) {
            case 0 -> envBean.getYoutubeApiKey1();
            case 1 -> envBean.getYoutubeApiKey2();
            case 2 -> envBean.getYoutubeApiKey3();
            case 3 -> envBean.getYoutubeApiKey4();
            case 4 -> envBean.getYoutubeApiKey5();
            case 5 -> envBean.getYoutubeApiKey6();
            case 6 -> envBean.getYoutubeApiKey7();
            case 7 -> envBean.getYoutubeApiKey8();
            case 8 -> envBean.getYoutubeApiKey9();
            case 9 -> envBean.getYoutubeApiKey10();
            case 10 -> envBean.getYoutubeApiKey11();
            case 11 -> envBean.getYoutubeApiKey12();
            case 12 -> envBean.getYoutubeApiKey13();
            case 13 -> envBean.getYoutubeApiKey14();
            default -> throw new IllegalArgumentException("Youtube API 허용 인덱스 초과");
        };
    }

    /** @param search 유튜브 검색 요청 객체
     * @return 유튜브 동영상 검색 결과 목록
     */
    private List<SearchResultSnippet> requestYoutubeSearching(final YouTube.Search.List search) throws IOException {
        for (int i = 0; i < envBean.getYoutubeKeyMaxSize(); i++) {
            try {
                search.setKey(getYoutubeApiKey(youtubeKeyCurrentIndex));
                return search.execute().getItems().stream()
                        .map(SearchResult::getSnippet)
                        .toList();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 403) {
                    log.error( "[GoogleJsonResponseException] " + (youtubeKeyCurrentIndex+1)+ "번째 YouTube API Key가 만료되었습니다.");
                    youtubeKeyCurrentIndex = (youtubeKeyCurrentIndex+1) % envBean.getYoutubeKeyMaxSize();
                } else {
                    throw new YoutubeException.YoutubeApiRequestFailed();
                }
            } catch (IllegalArgumentException e) {
                log.error( "[IllegalArgumentException] " + e.getMessage());
                throw new YoutubeException.YoutubeApiRequestFailed();
            }
        }
        throw new YoutubeException.YoutubeApiRequestLimitExceeded();
    }

    /** @param q 검색어
     * @param maxResults 최대 검색 결과 개수
     * @return 검색 결과
     */
    public List<VideoResponse> searchVideos(final String q, Long maxResults){
        try {
            // YouTube Data API에 접근할 수 있는 YouTube 클라이언트 생성
            YouTube youtube = new YouTube.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    request -> {
                    })
                    .build();

            // YouTube Data API를 사용해 동영상 검색을 위한 요청 객체 생성. 최대 사이즈 제한
            YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"))
                    .setQ(q)
                    .setType(Collections.singletonList("video"))
                    .setMaxResults(Math.min(maxResults, YOUTUBE_SEARCH_LIMIT.getLimit()));

            // 검색 요청 실행 후 검색 결과에서 동영상 목록 가져오기
            List<SearchResultSnippet> searchResultSnippets = requestYoutubeSearching(search);

            return searchResultSnippets.stream()
                    .map(VideoResponse::of)
                    .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new YoutubeException.YoutubeApiRequestFailed();
        }
    }
}
