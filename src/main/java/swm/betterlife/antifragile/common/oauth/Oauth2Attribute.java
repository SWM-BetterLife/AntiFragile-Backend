package swm.betterlife.antifragile.common.oauth;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

@Slf4j
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Oauth2Attribute {
    private String providerId;
    private String email;
    private LoginType loginType;

    static Oauth2Attribute of(
        String provider, Map<String, Object> attributes, String attributeKeyName
    ) {
        Map<String, Object> providerAttributes = getProviderAttributes(provider, attributes);
        String email = (String) providerAttributes.get("email");
        String providerId = (String) providerAttributes.get(attributeKeyName);

        return Oauth2Attribute.builder()
            .email(email)
            .loginType(getLoginType(provider))
            .providerId(providerId)
            .build();
    }

    private static Map<String, Object> getProviderAttributes(
        String provider, Map<String, Object> attributes
    ) {
        return switch (provider) {
            case "google" -> attributes;
            case "kakao" -> (Map<String, Object>) attributes.get("kakao_account");
            default -> throw new RuntimeException("NotMatchedProviderNameException");
        };
    }

    private static LoginType getLoginType(String provider) {
        return switch (provider) {
            case "google" -> LoginType.GOOGLE;
            case "kakao" -> LoginType.KAKAO;
            default -> throw new RuntimeException("NotMatchedProviderNameException");
        };
    }

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("providerId", providerId);
        map.put("email", email);
        map.put("loginType", loginType);

        return map;
    }
}
