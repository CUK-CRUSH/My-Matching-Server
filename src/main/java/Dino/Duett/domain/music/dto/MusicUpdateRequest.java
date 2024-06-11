package Dino.Duett.domain.music.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "음악 수정 요청")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MusicUpdateRequest {
    @Schema(description = "음악 id", name = "musicId")
    @NotEmpty
    private Long musicId;
    @Schema(description = "음악 제목", name = "title")
    @NotEmpty
    @Size(max = 100)
    private String title;
    @Schema(description = "아티스트 이름", name = "artist")
    @NotEmpty
    @Size(max = 100)
    private String artist;
    @Schema(description = "음악 링크 고유식별자", name = "urlName")
    @NotBlank
    private String url;
}
