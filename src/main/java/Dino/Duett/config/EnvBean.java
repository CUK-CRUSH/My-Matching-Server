package Dino.Duett.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Component
public class EnvBean {
    @Value("${email.username}")
    private String emailUsername;
    @Value("${email.password}")
    private String emailPassword;
    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;
    @Value("${spring.cloud.gcp.storage.key-name}")
    private String keyName;
    @Value("${spring.cloud.gcp.storage.bucket-dir}")
    private String bucketDir;
    @Value("${spring.cloud.gcp.storage.bucket-name}")
    private String bucketName;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(keyName);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
