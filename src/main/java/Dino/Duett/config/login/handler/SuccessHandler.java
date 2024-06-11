package Dino.Duett.config.login.handler;

import Dino.Duett.config.login.jwt.JwtTokenProvider;
import Dino.Duett.config.login.jwt.JwtTokenType;
import Dino.Duett.config.security.AuthMember;
import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.global.dto.JsonBody;
import Dino.Duett.global.dto.TokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationCodeManager verificationCodeManager;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증된 사용자 정보를 가져온다.
        AuthMember authMember = (AuthMember) authentication.getPrincipal();
        // 로그인 성공 시 redis 인증코드 삭제
        verificationCodeManager.deleteCode(authMember.getPhoneNumber());

        // 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authMember.getId(), JwtTokenType.ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.createToken(authMember.getId(), JwtTokenType.REFRESH_TOKEN);

        // 토큰 DTO 생성
        TokenDto tokens = TokenDto.of(accessToken, refreshToken);

        // 응답 생성
        JsonBody<TokenDto> jsonBody = JsonBody.of(200, "로그인 성공", tokens);
        // json 형태의 String 으로 변환하여 응답
        response.getWriter().write(objectMapper.writeValueAsString(jsonBody));
    }
    // 다음 필터 체이닝 호출 과정을 명시적으로 보이게 하기 위해 추가
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        this.onAuthenticationSuccess(request, response, authentication);
        chain.doFilter(request, response);
    }
}
