package Dino.Duett.test.component;

import Dino.Duett.config.EnvBean;
import Dino.Duett.gmail.GmailReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static Dino.Duett.gmail.enums.Message.EMAIL_VALIDATION_FAILED;
import static Dino.Duett.gmail.enums.Message.NO_MESSAGES_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("EmailReader 테스트")
public class GmailReaderTest {
    @InjectMocks
    private GmailReader gmailReader;
    @Mock
    private EnvBean env;

    @Test
    @DisplayName("email 인증 성공 테스트")
    public void validateTest() {
        // given
        String phoneNumber = "01044206790";
        String code = "ㅡ";
        given(env.getEmailUsername()).willReturn(System.getenv("EMAIL_USERNAME"));
        given(env.getEmailPassword()).willReturn(System.getenv("EMAIL_PASSWORD"));

        // when
        gmailReader.validate(phoneNumber, code);
    }

    @Test
    @DisplayName("email 인증 실패 테스트 - 메일이 없는 경우")
    public void validateFailTest1() {
        // given
        String phoneNumber = "0123456789";
        String code = "test2";
        given(env.getEmailUsername()).willReturn(System.getenv("EMAIL_USERNAME"));
        given(env.getEmailPassword()).willReturn(System.getenv("EMAIL_PASSWORD"));

        // when
        Throwable throwable = catchThrowable(() -> gmailReader.validate(phoneNumber, code));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(((ResponseStatusException) throwable).getMessage()).isEqualTo("400 BAD_REQUEST \"" + NO_MESSAGES_FOUND.getMessage() +"\"");
    }

    @Test
    @DisplayName("email 인증 실패 테스트 - 코드가 일치하지 않는 경우")
    public void validateFailTest2() {
        // given
        String phoneNumber = "01044206790";
        String code = "wrongCode";
        given(env.getEmailUsername()).willReturn(System.getenv("EMAIL_USERNAME"));
        given(env.getEmailPassword()).willReturn(System.getenv("EMAIL_PASSWORD"));

        // when
        Throwable throwable = catchThrowable(() -> gmailReader.validate(phoneNumber, code));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(((ResponseStatusException) throwable).getMessage()).isEqualTo("401 UNAUTHORIZED \"" + EMAIL_VALIDATION_FAILED.getMessage() +"\"");
    }

    @Test
    @DisplayName("email 인증 실패 테스트 - 전화번호가 숫자로만 이루어지진게 아닌 경우")
    public void validateFailTest3() {
        // given
        String phoneNumber = "010-4420-6790";
        String code = "ㅡ";

        // when
        Throwable throwable = catchThrowable(() -> gmailReader.validate(phoneNumber, code));

        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(((ResponseStatusException) throwable).getMessage()).isEqualTo("400 BAD_REQUEST \"" + EMAIL_VALIDATION_FAILED.getMessage() +"\"");
    }
}
