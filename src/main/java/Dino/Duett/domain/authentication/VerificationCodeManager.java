package Dino.Duett.domain.authentication;

import Dino.Duett.domain.authentication.dto.VerificationCodeDto;
import Dino.Duett.domain.authentication.exception.AuthenticationException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static Dino.Duett.global.exception.ErrorCode.INVALID_VERIFICATION_CODE;

@Service
@RequiredArgsConstructor
public class VerificationCodeManager {
    private final StringRedisTemplate redisTemplate;

    public String generateVerificationCode(String phoneNumber) {
        String code = UUID.randomUUID().toString(); // 랜덤한 코드 생성
        redisTemplate.opsForValue().set(phoneNumber, code, 10, TimeUnit.MINUTES); // Redis에 저장, 10분 후 만료
        return code;
    }

    public VerificationCodeDto requestCodeDto(String phoneNumber) {
        String code = generateVerificationCode(phoneNumber);
        return VerificationCodeDto.builder()
                .code(code)
                .build();
    }

    public void verifyCode(String phoneNumber, String code) throws ResponseStatusException {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new AuthenticationException.InvalidVerificationCodeException();
        }
    }

    public String getCode(String phoneNumber) {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        if (storedCode == null) {
            throw new AuthenticationException.NotFoundVerificationCodeException();
        }
        return storedCode;
    }

    public void deleteCode(String phoneNumber) {
        redisTemplate.delete(phoneNumber);
    }
}