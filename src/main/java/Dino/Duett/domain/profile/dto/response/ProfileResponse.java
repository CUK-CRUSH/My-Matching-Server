package Dino.Duett.domain.profile.dto.response;

import Dino.Duett.domain.mood.dto.MoodResponse;
import Dino.Duett.domain.music.dto.MusicResponse;
import Dino.Duett.domain.profile.entity.Profile;
import Dino.Duett.domain.tag.dto.response.TagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Schema(description = "프로필 정보 조회 전체 응답")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProfileResponse {
    @Schema(description = "프로필 ID",example = "1", nullable = true)
    private Long profileId;
    @Schema(description = "사용자의 이름", example = "name", nullable = true)
    private String name;
    @Schema(description = "생년월일", example = "2000.11.01", nullable = true)
    String birthDate;
    @Schema(description = "MBTI 유형", example = "ENTP", nullable = true)
    private String mbti;
    @Schema(description = "한 줄 소개", example = "crush", nullable = true)
    private String oneLineIntroduction;
    @Schema(description = "프로필 이미지 URL", example = "crush", nullable = true)
    private String profileImageUrl;
    @Schema(example = "[{\"tagId\": 3, \"name\": \"POP\", \"priority\": 1}, {\"tagId\": 4, \"name\": \"발라드\", \"priority\": 2}, {\"tagId\": 4, \"name\": \"국내힙합\", \"priority\": 0}]", nullable = true)
    private List<TagResponse> MusicTags;
    @Schema(description = "취미 태그", example = "[{\"tagId\": 3, \"name\": \"뮤직페스티벌\", \"priority\": 1}, {\"tagId\": 4, \"name\": \"콘서트\", \"priority\": 2}, , {\"tagId\": 4, \"name\": \"뮤지컬\", \"priority\": 0}]", nullable = true)
    private List<TagResponse> HobbyTags;
    @Schema(description = "인생곡 리스트", example = "[{\"title\": \"title\", \"artist\": \"artist\", \"musicUrl\": \"musicUrl\"}]", nullable = true)
    private List<MusicResponse> LifeMusics;
    @Schema(description = "무드 정보", example = "crush", nullable = true)
    private MoodResponse mood;
    @Schema(description = "긴 자기 소개",example = "crush", nullable = true)
    private String selfIntroduction;
    @Schema(description = "호감을 느낄만한 상대의 음악취향", example = "crush", nullable = true)
    private String likeableMusicTaste;

    public static ProfileResponse of(final Profile profile, final String profileImageUrl, final MoodResponse moodResponse, final List<TagResponse> musicTags, final List<TagResponse> hobbyTags) {
        return new ProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                profile.getMbti().name(),
                profile.getOneLineIntroduction(),
                profileImageUrl,
                musicTags,
                hobbyTags,
                profile.getMusics().stream().map(MusicResponse::of).toList(),
                moodResponse,
                profile.getSelfIntroduction(),
                profile.getLikeableMusicTaste()
        );
    }
}
