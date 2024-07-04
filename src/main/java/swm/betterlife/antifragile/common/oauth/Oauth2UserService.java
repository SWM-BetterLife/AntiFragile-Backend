package swm.betterlife.antifragile.common.oauth;

import static swm.betterlife.antifragile.domain.member.entity.RoleType.ROLE_USER;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    /* {url}/oauth2/authorization/google 로 접속 */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        Oauth2Attribute oauth2Attribute = Oauth2Attribute.of(
            registrationId, oauth2User.getAttributes(), userNameAttributeName
        );

        boolean isExist = memberRepository.existsByEmailAndLoginType(
            oauth2Attribute.getEmail(),
            oauth2Attribute.getLoginType());

        Map<String, Object> attributeMap = oauth2Attribute.toMap();
        attributeMap.put("isExist", isExist);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(ROLE_USER.name())),
            attributeMap,
            "providerId"
        );
    }
}
