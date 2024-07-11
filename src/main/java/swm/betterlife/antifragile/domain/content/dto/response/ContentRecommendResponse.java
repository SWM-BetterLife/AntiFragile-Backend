package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ContentRecommendResponse(
        List<Content> contents
) {
    @Builder
    public record Content(
            String id,
            String title,
            String description,
            Channel channel,
            String thumbnailImg,
            String youtubeLink,
            VideoStats videoStats,
            AppStats appStats
    ) {}

    @Builder
    public record Channel(
            String name,
            String img,
            String subscribeNumber
    ) {}

    @Builder
    public record VideoStats(
            String viewNumber, String likeNumber
    ) {}

    @Builder
    public record AppStats(
        String viewNumber, String likeNumber
    ) {}
}