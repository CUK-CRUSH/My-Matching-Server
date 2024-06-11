package Dino.Duett.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.Multipart;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Schema(description = "내 정보 등록 및 수정 요청", type = "multipartForm")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProfileInfoRequest {
    @Schema(description = "프로필 이미지", example = "image.jpg", nullable = true)
    MultipartFile profileImage;
    @Schema(description = "닉네임", example = "crush", minLength = 3, maxLength = 15, nullable = true)
    String name;
    @Schema(description = "프로필 한줄소개", example = "안녕하세요!", maxLength = 30, nullable = true)
    String oneLineIntroduction;
    @Schema(description = "프로필 이미지 삭제 여부", example = "true", nullable = true)
    Boolean isDeleteProfileImage;
}
