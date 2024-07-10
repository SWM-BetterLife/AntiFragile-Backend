package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emoticon;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;

@Slf4j
@Builder
public record EmoticonThemeOwnDetailResponse(
    ObjectId id,
    String name,
    List<EmoticonInfoResponse> emoticons
) {
    public static EmoticonThemeOwnDetailResponse from(EmoticonTheme emoticonTheme) {
        log.info("from _id : {}", emoticonTheme.getId());
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
