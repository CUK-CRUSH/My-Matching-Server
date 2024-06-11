package Dino.Duett.domain.tag.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "태그 등록 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TagRequest {
    @Schema(description = "태그 이름", example = "음악")
    String name;
    @Schema(description = "태그 강조 순위. 2:강조, 1:일반, 0: 선택안함", example = "1")
    int priority;
}
