package swm.betterlife.antifragile.common.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.exception.S3DeleteFailException;
import swm.betterlife.antifragile.common.exception.S3UploadFailException;
import swm.betterlife.antifragile.common.exception.S3UrlGenerationFailException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageComponent {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucket;

    public String uploadImage(S3ImageCategory category, MultipartFile multipartFile) {
        String fileName = createFileName(
            category,
            Objects.requireNonNull(multipartFile.getOriginalFilename())
        );

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(new PutObjectRequest(
                bucket,
                fileName,
                multipartFile.getInputStream(),
                objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3UploadFailException();
        }

        return fileName;
    }

    private String createFileName(S3ImageCategory category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = UUID.randomUUID().toString().replace("-", "");

        return category + "/" + fileName + "_" + random + fileExtension;
    }

    public void deleteImage(String filename) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, filename));
        } catch (AmazonS3Exception e) {
            throw new S3DeleteFailException();
        }
    }

    public String getUrl(String filename) {
        if (filename != null) {
            return amazonS3.getUrl(bucket, filename).toString();
        }
        return null;
    }

    public String getModelUrl(String fileName) {
        String fullPath = "MODEL/" + fileName;
        try {
            return amazonS3.getUrl(bucket, fullPath).toString();
        } catch (Exception e) {
            throw new S3UrlGenerationFailException();
        }
    }
}
