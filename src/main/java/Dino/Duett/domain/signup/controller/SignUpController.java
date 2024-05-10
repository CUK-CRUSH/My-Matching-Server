package Dino.Duett.domain.signup.controller;

import Dino.Duett.domain.signup.dto.SignUpReq;
import Dino.Duett.domain.signup.dto.SignUpRes;
import Dino.Duett.domain.signup.service.SignUpService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SignUp", description = "회원가입 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/signup")
public class SignUpController {
    private final SignUpService signUpService;

    @Operation(summary = "회원가입")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonBody<SignUpRes> signUp(SignUpReq signUpReq) {
        return JsonBody.of(200, "회원가입 성공", signUpService.signUp(signUpReq));
    }
}
