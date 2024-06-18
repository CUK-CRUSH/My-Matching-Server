package Dino.Duett.domain.search.dto;

import com.google.api.services.youtube.model.Thumbnail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThumbnailResponse {
    @Schema(name = "url", description = "유튜브 비디오 썸네일 URL")
    private String url;
    @Schema(name = "width", description = "유튜브 비디오 썸네일 너비")
    private Long width;
    @Schema(name = "height", description = "유튜브 비디오 썸네일 높이")
    private Long height;

    public static ThumbnailResponse of(final Thumbnail thumbnail){
        return new ThumbnailResponse(
                thumbnail.getUrl(),
                thumbnail.getWidth(),
                thumbnail.getHeight());
    }
}
