package Dino.Duett.domain.search.controller;

import Dino.Duett.domain.search.dto.VideoResponse;
import Dino.Duett.domain.search.service.SearchService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "비디오 검색결과 더보기", description = "유튜브 API를 이용하여 비디오를 검색합니다. 기본 검색 개수는 5개, 최대 검색 개수는 10개입니다.")
    @GetMapping("/video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "9000", description = "유튜브 API 오류 발생"),
            @ApiResponse(responseCode = "9001", description = "유뷰트 API 할당량이 모두 소진")
    })
    public JsonBody<List<VideoResponse>> searchVideo(@RequestParam(name = "q", defaultValue = "", required = false) String q,
                                                     @RequestParam(name = "maxResults", defaultValue = "5", required = false) Long maxResults) {
        List<VideoResponse> searchRes = searchService.searchVideos(q, maxResults);
        return JsonBody.of(HttpStatus.OK.value(),"검색 성공", searchRes);
    }
}
