package swm.betterlife.antifragile.domain.recentcontent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.service.ContentService;
import swm.betterlife.antifragile.domain.recentcontent.entity.ContentRecord;
import swm.betterlife.antifragile.domain.recentcontent.entity.RecentContent;

@Service
@RequiredArgsConstructor
public class RecentContentService {

    private final MongoTemplate mongoTemplate;

    private final ContentService contentService;

    @Transactional
    public void addRecentContent(String memberId, String contentId) {
        Content content = contentService.getContentById(contentId);

        Query query = new Query(Criteria.where("memberId").is(memberId));
        Update update = new Update().push("contentRecords", ContentRecord.of(content));
        FindAndModifyOptions options = FindAndModifyOptions.options().upsert(true).returnNew(true);

        mongoTemplate.findAndModify(query, update, options, RecentContent.class);
    }
}
