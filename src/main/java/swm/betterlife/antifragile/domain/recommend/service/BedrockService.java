package swm.betterlife.antifragile.domain.recommend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.config.AwsBedrockConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class BedrockService {
    private final AwsBedrockConfig awsBedrockConfig;
    private static final String SERVICE = "bedrock";
    private static final String API_ENDPOINT = "https://bedrock-runtime.us-east-1.amazonaws.com";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> callBedrockApi(String emotion, String diarySummary) throws Exception {
        String payload = String.format("{ \"input\": { \"emotion\": \"%s\", \"diary_summary\": \"%s\" } }", emotion, diarySummary);
        String canonicalUri = "/model/" + awsBedrockConfig.getModelId() + "/invoke";
        String httpMethod = "POST";

        Instant now = Instant.now();
        String amzDate = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
            .withZone(ZoneOffset.UTC)
            .format(now);
        String dateStamp = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withZone(ZoneOffset.UTC)
            .format(now);

        String payloadHash = hash(payload);
        String canonicalRequest = createCanonicalRequest(httpMethod, canonicalUri, payloadHash, amzDate);
        log.debug("Canonical Request:\n{}", canonicalRequest);

        String stringToSign = createStringToSign(canonicalRequest, amzDate, dateStamp);
        log.debug("String to Sign:\n{}", stringToSign);

        byte[] signingKey = getSignatureKey(awsBedrockConfig.getSecretKey(), dateStamp, awsBedrockConfig.getRegion(), SERVICE);
        String signature = bytesToHex(hmacSha256(signingKey, stringToSign));
        log.debug("Calculated Signature: {}", signature);

        String authorizationHeader = "AWS4-HMAC-SHA256 Credential=" + awsBedrockConfig.getAccessKey() + "/" + dateStamp + "/" + awsBedrockConfig.getRegion() + "/" + SERVICE + "/aws4_request, SignedHeaders=host;x-amz-date, Signature=" + signature;
        log.debug("Authorization Header: {}", authorizationHeader);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_ENDPOINT + canonicalUri))
            .header("Authorization", authorizationHeader)
            .header("x-amz-date", amzDate)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Received response: {}", response.body());

        List<String> recommendedVideos = parseResponse(response.body());
        log.info("Received recommended video IDs: {}", recommendedVideos);

        return recommendedVideos;
    }

    private List<String> parseResponse(String responseBody) throws Exception {
        List<String> recommendedVideos = new ArrayList<>();
        JsonNode root = objectMapper.readTree(responseBody);

        if (root.has("recommended_videos")) {
            for (JsonNode videoIdNode : root.get("recommended_videos")) {
                recommendedVideos.add(videoIdNode.asText());
            }
        }

        return recommendedVideos;
    }

    private String createCanonicalRequest(String httpMethod, String canonicalUri, String payloadHash, String amzDate) {
        String canonicalRequest = httpMethod + "\n" +
            canonicalUri + "\n" +
            "\n" +
            "host:bedrock-runtime.us-east-1.amazonaws.com\n" +
            "x-amz-date:" + amzDate + "\n\n" +
            "host;x-amz-date\n" +
            payloadHash;
        log.debug("Canonical Request: {}", canonicalRequest);
        return canonicalRequest;
    }

    private String createStringToSign(String canonicalRequest, String amzDate, String dateStamp) {
        String stringToSign = "AWS4-HMAC-SHA256\n" +
            amzDate + "\n" +
            dateStamp + "/" + awsBedrockConfig.getRegion() + "/" + SERVICE + "/aws4_request\n" +
            hash(canonicalRequest);
        log.debug("String to Sign: {}", stringToSign);
        return stringToSign;
    }

    private byte[] getSignatureKey(String secretKey, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kDate = hmacSha256(("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8), dateStamp);
        byte[] kRegion = hmacSha256(kDate, regionName);
        byte[] kService = hmacSha256(kRegion, serviceName);
        byte[] signingKey = hmacSha256(kService, "aws4_request");
        log.debug("Signing Key: {}", bytesToHex(signingKey));
        return signingKey;
    }

    private byte[] hmacSha256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private String hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
