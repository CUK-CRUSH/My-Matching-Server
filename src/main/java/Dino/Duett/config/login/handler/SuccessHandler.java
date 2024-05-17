package Dino.Duett.config.login.handler;

import Dino.Duett.config.login.jwt.JwtTokenProvider;
import Dino.Duett.config.login.jwt.JwtTokenType;
import Dino.Duett.config.security.AuthMember;
import Dino.Duett.domain.authentication.VerificationCodeManager;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증된 사용자 정보를 가져온다.
        AuthMember authMember = (AuthMember) authentication.getPrincipal();
        // 로그인 성공 시 redis 인증코드 삭제
        verificationCodeManager.deleteCode(authMember.getPhoneNumber());

        // 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authMember.getId(), JwtTokenType.ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.createToken(authMember.getId(), JwtTokenType.REFRESH_TOKEN);

        // Map 사용해서 토큰을 JSON 형태로 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.getWriter().write(tokens.toString());
    }
    // 다음 필터 체이닝 호출 과정을 명시적으로 보이게 하기 위해 추가
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        this.onAuthenticationSuccess(request, response, authentication);
        chain.doFilter(request, response);
    }
}
