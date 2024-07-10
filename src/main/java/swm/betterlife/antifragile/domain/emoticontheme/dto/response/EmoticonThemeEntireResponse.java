package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import java.util.List;

public record EmoticonThemeEntireResponse(
    List<EmoticonThemeSummaryResponse> emoticonThemes
) {
}
