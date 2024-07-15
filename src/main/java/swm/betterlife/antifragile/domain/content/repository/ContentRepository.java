package swm.betterlife.antifragile.domain.content.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.content.entity.Content;

public interface ContentRepository extends MongoRepository<Content, String> {
    List<Content> findByUrlIn(List<String> urls);

    Optional<Content> findByUrl(String url);
}
