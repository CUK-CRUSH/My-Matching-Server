package Dino.Duett.domain.tag.dto.response;

import Dino.Duett.domain.tag.dto.request.TagRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "태그 타입별 응답")
@Getter
@AllArgsConstructor
public class TagByTypeResponse {
    @Schema(description = "음악 태그 목록")
    List<TagResponse> musicTags;
    @Schema(description = "취미 태그 목록")
    List<TagResponse> hobbyTags;

    public static TagByTypeResponse of(List<TagResponse> musicTags, List<TagResponse> hobbyTags){
        return new TagByTypeResponse(musicTags, hobbyTags);
    }
}
