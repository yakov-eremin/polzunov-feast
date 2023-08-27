package com.example.polzunovfeastserver.image;

import com.example.polzunovfeastserver.image.exception.FailedToDeleteImageException;
import com.example.polzunovfeastserver.image.exception.FailedToSaveImageException;
import com.example.polzunovfeastserver.image.exception.ImageUrlNotFoundException;
import com.example.polzunovfeastserver.image.exception.UnsupportedImageTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private static final String IMAGE_DIR_PATH = "/image";

    private final ImageEntityRepository imageRepo;
    private String absoluteStaticDataPath = null;

    private final Map<String, String> contentTypeToExtensions = Map.of(
            "image/png", ".png",
            "image/jpeg", ".jpeg"
    );

    @Value("${static-data.root-dir}")
    private String staticDataPath;

    @Value("${static-data.root-url}")
    private String staticDataUrl;

    /**
     * @throws ImageUrlNotFoundException some image urls were not found
     */
    public Set<ImageEntity> findAllEntitiesByUrls(Set<String> urls) {
        urls = new HashSet<>(urls);
        Set<ImageEntity> imageEntities = imageRepo.findAllByUrl(urls);
        urls.removeAll(imageEntities.stream().map(ImageEntity::getUrl).collect(toSet()));
        if (!urls.isEmpty()) {
            throw new ImageUrlNotFoundException(format("Some image urls were not found: %s", urls));
        }
        return imageEntities;
    }

    /**
     * Deletes images from the file system, NOT from the database.
     *
     * @throws FailedToDeleteImageException cannot delete image
     */
    public void deleteImagesFromFileSystem(Set<ImageEntity> imageEntities) {
        for (ImageEntity imageEntity : imageEntities) {
            Path imagePath = Paths.get(getAbsoluteStaticDataPath() + imageEntity.getPath());
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new FailedToDeleteImageException("Failed to delete image from file system: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Saves image to file system and to the database
     *
     * @return new {@link ImageEntity} with id = null
     * @throws UnsupportedImageTypeException content type of provided file is null or unsupported
     * @throws FailedToSaveImageException    cannot save image
     */
    public ImageEntity addImage(MultipartFile image) {
        String imagePath = IMAGE_DIR_PATH + "/" + createUniqueImageName(image);
        try {
            Files.createDirectories(Paths.get(getAbsoluteStaticDataPath() + IMAGE_DIR_PATH));
            image.transferTo(Paths.get(getAbsoluteStaticDataPath() + imagePath));
        } catch (IOException e) {
            throw new FailedToSaveImageException("Cannot save image: " + e.getMessage(), e);
        }
        return imageRepo.save(new ImageEntity(null, staticDataUrl + imagePath, imagePath));
    }

    private String createUniqueImageName(MultipartFile image) {
        return UUID.randomUUID() + "_" + Instant.now() + getImageExtension(image);
    }

    /**
     * @throws UnsupportedImageTypeException content type of provided file is null or not contains in {@link #contentTypeToExtensions}
     */
    private String getImageExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType == null || !contentTypeToExtensions.containsKey(contentType)) {
            throw new UnsupportedImageTypeException(format("Image type '%s' is not supported", contentType));
        }
        return contentTypeToExtensions.get(contentType);
    }

    private String getAbsoluteStaticDataPath() {
        if (absoluteStaticDataPath == null) {
            absoluteStaticDataPath = System.getProperty("user.dir") + staticDataPath;
        }
        return absoluteStaticDataPath;
    }
}
