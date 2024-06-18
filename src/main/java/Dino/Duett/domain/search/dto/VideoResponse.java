package Dino.Duett.domain.search.dto;

import com.google.api.services.youtube.model.SearchResultSnippet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoResponse {
    @Schema(name = "id", description = "유튜브 비디오 ID")
    private String id;
    @Schema(name = "title", description = "유튜브 비디오 제목")
    private String title;
    @Schema(name = "thumbnail", description = "유튜브 비디오 썸네일 관련 정보")
    private ThumbnailResponse thumbnail;
    @Schema(name = "ChannelTitle", description = "유튜브 채널 이름")
    private String ChannelTitle;

    public static VideoResponse of(final SearchResultSnippet searchResultSnippet) {
        return VideoResponse.builder()
                .id(searchResultSnippet.getChannelId())
                .title(searchResultSnippet.getTitle())
                .thumbnail(ThumbnailResponse.of(searchResultSnippet.getThumbnails().getHigh()))
                .ChannelTitle(searchResultSnippet.getChannelTitle())
                .build();
    }
}

