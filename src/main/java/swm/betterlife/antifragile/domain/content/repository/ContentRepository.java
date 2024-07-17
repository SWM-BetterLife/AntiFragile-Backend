package swm.betterlife.antifragile.domain.content.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.ContentNotFoundException;
import swm.betterlife.antifragile.domain.content.entity.Content;

public interface ContentRepository extends MongoRepository<Content, String> {

    default Content getContent(String id) {
        return findById(id).orElseThrow(ContentNotFoundException::new);
    }

    List<Content> findByUrlIn(List<String> urls);
}