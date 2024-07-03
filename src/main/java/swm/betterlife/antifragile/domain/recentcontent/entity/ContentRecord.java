package swm.betterlife.antifragile.domain.recentContent.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRecord {
    private ObjectId id;

    private String title;

    private String thumbnailImgUrl;

    private Integer subscriberNumber;

    private Integer viewNumber;

    private Integer likeNumber;

    private String url;

    private LocalDateTime viewedAt;
}
