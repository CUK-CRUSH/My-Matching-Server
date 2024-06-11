package Dino.Duett.domain.mood.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "mood 조회 응답")
@Getter
@AllArgsConstructor
public class MoodResponse {
    @Schema(description = "연인과 함께 듣고싶은 곡 제목", example = "title")
    String title;
    @Schema(description = "연인과 함께 듣고싶은 곡 아티스트 이름", example = "artist")
    String artist;
    @Schema(description = "연인과 함께 듣고싶은 곡 이미지 URL", example = "https://duett-mood-image.s3.ap-northeast-2.amazonaws.com/1.jpg")
    String moodImageUrl;

    public static MoodResponse of(final String title, final String artist, final String moodImageUrl) {
        return new MoodResponse(
                title,
                artist,
                moodImageUrl
        );
    }
}
