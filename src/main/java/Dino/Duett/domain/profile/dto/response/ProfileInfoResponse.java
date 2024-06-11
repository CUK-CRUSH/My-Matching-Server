package Dino.Duett.domain.profile.dto.response;

import Dino.Duett.domain.profile.entity.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.Multipart;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
@Schema(description = "내 정보 조회 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileInfoResponse {
    @Schema(description = "프로필 이미지 URL", example = "https://duett.com/image.jpg")
    String profileImageUrl;
    @Schema(description = "닉네임", example = "crush", maxLength = 20)
    String name;
    @Schema(description = "생년월일", example = "2000.11.01")
    String birthDate;
    @Schema(description = "성별", example = "여성")
    String gender;
    @Schema(description = "프로필 한줄소개", example = "안녕하세요!", maxLength = 30)
    String oneLineIntroduction;

    public static ProfileInfoResponse of(final Profile profile, final String profileImageUrl) {
        return new ProfileInfoResponse(
                profileImageUrl,
                profile.getName(),
                profile.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                profile.getGender().name(),
                profile.getOneLineIntroduction()
        );
    }
}
