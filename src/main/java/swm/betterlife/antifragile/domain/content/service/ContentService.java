package swm.betterlife.antifragile.domain.content.service;

import static java.util.stream.Collectors.toList;

import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
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
import swm.betterlife.antifragile.common.exception.YouTubeApiException;
import swm.betterlife.antifragile.domain.content.dto.response.ContentListResponse;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.repository.ContentRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.service.MemberService;
import swm.betterlife.antifragile.domain.recommend.dto.response.YouTubeResponse;
import swm.betterlife.antifragile.domain.recommend.service.RecommendService;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentQueryService contentQueryService;
    private final ContentRepository contentRepository;
    private final MongoTemplate mongoTemplate;
    private final MemberService memberService;
    private final DiaryAnalysisService diaryAnalysisService;
    private final RecommendService recommendService;

    @Transactional
    public ContentListResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis =
            diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
        Member member = memberService.getMemberById(memberId);
        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis, member);

        List<Content> savedContents = saveOrUpdateContents(recommendedContents);
        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentListResponse.from(
            savedContents.stream()
                .map(content -> ContentListResponse.ContentResponse.from(
                    content,
                    contentQueryService.getContentLikeNumber(content),
                    contentQueryService.isLiked(memberId, content)
                )).toList()
        );
    }

    @Transactional
    public ContentListResponse saveReRecommendContents(
        String memberId,
        LocalDate date,
        String feedback
    ) {
        validateRecommendLimit(memberId);

        DiaryAnalysis analysis =
            diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
        Member member = memberService.getMemberById(memberId);
        List<String> recommendedUrls = extractRecommendContentUrls(analysis);

        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis, member);
        // TODO: 추후에 feedback을 통해서 재추천 컨텐츠를 가져와야 함

        List<Content> savedContents = saveOrUpdateContents(recommendedContents);

        diaryAnalysisService.saveRecommendContents(analysis, savedContents);

        return ContentListResponse.from(
            savedContents.stream()
                .map(content -> ContentListResponse.ContentResponse.from(
                    content,
                    contentQueryService.getContentLikeNumber(content),
                    contentQueryService.isLiked(memberId, content)
                )).toList()
        );
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

    private List<Content> getRecommendContentsByAnalysis(DiaryAnalysis analysis, Member member) {

        String prompt = recommendService.createPrompt(analysis.getEmotions(), member);

        try {
            YouTubeResponse youTubeResponse = recommendService.youTubeRecommend(prompt);
            return youTubeResponse.toContentList();
        } catch (IOException e) {
            throw new YouTubeApiException();
        }
    }

    private List<Content> getRecommendContentsByAnalysis(
        DiaryAnalysis analysis,
        List<String> recommendedUrls,
        String feedback
    ) {
        // TODO: gpt api와 youtube api를 통해서 재추천 컨텐츠를 가져와야 함
        return null;
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

    private void validateRecommendLimit(String memberId) {
        memberService.decrementRemainRecommendNumber(memberId);
    }

    private List<String> extractRecommendContentUrls(DiaryAnalysis analysis) {
        return analysis.getRecommendContents().stream()
            .map(RecommendContent::getContentUrl)
            .toList();
    }
}
