package Dino.Duett.domain.tag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "태그 타입별 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TagByTypeRequest {
    @Schema(description = "음악 태그 목록")
    List<TagRequest> musicTags;
    @Schema(description = "취미 태그 목록")
    List<TagRequest> hobbyTags;
}
