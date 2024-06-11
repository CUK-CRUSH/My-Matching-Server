package Dino.Duett.domain.tag.dto.response;

import Dino.Duett.domain.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "태그 응답")
@Getter
@AllArgsConstructor
public class TagResponse {
    @Schema(description = "태그 ID", example = "1")
    Long tagId;
    @Schema(description = "태그 이름", example = "음악")
    String name;
    @Schema(description = "태그 강조 순위. 2:강조, 1:일반, 0: 선택안함", example = "1")
    int priority;

    public static TagResponse of(Tag tag, int priority){
        return new TagResponse(tag.getId(), tag.getName(), priority);
    }
}
