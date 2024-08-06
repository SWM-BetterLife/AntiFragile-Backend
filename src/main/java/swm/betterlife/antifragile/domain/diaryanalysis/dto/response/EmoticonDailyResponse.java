package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record EmoticonDailyResponse(
    List<String> emotions,
    EmoticonEntry emoticon
) {
    public static EmoticonDailyResponse from(
        List<String> emotions,
        EmoticonEntry emoticon
    ) {
        return EmoticonDailyResponse.builder()
            .emotions(emotions)
            .emoticon(emoticon)
            .build();
    }
}