package swm.betterlife.antifragile.domain.diaryanalysis.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisNotFoundException;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;

public interface DiaryAnalysisRepository extends MongoRepository<DiaryAnalysis, String> {

    default DiaryAnalysis getDiaryAnalysis(String memberId, LocalDate diaryDate) {
        return findByMemberIdAndDiaryDate(memberId, diaryDate)
            .orElseThrow(DiaryAnalysisNotFoundException::new);
    }
    Optional<DiaryAnalysis> findByMemberIdAndDiaryDate(String memberId, LocalDate diaryDate);
}