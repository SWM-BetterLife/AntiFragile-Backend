package swm.betterlife.antifragile.common.security;

import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

@Slf4j
@Builder
public record PrincipalDetails(
        String email,
        String password,
        ObjectId memberId,
        LoginType loginType
) implements UserDetails {

    public static PrincipalDetails of(
        String email, ObjectId memberId, LoginType loginType
    ) {
        return new PrincipalDetails(email, "", memberId, loginType);
    }

    public static PrincipalDetails of(Member member) {
        return new PrincipalDetails(
                member.getEmail(),
                member.getPassword(),
                member.getId(),
                member.getLoginType()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 역할 관련 협의 필요
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));    //TODO : Enum 처리
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginType + ":" + email; //todo: common Method 분리
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}