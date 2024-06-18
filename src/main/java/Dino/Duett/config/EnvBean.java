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

    @Value("${youtube.api-key1}")
    private String youtubeApiKey1;
    @Value("${youtube.api-key2}")
    private String youtubeApiKey2;
    @Value("${youtube.api-key3}")
    private String youtubeApiKey3;
    @Value("${youtube.api-key4}")
    private String youtubeApiKey4;
    @Value("${youtube.api-key5}")
    private String youtubeApiKey5;
    @Value("${youtube.api-key6}")
    private String youtubeApiKey6;
    @Value("${youtube.api-key7}")
    private String youtubeApiKey7;
    @Value("${youtube.api-key8}")
    private String youtubeApiKey8;
    @Value("${youtube.api-key9}")
    private String youtubeApiKey9;
    @Value("${youtube.api-key10}")
    private String youtubeApiKey10;
    @Value("${youtube.api-key11}")
    private String youtubeApiKey11;
    @Value("${youtube.api-key12}")
    private String youtubeApiKey12;
    @Value("${youtube.api-key13}")
    private String youtubeApiKey13;
    @Value("${youtube.api-key14}")
    private String youtubeApiKey14;

    private final int youtubeKeyMaxSize = 14;

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
