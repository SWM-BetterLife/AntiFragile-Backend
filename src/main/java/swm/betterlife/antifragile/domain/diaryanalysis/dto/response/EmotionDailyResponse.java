package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record EmotionDailyResponse(
    List<String> emotions,
    EmoticonEntry emoticon
) {
    public static EmotionDailyResponse from(
        List<String> emotions,
        EmoticonEntry emoticon
    ) {
        return EmotionDailyResponse.builder()
            .emotions(emotions)
            .emoticon(emoticon)
            .build();
    }
}