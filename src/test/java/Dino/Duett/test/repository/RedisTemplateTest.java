package Dino.Duett.test.repository;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import Dino.Duett.gmail.GmailReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@DisplayName("RedisTemplate 테스트")
public class RedisTemplateTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("Redis 작성 테스트")
    public void redisTest() {
        // given
        String phoneNumber = "01044206790";
        String code = "Redis 작성 테스트";

        // when
        redisTemplate.opsForValue().set(phoneNumber, code);
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);

        // then
        assertThat(storedCode).isEqualTo(code);
        System.out.println(storedCode);
    }

    @Test
    @DisplayName("Redis 중복 저장 테스트")
    public void redisDuplicateTest() {
        // given
        String phoneNumber = "01044206790";
        String code1 = "Redis 중복 저장 테스트1";
        String code2 = "Redis 중복 저장 테스트2";
        redisTemplate.opsForValue().set(phoneNumber, code1);

        // when
        redisTemplate.opsForValue().set(phoneNumber, code2);
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);

        // then
        assertThat(storedCode).isEqualTo(code2);
        System.out.println(storedCode);
    }

    @Test
    @DisplayName("Redis 삭제 테스트")
    public void redisDeleteTest() {
        // given
        String phoneNumber = "01044206790";
        String code = "Redis 삭제 테스트";
        redisTemplate.opsForValue().set(phoneNumber, code);

        // when
        redisTemplate.delete(phoneNumber);
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);

        // then
        assertThat(storedCode).isNull();
        System.out.println(storedCode);
    }


}
