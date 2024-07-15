package swm.betterlife.antifragile.domain.content.service;

import com.mongodb.client.result.UpdateResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.ContentAlreadyLikedException;
import swm.betterlife.antifragile.common.exception.ContentNotFoundException;
import swm.betterlife.antifragile.common.exception.ContentNotLikedException;
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

    @Transactional
    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis =
            diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);

        List<Content> savedContents = saveOrUpdateContents(recommendedContents);

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from)
            .toList());
    }

    @Transactional
    public ContentRecommendResponse saveReRecommendContents(
        String memberId,
        LocalDate date,
        String feedback
    ) {
        validateRecommendLimit(memberId);

        DiaryAnalysis analysis =
            diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
        List<String> recommendedUrls = extractRecommendContentUrls(analysis);

        List<Content> recommendedContents = getRecommendContentsByAnalysis(
            analysis,
            recommendedUrls,
            feedback
        );

        List<Content> savedContents = saveOrUpdateContents(recommendedContents);

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentRecommendResponse.from(savedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
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

    private List<Content> saveOrUpdateContents(List<Content> recommendedContents) {
        List<String> urls = recommendedContents.stream().map(Content::getUrl).toList();
        Map<String, Content> existingContents = contentRepository.findByUrlIn(urls).stream()
            .collect(Collectors.toMap(Content::getUrl, Function.identity()));

        List<Content> toSaveContents = new ArrayList<>();
        for (Content content : recommendedContents) {
            Content existingContent = existingContents.get(content.getUrl());
            if (existingContent != null) {
                existingContent.updateContent(content);
                toSaveContents.add(existingContent);
            } else {
                toSaveContents.add(content);
            }
        }

        return contentRepository.saveAll(toSaveContents);
    }

    private List<String> extractRecommendContentUrls(DiaryAnalysis analysis) {
        return analysis.getContents().stream()
            .map(RecommendContent::getContentUrl)
            .toList();
    }

    @Transactional
    public void likeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().addToSet("likeMemberIds", memberId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Content.class);

        if (result.getMatchedCount() == 0) {
            throw new ContentNotFoundException();
        } else if (result.getModifiedCount() == 0) {
            throw new ContentAlreadyLikedException();
        }
    }

    @Transactional
    public void unlikeContent(String memberId, String contentId) {
        Query query = new Query(Criteria.where("id").is(contentId));
        Update update = new Update().pull("likeMemberIds", memberId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Content.class);

        if (result.getMatchedCount() == 0) {
            throw new ContentNotFoundException();
        } else if (result.getModifiedCount() == 0) {
            throw new ContentNotLikedException();
        }
    }

    @Transactional(readOnly = true)
    public ContentRecommendResponse getRecommendContents(String memberId, LocalDate date) {
//        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
//        if (analysis.getRecommendContents() == null || analysis.getRecommendContents().isEmpty()) {
//            throw new RecommendedContentNotFoundException();
//        }
//
//        return ContentRecommendResponse.from(
//            analysis.getRecommendContents().stream()
//                .sorted(Comparator.comparing(RecommendContent::getRecommendAt).reversed())
//                .limit(5)
//                .map(ContentRecommendResponse.ContentResponse::from)
//                .toList()
//        );
        return null;
    }

    @Transactional(readOnly = true)
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


    private void validateRecommendLimit(String memberId) {
        memberService.decrementRemainRecommendNumber(memberId);
    }
}
