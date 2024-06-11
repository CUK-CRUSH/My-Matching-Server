package Dino.Duett.domain.profile.dto.response;

import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.tag.dto.request.TagRequest;
import Dino.Duett.domain.tag.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "내 소개 조회 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileIntroResponse {
    @Schema(description = "MBTI", example = "ENFP", nullable = true)
    String mbti;
    @Schema(description = "음악 태그", example = "[{\"name\": \"Pop\", \"depth\": 0}, {\"name\": \"Rock\", \"depth\": 1}]", nullable = true)
    List<TagResponse> musicTags;
    @Schema(description = "취미 태그", example = "[{\"name\": \"Pop\", \"depth\": 0}, {\"name\": \"Rock\", \"depth\": 1}]", nullable = true)
    List<TagResponse> hobbyTags;
    @Schema(description = "자기소개", example = "안녕하세요!", minLength = 50, maxLength = 500, nullable = true)
    String selfIntroduction;
    @Schema(description = "호감을 느낄만한 상대의 음악취향", example = "인디", minLength = 50, maxLength = 500, nullable = true)
    String likeableMusicTaste;

    public static ProfileIntroResponse of(final String mbti,
                                          final List<TagResponse> musicTags,
                                          final List<TagResponse> hobbyTags,
                                          final String selfIntroduction,
                                          final String likeableMusicTaste) {
        return new ProfileIntroResponse(
                mbti,
                musicTags,
                hobbyTags,
                selfIntroduction,
                likeableMusicTaste
        );
    }
}
