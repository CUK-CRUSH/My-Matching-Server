package Dino.Duett.test.service;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.image.entity.Image;
import Dino.Duett.domain.image.repository.ImageRepository;
import Dino.Duett.domain.image.service.ImageService;
import Dino.Duett.utils.TestUtil;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("ImageService 단위 테스트")
public class ImageServiceTest {
    @InjectMocks
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private EnvBean envBean;
    @Mock
    private Storage storage;

    @Test
    @DisplayName("가짜 이미지 파일을 만들고 GCS 업로드 테스트.")
    void saveImageTest() throws IOException {
        // given
        InputStream fileInputStream = TestUtil.makeImageFile();
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", fileInputStream);
        Blob blob = mock(Blob.class);
        String fileName = "duett-test-dir/" + imageFile.getOriginalFilename();

        given(envBean.getBucketName()).willReturn("duett-test-bucket");
        given(envBean.getBucketDir()).willReturn("duett-test-dir");

        given(storage.create(any(BlobInfo.class), any(byte[].class))).willReturn(blob);
        given(blob.getName()).willReturn(fileName);
        given(imageRepository.save(any(Image.class))).willAnswer(invocation -> {
            Image image = invocation.getArgument(0);
            return Image.builder()
                    .id(1L)
                    .uuid(image.getUuid())
                    .extension(image.getExtension())
                    .name(image.getName())
                    .build();
        });

        // when
        Image image = imageService.saveImage(imageFile);

        // then
        assertThat(image.getName()).isEqualTo(fileName);
    }
}

@SpringBootTest
@DisplayName("실제 이미지 파일을 GCS에 업로드 테스트. 따라서 필요 시 직접 테스트할 것.")
@Disabled
class GCSTest {
    @Autowired
    private ImageService imageService;
    private final ArrayList<Image> testImages = new ArrayList<>();

    @Test
    @DisplayName("이미지 파일 GCS에 저장 테스트.")
    void saveImageToGcsTest() throws IOException {
        // given
        InputStream fileInputStream = TestUtil.makeImageFile();
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", fileInputStream);

        // when
        Image image = imageService.saveImage(imageFile);
        testImages.add(image);

        // then
        assertThat(image.getId()).isNotNull();
        assertThat(image.getName()).isNotNull();
    }

    @AfterEach
    @DisplayName("테스트 후에 GCS에 새로 생긴 이미지 파일 삭제")
    void deleteImageToGcsTest() {
        testImages.forEach(image -> {
            System.out.println(image.getName() + " 이미지 삭제 완료");
            imageService.deleteImage(image.getId());
        });
    }

}
