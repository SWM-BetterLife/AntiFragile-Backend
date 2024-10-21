package swm.betterlife.antifragile.domain.recentcontent.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.recentcontent.entity.RecentContent;

public interface RecentContentRepository extends MongoRepository<RecentContent, String> {
}
