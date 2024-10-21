package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;

@Builder
public record ContentDetailResponse(
    String id,
    String title,
    String description,
    ChannelResponse channel,
    String url,
    Long likeNumber,
    Boolean isLiked,
    Boolean isSaved
) {
    public static ContentDetailResponse from(
        Content content,
        Long likeNumber,
        Boolean isLiked,
        Boolean isSaved
    ) {
        return ContentDetailResponse.builder()
            .id(content.getId())
            .title(content.getTitle())
            .description(content.getDescription())
            .channel(ChannelResponse.from(content.getYouTubeInfo()))
            .url(content.getUrl())
            .likeNumber(likeNumber)
            .isLiked(isLiked)
            .isSaved(isSaved)
            .build();
    }
}
