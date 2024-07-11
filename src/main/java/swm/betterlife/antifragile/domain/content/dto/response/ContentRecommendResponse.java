package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.YoutubeInfo;

import java.util.List;

@Builder
public record ContentRecommendResponse(
        List<ContentResponse> contents
) {
    public static ContentRecommendResponse from(List<ContentResponse> contents) {
        return ContentRecommendResponse.builder()
                .contents(contents)
                .build();
    }

    @Builder
    public record ContentResponse(
            String id,
            String title,
            String description,
            Channel channel,
            String thumbnailImg,
            String youtubeLink,
            VideoStats videoStats,
            AppStats appStats
    ) {
        public static ContentResponse from(Content content) {
            return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .channel(Channel.from(content.getYouTubeInfo()))
                .thumbnailImg(content.getThumbnailImgUrl())
                .youtubeLink(content.getUrl())
                .videoStats(VideoStats.from(content.getYouTubeInfo()))
                .appStats(AppStats.from(content))
                .build();
        }
    }

    @Builder
    public record Channel(
            String name,
            String img,
            Long subscribeNumber
    ) {
        public static Channel from(YoutubeInfo youtubeInfo) {
            return Channel.builder()
                .name(youtubeInfo.getChannelName())
                .img(youtubeInfo.getChannelImg())
                .subscribeNumber(youtubeInfo.getSubscriberNumber())
                .build();
        }
    }

    @Builder
    public record VideoStats(
            Long viewNumber, Long likeNumber
    ) {
        public static VideoStats from(YoutubeInfo youtubeInfo) {
            return VideoStats.builder()
                .viewNumber(youtubeInfo.getViewNumber())
                .likeNumber(youtubeInfo.getLikeNumber())
                .build();
        }
    }

    @Builder
    public record AppStats(
        Long viewNumber, Long likeNumber
    ) {
        public static AppStats from(Content content) {
            return AppStats.builder()
                .viewNumber(content.getAppViewNumber())
                .likeNumber(content.getAppLikeNumber())
                .build();
        }
    }
}