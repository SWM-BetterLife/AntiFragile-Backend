package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record EmotionDailyResponse(
    List<String> emotions
) {
    public static EmotionDailyResponse from(List<String> emotions) {
        return EmotionDailyResponse.builder()
            .emotions(emotions)
            .build();
    }
}