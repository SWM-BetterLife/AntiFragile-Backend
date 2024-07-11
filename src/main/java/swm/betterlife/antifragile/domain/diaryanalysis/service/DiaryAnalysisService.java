package swm.betterlife.antifragile.domain.diaryanalysis.service;


import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisNotFoundException;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.repository.DiaryAnalysisRepository;


@Service
@RequiredArgsConstructor
public class DiaryAnalysisService {

    private final MongoTemplate mongoTemplate;
    private final DiaryAnalysisRepository diaryAnalysisRepository;

    public DiaryAnalysis getDiaryAnalysisByMemberIdAndDate(String memberId, LocalDate date) {
        return diaryAnalysisRepository.findByMemberIdAndDiaryDate(memberId, date)
            .orElseThrow(DiaryAnalysisNotFoundException::new);
    }

    public void saveRecommendContents(DiaryAnalysis diaryAnalysis, List<Content> contents) {
        List<RecommendContent> recommendContents = contents.stream()
            .map(RecommendContent::of).toList();

        Query query = new Query(Criteria.where("id").is(diaryAnalysis.getId()));
        Update update = new Update().push("contents").each(recommendContents.toArray());

        mongoTemplate.updateFirst(query, update, DiaryAnalysis.class);
    }
}
