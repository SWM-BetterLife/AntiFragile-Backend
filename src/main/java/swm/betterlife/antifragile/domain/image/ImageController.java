package swm.betterlife.antifragile.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.util.S3ImageComponent;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final S3ImageComponent s3ImageComponent;

    @PostMapping
    public ResponseBody<String> uploadImage(@RequestPart MultipartFile image) {
        return ResponseBody.ok(s3ImageComponent.uploadImage("test", image));
    }

    @DeleteMapping
    public ResponseBody<Void> deleteImage(@RequestParam String imageUrl) {
        s3ImageComponent.deleteImage(imageUrl);
        return ResponseBody.ok();
    }
}
