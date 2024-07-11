package swm.betterlife.antifragile.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.content.dto.response.ContentRecommendResponse;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.repository.ContentRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final MongoTemplate mongoTemplate;
    private final ContentRepository contentRepository;

    private final DiaryAnalysisService diaryAnalysisService;

    @Transactional
    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = diaryAnalysisService
            .getDiaryAnalysisByMemberIdAndDate(memberId, date);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);

        List<Content> savedContents = recommendedContents.stream()
            .map(this::saveOrUpdateContent).toList();

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
    }

    private List<Content> getRecommendContentsByAnalysis(DiaryAnalysis analysis) {
        // TODO: gpt api와 youtube api를 통해서 추천 컨텐츠를 가져와야 함
        return Collections.emptyList();
    }

    private Content saveOrUpdateContent(Content recommendedContent) {
        Query query = new Query(Criteria.where("url").is(recommendedContent.getUrl()));
        Update update = new Update()
            .set("title", recommendedContent.getTitle())
            .set("description", recommendedContent.getDescription())
            .set("thumbnailImgUrl", recommendedContent.getThumbnailImgUrl())
            .set("youTubeInfo.subscriberNumber", recommendedContent.getYouTubeInfo().getSubscriberNumber())
            .set("youTubeInfo.channelName", recommendedContent.getYouTubeInfo().getChannelName())
            .set("youTubeInfo.channelImg", recommendedContent.getYouTubeInfo().getChannelImg())
            .set("youTubeInfo.viewNumber", recommendedContent.getYouTubeInfo().getViewNumber())
            .set("youTubeInfo.likeNumber", recommendedContent.getYouTubeInfo().getLikeNumber());

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true).upsert(true),
            Content.class
        );
    }
}
