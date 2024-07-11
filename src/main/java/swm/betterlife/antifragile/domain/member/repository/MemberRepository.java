package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

public interface MemberRepository extends MongoRepository<Member, String> {

    default Member getMember(String id) {
        return findById(id).orElseThrow(MemberNotFoundException::new);
    }

    default Member getMember(String email, LoginType loginType) {
        return findByEmailAndLoginType(email, loginType)
            .orElseThrow(MemberNotFoundException::new);   //todo : Custom Ex
    }

    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);
}
