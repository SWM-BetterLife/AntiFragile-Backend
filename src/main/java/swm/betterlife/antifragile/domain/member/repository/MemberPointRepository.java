package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.MemberPointNotFoundException;
import swm.betterlife.antifragile.domain.member.entity.MemberPoint;

public interface MemberPointRepository extends MongoRepository<MemberPoint, String> {

    default MemberPoint getMemberPoint(String memberId) {
        return findByMemberId(memberId).orElseThrow(MemberPointNotFoundException::new);
    }

    Optional<MemberPoint> findByMemberId(String memberId);

}
