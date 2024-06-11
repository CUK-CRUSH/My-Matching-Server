package Dino.Duett.domain.mood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "mood 등록 요청", type = "multipartForm")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MoodCreateRequest {
    @Schema(description = "음악 제목", example = "title")
    String title;
    @Schema(description = "음악 아티스트 이름", example = "artist")
    String artist;
    @Schema(description = "mood 이미지", example = "mood_image.png")
    MultipartFile moodImage;
}
