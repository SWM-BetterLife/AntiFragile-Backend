package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;

@Builder
public record EmotionDailyResponse(
    List<String> emotions,
    EmoticonDetails emoticon
) {
    public static EmotionDailyResponse from(
        List<String> emotions,
        EmoticonDetails emoticon
    ) {
        return EmotionDailyResponse.builder()
            .emotions(emotions)
            .emoticon(emoticon)
            .build();
    }

    @Builder
    public record EmoticonDetails(
        String imgUrl,
        String emoticonThemeId,
        Emotion emotion
    ) {}
}