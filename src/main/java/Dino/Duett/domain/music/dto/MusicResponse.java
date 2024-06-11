package Dino.Duett.domain.music.dto;

import Dino.Duett.domain.music.entity.Music;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(description = "음악 조회 응답")
@Getter
@AllArgsConstructor
public class MusicResponse {
    @Schema(description = "음악 id", name = "musicId")
    @NotEmpty
    private Long musicId;
    @Schema(description = "음악 제목", name = "title")
    @NotEmpty
    private String title;
    @Schema(description = "아티스트 이름", name = "artist")
    @NotEmpty
    private String artist;
    @Schema(description = "음악 링크 고유식별자", name = "urlName")
    @NotBlank
    private String url;

    public static MusicResponse of(Music music){
        return new MusicResponse(music.getId(), music.getTitle(), music.getArtist(), music.getUrl());
    }
}
