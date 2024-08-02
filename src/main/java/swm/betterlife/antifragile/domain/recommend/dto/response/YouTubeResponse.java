package swm.betterlife.antifragile.domain.recommend.dto.response;

import java.util.List;

public record YouTubeResponse(
    List<YouTubeApiInfo> youTubeApiInfos
) {
    public record YouTubeApiInfo(
        String title,
        String description,
        String thumbnailImgUrl,
        Long subscriberNumber,
        String channelName,
        String channelImgUrl,
        String videoUrl
    ) {}
}
