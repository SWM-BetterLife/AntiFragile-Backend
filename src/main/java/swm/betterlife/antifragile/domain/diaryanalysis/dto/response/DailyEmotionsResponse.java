package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record DailyEmotionsResponse(
    List<String> emotions
) {
    public static DailyEmotionsResponse from(List<String> emotions) {
        return DailyEmotionsResponse.builder()
            .emotions(emotions)
            .build();
    }
}