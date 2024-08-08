package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.member.entity.MemberPoint;

public interface MemberPointRepository extends MongoRepository<MemberPoint, String> {

    default MemberPoint getMemberPoint(String memberId) {
        return findByMemberId(memberId).orElse(new MemberPoint(memberId, 0));
    }

    Optional<MemberPoint> findByMemberId(String memberId);

}
