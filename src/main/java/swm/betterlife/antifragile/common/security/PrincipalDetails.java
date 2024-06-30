package swm.betterlife.antifragile.common.security;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

import java.util.Collection;
import java.util.List;

@Builder
public record PrincipalDetails(
        Long id,
        String email,
        String password,
        LoginType loginType
) implements UserDetails {

    public static PrincipalDetails of(Member member) {
        return new PrincipalDetails(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
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
        return email;
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