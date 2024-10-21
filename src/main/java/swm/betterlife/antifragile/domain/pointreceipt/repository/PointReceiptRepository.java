package swm.betterlife.antifragile.domain.pointreceipt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.pointreceipt.entity.PointReceipt;

public interface PointReceiptRepository extends MongoRepository<PointReceipt, String> {

    Page<PointReceipt> findAllByMemberId(String memberId, Pageable pageable);

}
