package swm.betterlife.antifragile.domain.recentcontent.service;

import com.mongodb.client.result.UpdateResult;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.DatabaseUpdateFailException;
import swm.betterlife.antifragile.common.exception.DatabaseUpsertFailException;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.service.ContentQueryService;
import swm.betterlife.antifragile.domain.recentcontent.entity.ContentRecord;
import swm.betterlife.antifragile.domain.recentcontent.entity.RecentContent;

@Service
@RequiredArgsConstructor
public class RecentContentService {

    private final MongoTemplate mongoTemplate;
    private final ContentQueryService contentQueryService;

    @Transactional
    public void addOrUpdateRecentContent(String memberId, String contentId) {
        Content content = contentQueryService.getContentById(contentId);

        Query updateQuery = new Query(Criteria.where("memberId").is(memberId)
            .and("contentRecords.contentId").is(contentId));
        Update update = new Update().set("contentRecords.$.viewedAt", LocalDateTime.now());
        UpdateResult updateResult = mongoTemplate.updateFirst(
            updateQuery,
            update,
            RecentContent.class
        );

        if (updateResult.getMatchedCount() == 0) {
            addRecentContent(memberId, content);
        } else if (updateResult.getModifiedCount() == 0) {
            throw new DatabaseUpdateFailException();
        }
    }

    private void addRecentContent(String memberId, Content content) {
        Query insertQuery = new Query(Criteria.where("memberId").is(memberId));
        Update insertUpdate = new Update().push(
            "contentRecords", ContentRecord.of(content.getId()));
        UpdateResult insertResult =  mongoTemplate.upsert(
            insertQuery,
            insertUpdate,
            RecentContent.class
        );

        if (insertResult.getMatchedCount() == 0 && insertResult.getUpsertedId() == null) {
            throw new DatabaseUpsertFailException();
        }

    }
}
