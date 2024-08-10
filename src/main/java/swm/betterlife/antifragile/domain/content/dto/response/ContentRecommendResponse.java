package swm.betterlife.antifragile.domain.content.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;

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
            ChannelResponse channel,
            String thumbnailImg,
            Long likeNumber,
            Boolean isLiked
    ) {
        public static ContentResponse from(
            Content content,
            Long likeNumber,
            Boolean isLiked
        ) {
            return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .channel(ChannelResponse.from(content.getYouTubeInfo()))
                .thumbnailImg(content.getThumbnailImgUrl())
                .likeNumber(likeNumber)
                .isLiked(isLiked)
                .build();
        }
    }
}