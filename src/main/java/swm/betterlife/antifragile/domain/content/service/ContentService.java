package swm.betterlife.antifragile.domain.content.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.ContentNotFoundException;
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
        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);

        List<Content> savedContents = contentRepository.saveAll(
            recommendedContents.stream().map(this::saveOrUpdateContent).toList());

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

        List<Content> savedContents = contentRepository.saveAll(
            recommendedContents.stream().map(this::saveOrUpdateContent).toList());

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

    public Content saveOrUpdateContent(Content recommendedContent) {
        Optional<Content> existingContentOpt = contentRepository.findByUrl(
            recommendedContent.getUrl());

        if (existingContentOpt.isPresent()) {
            return existingContentOpt.get().updateContent(recommendedContent);
        }

        return recommendedContent;
    }

    private void validateRecommendLimit(String memberId) {
        memberService.decrementRemainRecommendNumber(memberId);
    }
}
