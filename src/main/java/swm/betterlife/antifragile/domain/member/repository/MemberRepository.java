package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

public interface MemberRepository extends MongoRepository<Member, Long> {

    default Member getMember(String email) {
        return findByEmail(email).orElseThrow(RuntimeException::new);   //todo : Custom Ex
    }

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);
}