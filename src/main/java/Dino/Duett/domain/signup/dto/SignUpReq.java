package Dino.Duett.domain.signup.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignUpReq {
    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String code;
    @NotBlank
    private String nickname;
    @NotBlank
    private String kakaoId;
    @NotBlank
    private String sex;
    @NotBlank
    private String birth;
    @NotNull
    private double[] location;
    @NotNull
    private MultipartFile profileImage;
    @NotBlank
    private String comment;
}
