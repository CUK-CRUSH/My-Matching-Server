package Dino.Duett.domain.image.service;

import Dino.Duett.config.EnvBean;
import Dino.Duett.domain.image.dto.ImageDto;
import Dino.Duett.domain.image.entity.Image;
import Dino.Duett.domain.image.exception.ImageException;
import Dino.Duett.domain.image.repository.ImageRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@Slf4j(topic = "ImageService")
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final EnvBean envBean;
    private final Storage storage;

    public String getUrl(Image image) {
        return String.format("https://storage.googleapis.com/%s/%s", envBean.getBucketName(), image.getName());
    }

    private byte[] convertToWebP(MultipartFile file) throws IOException, ImageException {
        // MultipartFile을 BufferedImage로 변환
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new ImageException.MultipartFileConvertFailedException(); // MultipartFile 변환 실패
        }

        // 비표준 색공간 이미지를 표준 RGB로 변환
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = convertedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // WebP 포맷으로 이미지 저장을 위한 설정
        ImageWriter writer = ImageIO.getImageWritersByMIMEType(file.getContentType()).next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        if (writeParam.canWriteCompressed()) {
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.8f); // 압축 품질 설정 (0 ~ 1)
        }

        // BufferedImage를 WebP 형식으로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(convertedImage, null, null), writeParam);
        } catch (IOException e) {
            log.error("ImageService.convertToWebP: {}", e.getMessage());
            throw new ImageException.ImageConvertFailedException(); // Image 변환 실패
        } finally {
            writer.dispose();
        }

        return baos.toByteArray();
    }


    public ImageDto saveImageToDto(MultipartFile imageFile) throws ImageException {
        Image image = saveImage(imageFile);
        return ImageDto.builder()
                .id(image.getId())
                .name(image.getName())
                .extension(image.getExtension())
                .uuid(image.getUuid())
                .url(getUrl(image))
                .build();
    }

    /**
     * 이미지 저장
     * GCS에 이미지를 저장하고 DB에 이미지 엔티티를 저장합니다.
     * */
    public Image saveImage(MultipartFile imageFile) throws ImageException {
        // 확장자를 webp로 고정
        String ext = "image/webp";
        String uuid = UUID.randomUUID().toString();
        String filePath = envBean.getBucketDir() + "/" + uuid;

        // 파일은 https://storage.googleapis.com/{버킷_이름}/{디렉토리}/{UUID}를 통해 조회할 수 있음
        BlobInfo imageInfo = BlobInfo.newBuilder(envBean.getBucketName(), filePath)
                .setContentType(ext)
                .build();

        try {
            byte[] webp = convertToWebP(imageFile);
            Blob blob = storage.create(imageInfo, webp);
            String fileName = blob.getName();

            Image image = Image.builder()
                    .name(fileName)
                    .extension(ext)
                    .uuid(uuid)
                    .build();

            return imageRepository.save(image);
        } catch (IOException e) {
            log.error("ImageService.saveImageToGcs: {}", e.getMessage());
            throw new ImageException.ImageSaveFailedException(); // Image 저장 실패
        }
    }

    /**
     * 이미지 삭제
     * GCS 이미지 삭제 후 DB에서 이미지 엔티티를 삭제합니다.
     * */
    public void deleteImage(Long id) throws ImageException {
        Image image = imageRepository.findById(id)
                .orElseThrow(ImageException.ImageNotFoundException::new); // Image 찾을 수 없음
        String fileName = image.getName();
        Blob blob = storage.get(envBean.getBucketName(), fileName);
        try {
            Storage.BlobSourceOption precondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            storage.delete(envBean.getBucketName(), fileName, precondition);

            imageRepository.deleteByName(fileName);
        } catch (Exception e) {
            log.error("ImageService.deleteImageToGcs: {}", e.getMessage());
            throw new ImageException.ImageDeleteFailedException(); // Image 삭제 실패
        }
    }
}
