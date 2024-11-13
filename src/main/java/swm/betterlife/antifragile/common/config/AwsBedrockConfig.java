package swm.betterlife.antifragile.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AwsBedrockConfig {

    @Value("${aws.bedrock.modelId}")
    private String modelId;

    @Value("${aws.bedrock.accessKey}")
    private String accessKey;

    @Value("${aws.bedrock.secretKey}")
    private String secretKey;

    @Value("${aws.bedrock.region}")
    private String region;
}