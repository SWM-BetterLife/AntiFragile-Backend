package swm.betterlife.antifragile.common.security;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername.username = {}", username);
        String[] emailAndLoginType = username.split(":");
        LoginType loginType = LoginType.valueOf(emailAndLoginType[0]);
        String email = emailAndLoginType[1];

        Member member = memberRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(MemberNotFoundException::new);
        return PrincipalDetails.of(member);
    }

}
