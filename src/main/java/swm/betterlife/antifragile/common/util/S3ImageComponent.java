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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.exception.S3UploadFailException;

@Component
@RequiredArgsConstructor
public class S3ImageComponent {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucketName}")
    private String bucket;

    public String uploadImage(String category, MultipartFile multipartFile) {
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

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String createFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = UUID.randomUUID().toString().replace("-", "");

        return category + "/" + fileName + "_" + random + fileExtension;
    }

    public void deleteImage(String imageUrl) {
        String[] deleteUrl = imageUrl.split("/", 4);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteUrl[3]));
        } catch (AmazonS3Exception e) {
            throw new S3UploadFailException();
        }
    }
}
