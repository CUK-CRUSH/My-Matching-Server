package Dino.Duett.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EnvBean {
    @Value("${email.username}")
    private String emailUsername;
    @Value("${email.password}")
    private String emailPassword;
}
