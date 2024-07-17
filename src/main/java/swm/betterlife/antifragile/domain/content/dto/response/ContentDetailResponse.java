package swm.betterlife.antifragile.domain.content.dto.response;

import lombok.Builder;
import swm.betterlife.antifragile.domain.content.entity.Content;
import swm.betterlife.antifragile.domain.content.entity.ContentInfo;

@Builder
public record ContentDetailResponse(
    String url,
    AppStats appStats,
    Boolean isLiked,
    Boolean isSaved
) {
    public static ContentDetailResponse from(
        Content content,
        ContentInfo contentInfo,
        Boolean isLiked,
        Boolean isSaved
    ) {
        return ContentDetailResponse.builder()
            .url(content.getUrl())
            .appStats(AppStats.from(contentInfo))
            .isLiked(isLiked)
            .isSaved(isSaved)
            .build();
    }

    @Builder
    public record AppStats(
        Long likeNumber, Long viewNumber, Long saveNumber
    ) {
        public static AppStats from(ContentInfo contentInfo) {
            return AppStats.builder()
                .likeNumber(contentInfo.getAppLikeNumber())
                .viewNumber(contentInfo.getAppViewNumber())
                .saveNumber(contentInfo.getAppSaveNumber())
                .build();
        }
    }
}
