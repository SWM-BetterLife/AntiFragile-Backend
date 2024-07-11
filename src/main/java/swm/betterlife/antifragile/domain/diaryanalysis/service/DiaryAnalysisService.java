package swm.betterlife.antifragile.domain.diaryanalysis.service;


import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.exception.DiaryAnalysisNotFoundException;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.DiaryAnalysis;
import swm.betterlife.antifragile.domain.diaryanalysis.repository.DiaryAnalysisRepository;


@Service
@RequiredArgsConstructor
public class DiaryAnalysisService {

    private final DiaryAnalysisRepository diaryAnalysisRepository;

    public DiaryAnalysis getDiaryAnalysisByMemberIdAndDate(String memberId, LocalDate date) {
        return diaryAnalysisRepository.findByMemberIdAndDiaryDate(memberId, date)
            .orElseThrow(DiaryAnalysisNotFoundException::new);
    }
}
