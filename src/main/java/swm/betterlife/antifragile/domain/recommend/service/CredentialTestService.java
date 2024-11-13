package swm.betterlife.antifragile.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityRequest;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;
import software.amazon.awssdk.services.sts.model.StsException;
import swm.betterlife.antifragile.common.config.AwsBedrockConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class CredentialTestService {

    private final AwsBedrockConfig awsBedrockConfig;

    public boolean testCredentials() {
        // AWS 자격 증명 설정
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
            awsBedrockConfig.getAccessKey(),
            awsBedrockConfig.getSecretKey()
        );

        // STS 클라이언트 생성
        try (StsClient stsClient = StsClient.builder()
            .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
            .region(Region.of(awsBedrockConfig.getRegion()))
            .build()) {

            // GetCallerIdentity 요청 보내기
            GetCallerIdentityResponse response = stsClient.getCallerIdentity(GetCallerIdentityRequest.builder().build());
            log.info("Credentials are valid. Account ID: {}", response.account());
            return true;

        } catch (StsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return false;
        }
    }
}
