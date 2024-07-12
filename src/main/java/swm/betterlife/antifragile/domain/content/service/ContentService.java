package swm.betterlife.antifragile.domain.content.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.exception.ContentNotFoundException;
import swm.betterlife.antifragile.common.exception.RecommendedContentNotFoundException;
import swm.betterlife.antifragile.domain.content.dto.response.ContentDetailResponse;
import swm.betterlife.antifragile.domain.content.dto.response.ContentRecommendResponse;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.repository.ContentRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;
import swm.betterlife.antifragile.domain.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final MongoTemplate mongoTemplate;

    private final MemberService memberService;

    private final DiaryAnalysisService diaryAnalysisService;

    private final ContentRepository contentRepository;

    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);

        List<Content> savedContents = recommendedContents.stream()
            .map(this::saveOrUpdateContent).toList();

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
    }

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

    public void likeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().addToSet("likeMemberIds", memberId);
        mongoTemplate.updateFirst(query, update, Content.class);
    }

    public void unlikeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().pull("likeMemberIds", memberId);
        mongoTemplate.updateFirst(query, update, Content.class);
    }

    public ContentRecommendResponse getRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
        if (analysis.getContents() == null || analysis.getContents().isEmpty()) {
            throw new RecommendedContentNotFoundException();
        }

        return ContentRecommendResponse.from(
            analysis.getContents().stream()
                .sorted(Comparator.comparing(RecommendContent::getRecommendAt).reversed())
                .limit(5)
                .map(ContentRecommendResponse.ContentResponse::from)
                .toList()
        );
    }

    public ContentDetailResponse getContentDetail(String memberId, String contentId) {
        Content content = getContentById(contentId);
        Boolean isLiked = content.getLikeMemberIds().contains(memberId);
        Boolean isSaved = content.getSaveMembers().stream()
            .anyMatch(saveMember -> saveMember.getMemberId().equals(memberId));

        return ContentDetailResponse.from(content, isLiked, isSaved);
    }


    public Content getContentById(String contentId) {
        return contentRepository.findById(contentId).orElseThrow(ContentNotFoundException::new);
    }

    private DiaryAnalysis getDiaryAnalysis(String memberId, LocalDate date) {
        return diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
    }

    private List<Content> getRecommendContentsByAnalysis(DiaryAnalysis analysis) {
        // TODO: gpt api와 youtube api를 통해서 추천 컨텐츠를 가져와야 함
        return MockDataProvider.getContents1();
    }

    private List<Content> getRecommendContentsByAnalysis(
        DiaryAnalysis analysis,
        List<String> recommendedUrls,
        String feedback
    ) {
        // TODO: gpt api와 youtube api를 통해서 재추천 컨텐츠를 가져와야 함
        return MockDataProvider.getContents2();
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
            .set(
                "youTubeInfo.subscriberNumber",
                recommendedContent.getYoutubeInfo().getSubscriberNumber())
            .set("youTubeInfo.channelName", recommendedContent.getYoutubeInfo().getChannelName())
            .set("youTubeInfo.channelImg", recommendedContent.getYoutubeInfo().getChannelImg())
            .set("youTubeInfo.viewNumber", recommendedContent.getYoutubeInfo().getViewNumber())
            .set("youTubeInfo.likeNumber", recommendedContent.getYoutubeInfo().getLikeNumber());

        return mongoTemplate.findAndModify(
            query,
            update,
            FindAndModifyOptions.options().returnNew(true).upsert(true),
            Content.class
        );
    }

    private void validateRecommendLimit(String memberId) {
        memberService.decrementRemainRecommendNumber(memberId);
    }
}
