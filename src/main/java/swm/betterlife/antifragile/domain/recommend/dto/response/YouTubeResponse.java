package swm.betterlife.antifragile.domain.recommend.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.YouTubeInfo;

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

    public List<Content> toContentList() {
        return youTubeApiInfos.stream()
            .map(apiInfo -> Content.builder()
                .title(apiInfo.title())
                .description(apiInfo.description())
                .thumbnailImgUrl(apiInfo.thumbnailImgUrl())
                .youTubeInfo(YouTubeInfo.builder()
                    .subscriberNumber(apiInfo.subscriberNumber())
                    .channelName(apiInfo.channelName())
                    .channelImg(apiInfo.channelImgUrl())
                    .viewNumber(0L)
                    .likeNumber(0L)
                    .build())
                .url(apiInfo.videoUrl())
                .build())
            .collect(Collectors.toList());
    }
}
