package swm.betterlife.antifragile.domain.member.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.member.entity.MemberDiary;

public interface MemberDiaryRepository extends MongoRepository<MemberDiary, String> {

    default MemberDiary getMemberPoint(String memberId) {
        return findByMemberId(memberId).orElse(new MemberDiary(memberId, 0));
    }

    Optional<MemberDiary> findByMemberId(String memberId);

}
