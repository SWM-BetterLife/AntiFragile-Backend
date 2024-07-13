package swm.betterlife.antifragile.domain.content.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.content.entity.Content;

public interface ContentRepository extends MongoRepository<Content, String> {
    Optional<Content> findByUrl(String url);

}
