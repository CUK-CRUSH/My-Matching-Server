package Dino.Duett.domain.authentication.controller;

import Dino.Duett.domain.authentication.dto.LoginDto;
import Dino.Duett.global.dto.JsonBody;
import Dino.Duett.global.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "로그인 API")
@RestController
@RequestMapping("/login")
public class LoginController {
    @Operation(summary = "로그인")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonBody<TokenDto> login(@Valid LoginDto loginDto) {
        // 실제 구현은 security filter 에서 처리. 여기는 API 명세하기 위한 용도
        loginDto.setPhoneNumber("01012345678");
        loginDto.setVerificationCode("123456");
        return null;
    }
}
