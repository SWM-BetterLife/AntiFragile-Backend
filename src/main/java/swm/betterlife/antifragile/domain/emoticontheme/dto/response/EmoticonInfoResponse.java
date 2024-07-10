package swm.betterlife.antifragile.domain.emoticontheme.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emoticon;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;

@Builder
public record EmoticonInfoResponse(
    Emotion emotion,
    String imgUrl
) {
    public static EmoticonInfoResponse from(Emoticon emoticon) {
        return EmoticonInfoResponse.builder()
            .emotion(emoticon.getEmotion())
            .imgUrl(emoticon.getImgUrl())
            .build();
    }
}
