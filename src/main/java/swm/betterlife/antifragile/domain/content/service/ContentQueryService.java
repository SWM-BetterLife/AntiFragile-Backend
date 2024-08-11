package swm.betterlife.antifragile.domain.content.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.common.exception.RecommendedContentNotFoundException;
import swm.betterlife.antifragile.domain.content.dto.response.ContentDetailResponse;
import swm.betterlife.antifragile.domain.content.dto.response.ContentListResponse;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.ContentInfo;
import swm.betterlife.antifragile.domain.content.repository.ContentInfoRepository;
import swm.betterlife.antifragile.domain.content.repository.ContentRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;

@Service
@RequiredArgsConstructor
public class ContentQueryService {

    private final ContentRepository contentRepository;
    private final ContentInfoRepository contentInfoRepository;
    private final DiaryAnalysisService diaryAnalysisService;

    @Transactional(readOnly = true)
    public ContentListResponse getRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = getDiaryAnalysis(memberId, date);

        List<String> recommendContentUrls = analysis.getRecommendContents().stream()
            .sorted(Comparator.comparing(RecommendContent::getRecommendAt).reversed())
            .limit(5)
            .map(RecommendContent::getContentUrl)
            .toList();

        List<Content> recommendContents = contentRepository.findByUrlIn(recommendContentUrls);

        return ContentListResponse.from(
            recommendContents.stream()
                .map(content -> ContentListResponse.ContentResponse.from(
                    content,
                    getContentLikeNumber(content),
                    isLiked(memberId, content)
                ))
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public ContentDetailResponse getContentDetail(String memberId, String contentId) {
        Content content = getContentById(contentId);

        return ContentDetailResponse.from(
            content,
            getContentLikeNumber(content),
            isLiked(memberId, content),
            isSaved(memberId, content)
        );
    }

    private DiaryAnalysis getDiaryAnalysis(String memberId, LocalDate date) {
        DiaryAnalysis analysis =
            diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);
        if (analysis.getRecommendContents() == null || analysis.getRecommendContents().isEmpty()) {
            throw new RecommendedContentNotFoundException();
        }

        return analysis;
    }

    private ContentInfo getContentInfos(String contentId) {
        return contentInfoRepository.getContentInfo(contentId);
    }

    public Boolean isLiked(String memberId, Content content) {
        if (content.getLikeMemberIds() == null) {
            return false;
        }
        return content.getLikeMemberIds().contains(memberId);
    }

    public Boolean isSaved(String memberId, Content content) {
        if (content.getSaveMembers() == null) {
            return false;
        }
        return content.getSaveMembers().stream()
            .anyMatch(saveMember -> saveMember.getMemberId().equals(memberId));
    }

    public Long getContentLikeNumber(Content content) {
        if (content.getLikeMemberIds() == null) {
            return 0L;
        }
        return (long) content.getLikeMemberIds().size();
    }

    public Content getContentById(String contentId) {
        return contentRepository.getContent(contentId);
    }
}
