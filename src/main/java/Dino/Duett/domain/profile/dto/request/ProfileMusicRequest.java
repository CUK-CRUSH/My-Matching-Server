package Dino.Duett.domain.profile.dto.request;

import Dino.Duett.domain.mood.dto.MoodCreateRequest;
import Dino.Duett.domain.mood.dto.MoodUpdateRequest;
import Dino.Duett.domain.music.dto.MusicUpdateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "음악 취향 수정 요청", type = "multipartForm")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProfileMusicRequest {
    @Schema(description = "인생곡 리스트", example = "[{\"title\": \"title\", \"artist\": \"artist\", \"musicUrl\": \"musicUrl\"}]", nullable = true)
    List<MusicUpdateRequest> lifeMusics;
    @Schema(description = "mood 정보", example = "{\"title\": \"title\", \"artist\": \"artist\", \"moodImageUrl\": \"https://duett-mood-image.s3.ap-northeast-2.amazonaws.com/1.jpg\"}", nullable = true)
    MoodUpdateRequest mood;
}
