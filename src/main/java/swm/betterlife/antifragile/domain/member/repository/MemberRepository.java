package swm.betterlife.antifragile.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getMember(String email) {
        return findByEmail(email).orElseThrow(RuntimeException::new);   //todo : Custom Ex
    }

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndLoginType(String email, LoginType loginType);
}
