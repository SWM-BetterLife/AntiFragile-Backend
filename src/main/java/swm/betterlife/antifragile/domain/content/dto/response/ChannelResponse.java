package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.YouTubeInfo;

@Builder
public record ChannelResponse(
    String name,
    String img,
    Long subscribeNumber
) {
    public static ChannelResponse from(YouTubeInfo youtubeInfo) {
        return ChannelResponse.builder()
            .name(youtubeInfo.getChannelName())
            .img(youtubeInfo.getChannelImg())
            .subscribeNumber(youtubeInfo.getSubscriberNumber())
            .build();
    }
}