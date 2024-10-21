package swm.betterlife.antifragile.domain.content.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.ContentInfoNotFoundException;
import swm.betterlife.antifragile.domain.content.entity.ContentInfo;

public interface ContentInfoRepository extends MongoRepository<ContentInfo, String> {

    default ContentInfo getContentInfo(String contentId) {
        return findByContentId(contentId).orElseThrow(ContentInfoNotFoundException::new);
    }

    Optional<ContentInfo> findByContentId(String contentId);
}
