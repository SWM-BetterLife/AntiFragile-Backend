package swm.betterlife.antifragile.domain.content.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.YoutubeInfo;
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
                .channel(Channel.from(content.getYoutubeInfo()))
                .thumbnailImg(content.getThumbnailImgUrl())
                .videoStats(VideoStats.from(content.getYoutubeInfo()))
                .build();
        }

        public static ContentResponse from(RecommendContent recommendContent) {
            return ContentResponse.builder()
                .id(recommendContent.getId())
                .title(recommendContent.getTitle())
                .description(recommendContent.getDescription())
                .channel(Channel.from(recommendContent.getYoutubeInfo()))
                .thumbnailImg(recommendContent.getThumbnailImgUrl())
                .videoStats(VideoStats.from(recommendContent.getYoutubeInfo()))
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
}