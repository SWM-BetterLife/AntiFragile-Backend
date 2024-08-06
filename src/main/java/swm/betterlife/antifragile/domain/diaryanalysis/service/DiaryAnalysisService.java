package swm.betterlife.antifragile.domain.diaryanalysis.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisAlreadyExistedException;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisNotFoundException;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.DiaryAnalysisModifyRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.DiaryAnalysisSaveRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.response.EmoticonDailyResponse;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.response.EmoticonEntry;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.response.EmoticonMonthlyResponse;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.repository.DiaryAnalysisRepository;
import swm.betterlife.antifragile.domain.emoticontheme.service.EmoticonThemeService;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping("diaries")
public class DiaryAnalysisService {

    private final DiaryAnalysisRepository diaryAnalysisRepository;
    private final MongoTemplate mongoTemplate;
    private final EmoticonThemeService emoticonThemeService;

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
    public void saveRecommendContents(DiaryAnalysis diaryAnalysis, List<Content> savedContents) {
        List<RecommendContent> recommendContents = savedContents.stream()
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
                "recommendContents",
                new Query(Criteria.where("contentId").is(recommendContent.getContentId())));
            mongoTemplate.updateFirst(query, pullUpdate, DiaryAnalysis.class);
        }
    }

    private void saveNewRecommendContents(List<RecommendContent> recommendContents, Query query) {
        Update pushUpdate = new Update().addToSet("recommendContents")
            .each(recommendContents.toArray());
        mongoTemplate.updateFirst(query, pushUpdate, DiaryAnalysis.class);
    }

    @Transactional
    public void saveDiaryAnalysis(
        String memberId, DiaryAnalysisSaveRequest request
    ) {
        LocalDate currentDate = LocalDate.now();

        Query query = new Query(Criteria.where("memberId").is(memberId)
            .and("diaryDate").is(currentDate));

        DiaryAnalysis existingDiaryAnalysis = mongoTemplate.findOne(query, DiaryAnalysis.class);

        if (existingDiaryAnalysis != null) {
            throw new DiaryAnalysisAlreadyExistedException();
        }

        // DiaryAnalysis 객체 생성
        DiaryAnalysis newDiaryAnalysis = DiaryAnalysis.builder()
            .memberId(memberId)
            .diaryDate(currentDate)
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
        String memberId, DiaryAnalysisModifyRequest request, LocalDate date
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

        DiaryAnalysis result = mongoTemplate.findAndModify(query, update, DiaryAnalysis.class);

        if (result == null) {
            throw new DiaryAnalysisNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public EmoticonDailyResponse getDateEmotions(
        String memberId, LocalDate date
    ) {
        Query query = new Query(Criteria.where("memberId").is(memberId)
            .and("diaryDate").is(date));

        DiaryAnalysis diaryAnalysis = mongoTemplate.findOne(query, DiaryAnalysis.class);

        if (diaryAnalysis != null) {
            return EmoticonDailyResponse.from(
                diaryAnalysis.getEmotions(),
                createEmoticonEntry(diaryAnalysis)
            );
        } else {
            throw new DiaryAnalysisNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public EmoticonMonthlyResponse getMonthEmoticons(
        String memberId, YearMonth yearMonth
    ) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Query query = new Query(Criteria.where("memberId").is(memberId)
            .and("diaryDate").gte(startDate).lte(endDate));

        List<DiaryAnalysis> diaryAnalyses = mongoTemplate.find(query, DiaryAnalysis.class);

        if (diaryAnalyses.isEmpty()) {
            throw new DiaryAnalysisNotFoundException();
        }

        List<EmoticonEntry> emoticonEntries = diaryAnalyses.stream()
            .map(this::createEmoticonEntry)
            .collect(Collectors.toList());

        return EmoticonMonthlyResponse.from(emoticonEntries);
    }

    public EmoticonEntry createEmoticonEntry(DiaryAnalysis diaryAnalysis) {
        String imgUrl = emoticonThemeService.getEmoticonImgUrl(diaryAnalysis.getEmoticon());

        return EmoticonEntry.builder()
            .imgUrl(imgUrl)
            .diaryDate(diaryAnalysis.getDiaryDate())
            .build();
    }

}