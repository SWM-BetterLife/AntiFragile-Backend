package swm.betterlife.antifragile.domain.recentcontent.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.betterlife.antifragile.domain.content.entity.Content;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRecord {
    private String id;

    private String title;

    private String thumbnailImgUrl;

    private Long subscriberNumber;

    private Long viewNumber;

    private Long likeNumber;

    private String url;

    private LocalDateTime viewedAt;

    public static ContentRecord of(Content content) {
        return ContentRecord.builder()
            .id(content.getId())
            .title(content.getTitle())
            .thumbnailImgUrl(content.getThumbnailImgUrl())
            .subscriberNumber(content.getYoutubeInfo().getSubscriberNumber())
            .viewNumber(content.getYoutubeInfo().getViewNumber())
            .likeNumber(content.getYoutubeInfo().getLikeNumber())
            .url(content.getUrl())
            .viewedAt(LocalDateTime.now())
            .build();
    }
}
