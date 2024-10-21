package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emoticon;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonThemeName;

@Slf4j
@Builder
public record EmoticonThemeOwnDetailResponse(
    String id,
    EmoticonThemeName name,
    List<EmoticonInfoResponse> emoticons
) {
    public static EmoticonThemeOwnDetailResponse from(EmoticonTheme emoticonTheme) {
        return EmoticonThemeOwnDetailResponse.builder()
            .id(emoticonTheme.getId())
            .name(emoticonTheme.getName())
            .emoticons(toDtoList(emoticonTheme.getEmoticons()))
            .build();
    }

    private static List<EmoticonInfoResponse> toDtoList(List<Emoticon> emoticons) {
        return emoticons.stream().map(EmoticonInfoResponse::from).toList();
    }
}
