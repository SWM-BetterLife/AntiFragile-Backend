package swm.betterlife.antifragile.domain.content.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.content.entity.Content;

public interface ContentRepository extends MongoRepository<Content, ObjectId> {
}
