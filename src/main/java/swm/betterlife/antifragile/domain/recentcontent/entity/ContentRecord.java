package swm.betterlife.antifragile.domain.recentcontent.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRecord {
    private String id;

    private String title;

    private String thumbnailImgUrl;

    private Integer subscriberNumber;

    private Integer viewNumber;

    private Integer likeNumber;

    private String url;

    private LocalDateTime viewedAt;
}
