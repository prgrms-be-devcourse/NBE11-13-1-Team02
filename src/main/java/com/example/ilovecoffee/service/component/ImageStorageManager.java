package com.example.ilovecoffee.service.component;

import com.example.ilovecoffee.constant.PathConstant;
import com.example.ilovecoffee.exception.ImageStorageException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageStorageManager {

    private static final String DEFAULT_IMAGE_URL = "/thumbnails/default.png";
    private static final String IMAGE_URL_PREFIX = "/thumbnails/";

    public String store(MultipartFile image) {
        if(image == null || image.isEmpty()) {
            return "/thumbnails/default.png";
        }
        validate(image);
        try {
            Files.createDirectories(PathConstant.THUMBNAIL_DIRECTORY);
            String originalFilename = sanitizeFilename(image.getOriginalFilename());
            String storedFilename = UUID.randomUUID() + "-" + originalFilename;

            Path targetPath = PathConstant.THUMBNAIL_DIRECTORY
                    .resolve(storedFilename)
                    .normalize();

            if(!targetPath.startsWith(PathConstant.THUMBNAIL_DIRECTORY)) {
                throw new ImageStorageException("올바르지 않은 이미지 파일명입니다.");
            }
            image.transferTo(targetPath);

            return IMAGE_URL_PREFIX + storedFilename;

        } catch (IOException e) {
            throw new ImageStorageException("이미지 저장에 실패했습니다", e);
        }
    }

    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        if (DEFAULT_IMAGE_URL.equals(imageUrl)) {
            return;
        }
        if (!imageUrl.startsWith(IMAGE_URL_PREFIX)) {
            throw new ImageStorageException(
                    "올바르지 않은 이미지 경로입니다."
            );
        }
        String storedFilename =
                imageUrl.substring(IMAGE_URL_PREFIX.length());
        Path targetPath = PathConstant.THUMBNAIL_DIRECTORY
                .resolve(storedFilename)
                .normalize();
        if (!targetPath.startsWith(PathConstant.THUMBNAIL_DIRECTORY)) {
            throw new ImageStorageException(
                    "올바르지 않은 이미지 경로입니다."
            );
        }
        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            throw new ImageStorageException(
                    "이미지 삭제에 실패했습니다.",
                    e
            );
        }
    }

    private void validate(MultipartFile image) {

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ImageStorageException("이미지 파일만 업로드할 수 있습니다.");
        }
    }
    private String sanitizeFilename(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return "thumbnail";
        }
        String filename = Paths.get(originalFilename)
                .getFileName()
                .toString();
        return filename
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-zA-Z0-9가-힣._-]", "");
    }

}
