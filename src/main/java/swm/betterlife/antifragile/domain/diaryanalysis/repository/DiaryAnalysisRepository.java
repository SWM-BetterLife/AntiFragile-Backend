package swm.betterlife.antifragile.domain.diaryanalysis.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;

public interface DiaryAnalysisRepository extends MongoRepository<DiaryAnalysis, String> {
    Optional<DiaryAnalysis> findByMemberIdAndDiaryDate(String memberId, LocalDate diaryDate);
}
