package swm.betterlife.antifragile.domain.content.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "content")
public class Content extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private String title;

    private List<ObjectId> likeMemberIds = new ArrayList<>();

    private List<SaveMember> saveMembers = new ArrayList<>();

    private String thumbnailImgUrl;

    private Integer subscriberNumber;

    private String channelName;

    private Integer viewNumber;

    private Integer likeNumber;

    private String url;
}
