package Dino.Duett.domain.profile.dto.response;

import Dino.Duett.domain.mood.dto.MoodResponse;
import Dino.Duett.domain.music.dto.MusicResponse;
import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.tag.dto.request.TagRequest;
import Dino.Duett.domain.tag.dto.response.TagResponse;
import Dino.Duett.domain.tag.entity.Tag;
import Dino.Duett.domain.tag.enums.TagType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Schema(description = "프로필 카드 조회 요약 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileCardSummaryResponse {
    @Schema(description = "프로필 ID",example = "1", nullable = true)
    private Long profileId;
    @Schema(description = "사용자의 이름", example = "name", nullable = true)
    private String name;
    @Schema(description = "사용자의 나이", example = "crush", nullable = true)
    private String age;
    @Schema(description = "MBTI 유형", example = "ENTP", nullable = true)
    private String mbti;
    @Schema(description = "한 줄 소개", example = "crush", nullable = true)
    private String oneLineIntroduction;
    @Schema(description = "사용자와의 거리", example = "1.5", nullable = true)
    private double distance;
    @Schema(description = "프로필 이미지 URL", example = "crush", nullable = true)
    private String profileImageUrl;
    @Schema(description = "음악 태그", example = "팝", nullable = true)
    private String musicTag;
    @Schema(description = "취미 태그", example = "뮤지컬", nullable = true)
    private String hobbyTag;

    public static ProfileCardSummaryResponse of(final Profile profile, final double distance, final String profileImageUrl) {
        return new ProfileCardSummaryResponse(
            profile.getId(),
            profile.getName(),
            Period.between(profile.getBirthDate().toLocalDate(), LocalDateTime.now().toLocalDate()).getYears() + "세",
            profile.getMbti().name(),
            profile.getOneLineIntroduction(),
            distance,
            profileImageUrl,
            profile.getProfileTags().stream().filter(profileTag -> profileTag.getTag().getType().equals(TagType.MUSIC.getType())).map(profileTag -> profileTag.getTag().getName()).toList().toString(),
            profile.getProfileTags().stream().filter(profileTag -> profileTag.getTag().getType().equals(TagType.HOBBY.getType())).map(profileTag -> profileTag.getTag().getName()).toList().toString()
        );
    }
}
