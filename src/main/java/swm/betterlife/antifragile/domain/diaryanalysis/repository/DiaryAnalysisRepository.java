package swm.betterlife.antifragile.domain.diaryanalysis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryAnalysisRepository extends MongoRepository<DiaryAnalysis, String> {
    Optional<DiaryAnalysis> findByMemberIdAndDiaryDate(String memberId, LocalDate diaryDate);
}
