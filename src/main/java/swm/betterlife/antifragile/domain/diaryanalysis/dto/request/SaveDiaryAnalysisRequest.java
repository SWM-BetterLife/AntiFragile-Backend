package swm.betterlife.antifragile.domain.diaryanalysis.dto.request;

import java.time.LocalDate;
import java.util.List;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.SelectedEmoticon;

public record SaveDiaryAnalysisRequest(
    List<String> emotions,
    String event,
    String thought,
    String action,
    String comment,
    LocalDate diaryDate,
    SelectedEmoticon emoticon
) {
}
