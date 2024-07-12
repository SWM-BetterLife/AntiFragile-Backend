package swm.betterlife.antifragile.domain.content.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.YouTubeInfo;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.RecommendContent;

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
            VideoStats videoStats
    ) {
        public static ContentResponse from(Content content) {
            return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .channel(Channel.from(content.getYouTubeInfo()))
                .thumbnailImg(content.getThumbnailImgUrl())
                .videoStats(VideoStats.from(content.getYouTubeInfo()))
                .build();
        }

        public static ContentResponse from(RecommendContent recommendContent) {
            return ContentResponse.builder()
                .id(recommendContent.getId())
                .title(recommendContent.getTitle())
                .description(recommendContent.getDescription())
                .channel(Channel.from(recommendContent.getYouTubeInfo()))
                .thumbnailImg(recommendContent.getThumbnailImgUrl())
                .videoStats(VideoStats.from(recommendContent.getYouTubeInfo()))
                .build();
        }
    }

    @Builder
    public record Channel(
            String name,
            String img,
            Long subscribeNumber
    ) {
        public static Channel from(YouTubeInfo youtubeInfo) {
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
        public static VideoStats from(YouTubeInfo youtubeInfo) {
            return VideoStats.builder()
                .viewNumber(youtubeInfo.getViewNumber())
                .likeNumber(youtubeInfo.getLikeNumber())
                .build();
        }
    }
}