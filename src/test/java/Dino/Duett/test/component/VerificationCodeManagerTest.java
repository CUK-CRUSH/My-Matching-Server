package Dino.Duett.test.component;

import Dino.Duett.domain.authentication.VerificationCodeManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("VerificationCodeManager 테스트")
public class VerificationCodeManagerTest {
    @InjectMocks
    private VerificationCodeManager verificationCodeManager;
    @Mock
    private StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("인증 코드 생성 테스트")
    public void generateVerificationCodeTest() {
        // given
        String phoneNumber = "01044206790";
        given(redisTemplate.opsForValue()).willReturn(mock(ValueOperations.class));

        // when
        String code = verificationCodeManager.generateVerificationCode(phoneNumber);
        given(redisTemplate.opsForValue().get(phoneNumber)).willReturn(code);

        // then
        verificationCodeManager.verifyCode(phoneNumber, code);
    }
}
