package swm.betterlife.antifragile.domain.member.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.basetimeentity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "members")
public class Member extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private LoginType loginType;

    private Role role;

    private String email;

    private String nickname;

    private String profileImgUrl;

    private List<ObjectId> emoticonThemeId = new ArrayList<>();

    private Integer point;

    private Integer remainRecommendNumber;
}
