package swm.betterlife.antifragile.domain.diaryanalysis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;

public interface DiaryAnalysisRepository extends MongoRepository<DiaryAnalysis, String> {
}