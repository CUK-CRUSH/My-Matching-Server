package Dino.Duett.domain.signup.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignUpReq {
    private String phoneNumber;
    private String code;
    private String nickname;
    private String kakaoId;
    private String sex;
    private String birth;
    private int[] location;
    private MultipartFile profileImage;
    private String comment;
}
