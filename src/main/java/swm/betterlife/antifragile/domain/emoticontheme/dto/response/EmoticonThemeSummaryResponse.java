package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonThemeName;

@Builder
public record EmoticonThemeSummaryResponse(
    String id,
    EmoticonThemeName name,
    Integer price
) {
    public static EmoticonThemeSummaryResponse from(EmoticonTheme emoticonTheme) {
        return EmoticonThemeSummaryResponse.builder()
            .id(emoticonTheme.getId())
            .name(emoticonTheme.getName())
            .price(emoticonTheme.getPrice())
            .build();
    }
}
