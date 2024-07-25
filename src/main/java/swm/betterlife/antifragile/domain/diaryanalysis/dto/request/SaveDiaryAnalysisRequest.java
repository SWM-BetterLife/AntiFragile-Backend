package swm.betterlife.antifragile.domain.diaryanalysis.dto.request;

import java.util.List;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.SelectedEmoticon;

public record SaveDiaryAnalysisRequest(
    List<String> emotions,
    String event,
    String thought,
    String action,
    String comment,
    SelectedEmoticon emoticon
) {
}
