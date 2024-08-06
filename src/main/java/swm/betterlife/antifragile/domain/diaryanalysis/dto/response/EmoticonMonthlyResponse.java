package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record EmoticonMonthlyResponse(
    List<EmoticonEntry> emoticons
) {
    public static EmoticonMonthlyResponse from(List<EmoticonEntry> emoticons) {
        return EmoticonMonthlyResponse.builder()
            .emoticons(emoticons)
            .build();
    }
}