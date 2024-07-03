package swm.betterlife.antifragile.domain.recentContent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "recent_content")
public class RecentContent extends BaseTimeEntity {

    @Id
    private ObjectId id;

    @Field("member_id")
    private ObjectId memberId;

    private List<RecentItem> recent;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentItem {

        @Id
        private ObjectId id;

        private String title;

        private String thumbnailImg;

        private Integer subscriberNumber;

        private Integer viewNumber;

        private Integer likeNumber;

        private String url;

        private Date viewedAt;
    }
}