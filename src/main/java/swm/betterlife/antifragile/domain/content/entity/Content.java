package swm.betterlife.antifragile.domain.content.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "content")
public class Content extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private String title;

    private List<ObjectId> likeUserIds;

    private List<SaveUser> saveUser;

    private String thumbnailImg;

    private Integer subscriberNumber;

    private String channelName;

    private Integer viewNumber;

    private Integer likeNumber;

    private String url;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SaveUser {

        private ObjectId memberId;

        private List<String> tags;
    }
}
