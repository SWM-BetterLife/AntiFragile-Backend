package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;

@Builder
public record ContentDetailResponse(
    String url,
    AppStats appStats,
    Boolean isLiked,
    Boolean isSaved
) {
    public static ContentDetailResponse from(Content content, Boolean isLiked, Boolean isSaved) {
        return ContentDetailResponse.builder()
            .url(content.getUrl())
            .appStats(AppStats.from(content))
            .isLiked(isLiked)
            .isSaved(isSaved)
            .build();
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
