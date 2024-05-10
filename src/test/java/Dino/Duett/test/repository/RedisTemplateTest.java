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

}
