package swm.betterlife.antifragile.domain.content.service;

import lombok.RequiredArgsConstructor;
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

    private final ContentRepository contentRepository;

    private final DiaryAnalysisService diaryAnalysisService;

    @Transactional
    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        DiaryAnalysis analysis = diaryAnalysisService.getDiaryAnalysisByMemberIdAndDate(memberId, date);

        List<Content> recommendedContents = getRecommendContentsByAnalysis(analysis);
        contentRepository.saveAll(recommendedContents);
        diaryAnalysisService.saveRecommendContents(analysis, recommendedContents);

        return ContentRecommendResponse.from(recommendedContents.stream()
            .map(ContentRecommendResponse.ContentResponse::from).toList());
    }

    private List<Content> getRecommendContentsByAnalysis(DiaryAnalysis analysis) {
        // TODO: gpt api와 youtube api를 통해서 추천 컨텐츠를 가져와야 함
        return Collections.emptyList();
    }

}
