package Dino.Duett.domain.music.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "음악 등록 요청")
@Getter
@AllArgsConstructor
public class MusicCreateRequest {
    @Schema(description = "음악 제목", example = "title")
    @NotEmpty
    private String title;
    @Schema(description = "아티스트 이름", example = "artist")
    @NotEmpty
    private String artist;
    @Schema(description = "음악 링크 고유식별자", example = "urlName")
    @NotBlank
    private String url;
}
