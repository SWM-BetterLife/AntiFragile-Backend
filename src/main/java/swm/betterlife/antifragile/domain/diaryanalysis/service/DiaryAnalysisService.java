package swm.betterlife.antifragile.domain.diaryanalysis.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisNotFoundException;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.ModifyDiaryAnalysisRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.SaveDiaryAnalysisRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.repository.DiaryAnalysisRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping("diaries")
public class DiaryAnalysisService {

    private final DiaryAnalysisRepository diaryAnalysisRepository;
    private final MongoTemplate mongoTemplate;

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

        removeExistingRecommendContents(recommendContents, query);
        saveNewRecommendContents(recommendContents, query);
    }

    private void removeExistingRecommendContents(
        List<RecommendContent> recommendContents,
        Query query
    ) {
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

    @Transactional
    public void saveDiaryAnalysis(
        String memberId, SaveDiaryAnalysisRequest request
    ) {
        if (request.diaryDate() != null) {
            // 이미 해당 memberId와 diaryDate를 가진 데이터가 있는지 확인
            DiaryAnalysis existingAnalysis = diaryAnalysisRepository.getDiaryAnalysis(
                memberId, request.diaryDate());
            if (existingAnalysis != null) {
                throw new IllegalArgumentException("이미 해당 날짜에 사용자의 일기가 존재합니다");
            }
        }

        // DiaryAnalysis 객체 생성
        DiaryAnalysis newDiaryAnalysis = DiaryAnalysis.builder()
            .memberId(memberId)
            .diaryDate(request.diaryDate())
            .emotions(request.emotions())
            .event(request.event())
            .thought(request.thought())
            .action(request.action())
            .comment(request.comment())
            .emoticon(request.emoticon())
            .build();

        // MongoDB에 저장
        mongoTemplate.save(newDiaryAnalysis);
    }

    @Transactional
    public void modifyDiaryAnalysis(
        String memberId, ModifyDiaryAnalysisRequest request, DateTime date
    ) {
        // 다이어리 분석을 가져오기 위한 쿼리 생성
        Query query = new Query(Criteria.where("memberId").is(memberId)
            .and("diaryDate").is(date));

        // 다이어리 분석을 수정하기 위한 업데이트 생성
        Update update = new Update()
            .set("emotions", request.emotions())
            .set("event", request.event())
            .set("thought", request.thought())
            .set("action", request.action())
            .set("comment", request.comment())
            .set("emoticon", request.emoticon());

        // FindAndModifyOptions 생성
        FindAndModifyOptions options = FindAndModifyOptions.options().upsert(true).returnNew(true);

        // 다이어리 분석을 업데이트하거나 새로 생성
        mongoTemplate.findAndModify(query, update, options, DiaryAnalysis.class);
    }
}