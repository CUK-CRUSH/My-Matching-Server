package Dino.Duett.domain.authentication.controller;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.domain.authentication.dto.CheckMemberDto;
import Dino.Duett.domain.authentication.dto.VerificationCodeDto;
import Dino.Duett.domain.member.service.MemberService;
import Dino.Duett.global.dto.JsonBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final VerificationCodeManager verificationCodeManager;
    private final MemberService memberService;

    @Operation(summary = "인증 코드 요청")
    @GetMapping(value = "/code")
    public JsonBody<VerificationCodeDto> requestCode(@NotBlank @RequestParam("phoneNumber") String phoneNumber) {
        return JsonBody.of(200, "인증 코드 요청 성공", verificationCodeManager.requestCodeDto(phoneNumber));
    }

    @Operation(summary = "사용자 회원가입 여부 확인")
    @GetMapping(value = "/member/exists")
    public JsonBody<CheckMemberDto> checkMember(@NotBlank @RequestParam("phoneNumber") String phoneNumber) {
        return JsonBody.of(200, "사용자 회원가입 여부 확인 성공", memberService.existsByPhoneNumber(phoneNumber));
    }
}
