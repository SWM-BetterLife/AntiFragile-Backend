package swm.betterlife.antifragile.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.ExcessRecommendLimitException;
import swm.betterlife.antifragile.domain.content.dto.response.ContentRecommendResponse;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.service.MemberService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final MongoTemplate mongoTemplate;

    private final MemberService memberService;
    private final DiaryAnalysisService diaryAnalysisService;

    @Transactional
    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);

        List<Content> savedContents = recommendedContents.stream()
            .map(this::saveOrUpdateContent).toList();

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
    }

    @Transactional
    public ContentRecommendResponse saveReRecommendContents(
        String memberId,
        LocalDate date,
        String feedback
    ) {
        validateRecommendLimit(memberId);

        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
        List<String> recommendedUrls = extractRecommendedUrls(analysis);

        List<Content> recommendedContents = getRecommendContentsByAnalysis(
            analysis,
            recommendedUrls,
            feedback
        );

        List<Content> savedContents = recommendedContents.stream()
            .map(this::saveOrUpdateContent).toList();

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
    }

    @Transactional
    public void likeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().addToSet("likeMemberIds", memberId);
        mongoTemplate.updateFirst(query, update, Content.class);
    }

    @Transactional
    public void unlikeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().pull("likeMemberIds", memberId);
        mongoTemplate.updateFirst(query, update, Content.class);
    }

    private DiaryAnalysis getDiaryAnalysis(String memberId, LocalDate date) {
        return diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
    }

    private List<Content> getRecommendContentsByAnalysis(DiaryAnalysis analysis) {
        // TODO: gpt api와 youtube api를 통해서 추천 컨텐츠를 가져와야 함
        return Collections.emptyList();
    }

    private List<Content> getRecommendContentsByAnalysis(
        DiaryAnalysis analysis,
        List<String> recommendedUrls,
        String feedback
    ) {
        // TODO: gpt api와 youtube api를 통해서 재추천 컨텐츠를 가져와야 함
        return Collections.emptyList();
    }

    private List<String> extractRecommendedUrls(DiaryAnalysis analysis) {
        return analysis.getContents().stream()
            .map(RecommendContent::getUrl)
            .toList();
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

    private void validateRecommendLimit(String memberId) {
        Member member = memberService.getMemberById(memberId);
        if (member.getRemainRecommendNumber() <= 0) {
            throw new ExcessRecommendLimitException();
        }
        member.decrementRemainRecommendNumber();
    }
}
