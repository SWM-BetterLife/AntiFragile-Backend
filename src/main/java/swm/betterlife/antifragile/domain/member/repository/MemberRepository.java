package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.MemberNotFoundException;
import swm.betterlife.antifragile.domain.member.entity.LoginType;
import swm.betterlife.antifragile.domain.member.entity.Member;

public interface MemberRepository extends MongoRepository<Member, ObjectId> {

    default Member getMember(String email) {
        return findByEmail(email).orElseThrow(MemberNotFoundException::new);   //todo : Custom Ex
    }

    default Member getMember(String email, LoginType loginType) {
        return findByEmailAndLoginType(email, loginType)
            .orElseThrow(MemberNotFoundException::new);   //todo : Custom Ex
    }

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);

    boolean existsByEmail(String email);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);
}
