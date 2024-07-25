package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record MonthEmoticonsResponse(
    List<EmoticonEntry> emoticons
) {
    public static MonthEmoticonsResponse from(List<EmoticonEntry> emoticons) {
        return MonthEmoticonsResponse.builder()
            .emoticons(emoticons)
            .build();
    }

    @Builder
    public record EmoticonEntry(
        String imgUrl,
        LocalDate diaryDate
    ) {}
}