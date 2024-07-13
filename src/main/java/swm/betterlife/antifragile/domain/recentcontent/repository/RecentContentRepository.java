package swm.betterlife.antifragile.domain.recentcontent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.recentcontent.entity.ContentRecord;

public interface RecentContentRepository extends MongoRepository<ContentRecord, String> {
}
