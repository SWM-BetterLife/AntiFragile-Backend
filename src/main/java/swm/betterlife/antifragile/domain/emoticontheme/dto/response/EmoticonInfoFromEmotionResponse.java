package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import lombok.Builder;

@Builder
public record EmoticonInfoFromEmotionResponse(
    String emoticonThemeId,
    String imgUrl
) {
}
