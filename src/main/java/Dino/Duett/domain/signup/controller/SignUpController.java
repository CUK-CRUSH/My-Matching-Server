package Dino.Duett.domain.signup.controller;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.authentication.dto.VerificationCodeDto;
import Dino.Duett.domain.signup.dto.SignUpReq;
import Dino.Duett.domain.signup.dto.SignUpRes;
import Dino.Duett.domain.signup.service.SignUpService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SignUp", description = "회원가입 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sign-up")
public class SignUpController {
    private final SignUpService signUpService;

    @Operation(summary = "회원가입")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonBody<SignUpRes> signUp(SignUpReq signUpReq) {
        return JsonBody.of(200, "회원가입 성공", signUpService.signUp(signUpReq));
    }

    @Operation(summary = "인증 코드 요청")
    @GetMapping(value = "/code")
    public JsonBody<VerificationCodeDto> requestCode(@RequestParam @NotBlank String phoneNumber) {
        return JsonBody.of(200, "인증 코드 요청 성공", signUpService.requestCode(phoneNumber));
    }
}
