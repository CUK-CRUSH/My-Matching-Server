package Dino.Duett.domain.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static Dino.Duett.domain.authentication.enums.Message.INVALID_VERIFICATION_CODE;

@Service
@RequiredArgsConstructor
public class VerificationCodeManager {
    private final StringRedisTemplate redisTemplate;

    public String generateVerificationCode(String phoneNumber) {
        String code = UUID.randomUUID().toString(); // 랜덤한 코드 생성
        redisTemplate.opsForValue().set(phoneNumber, code, 10, TimeUnit.MINUTES); // Redis에 저장, 10분 후 만료
        return code;
    }

    public void verifyCode(String phoneNumber, String code) throws ResponseStatusException {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_VERIFICATION_CODE.getMessage());
        }
    }

    public void deleteCode(String phoneNumber) {
        redisTemplate.delete(phoneNumber);
    }
}