package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;

@Builder
public record EmoticonThemeSummaryResponse(
    String id,
    String name,
    Integer price
) {
    public static EmoticonThemeSummaryResponse from(EmoticonTheme emoticonTheme) {
        return EmoticonThemeSummaryResponse.builder()
            .id(emoticonTheme.getId().toString())
            .name(emoticonTheme.getName())
            .price(emoticonTheme.getPrice())
            .build();
    }
}