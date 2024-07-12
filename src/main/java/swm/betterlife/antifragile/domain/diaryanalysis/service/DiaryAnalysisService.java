package swm.betterlife.antifragile.domain.diaryanalysis.service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @PostConstruct
    public void init() {
        createInitialDiaryAnalysis();
    }

    @Transactional(readOnly = true)
    public DiaryAnalysis getDiaryAnalysisByMemberIdAndDate(String memberId, LocalDate date) {
        Query query = new Query();
        query.addCriteria(Criteria.where("memberId").is(memberId)
            .and("diaryDate").is(date));

        DiaryAnalysis diaryAnalysis = mongoTemplate.findOne(query, DiaryAnalysis.class);
        if (diaryAnalysis == null) {
            throw new DiaryAnalysisNotFoundException();
        }

        return diaryAnalysis;
    }

    @Transactional
    public void saveRecommendContents(DiaryAnalysis diaryAnalysis, List<Content> contents) {
        List<RecommendContent> recommendContents = contents.stream()
            .map(RecommendContent::of).toList();

        Query query = new Query(Criteria.where("id").is(diaryAnalysis.getId()));

        removeExistingRecommandContents(recommendContents, query);
        saveNewRecommendContents(recommendContents, query);
    }

    private void removeExistingRecommandContents(List<RecommendContent> recommendContents, Query query) {
        for (RecommendContent recommendContent : recommendContents) {
            Update pullUpdate = new Update().pull(
                "contents",
                new Query(Criteria.where("url").is(recommendContent.getUrl())));
            mongoTemplate.updateFirst(query, pullUpdate, DiaryAnalysis.class);
        }
    }

    private void saveNewRecommendContents(List<RecommendContent> recommendContents, Query query) {
        Update pushUpdate = new Update().addToSet("contents").each(recommendContents.toArray());
        mongoTemplate.updateFirst(query, pushUpdate, DiaryAnalysis.class);
    }

    // 테스트용
    private void createInitialDiaryAnalysis() {
        String memberId = "66914f7b5df42e5ad7c5915a"; // 테스트를 위한 멤버 id
        LocalDate diaryDate = LocalDate.now();

        boolean exists = diaryAnalysisRepository
            .findByMemberIdAndDiaryDate(memberId, diaryDate).isPresent();
        if (!exists) {
            DiaryAnalysis diaryAnalysis = DiaryAnalysis.builder()
                .id(new ObjectId().toString())
                .memberId(memberId)
                .emotions(Arrays.asList("happy", "sad"))
                .event("Sample event")
                .thought("Sample thought")
                .action("Sample action")
                .comment("Sample comment")
                .diaryDate(diaryDate)
                .build();

            diaryAnalysisRepository.save(diaryAnalysis);
        }
    }
}
