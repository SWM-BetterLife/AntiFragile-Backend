package swm.betterlife.antifragile.domain.member.entity;

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
@Document(collection = "members")
public class Member extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private String loginType;

    private String role;
    private String email;
    private String nickname;

    private String profileImg;

    private List<ObjectId> emoticonThemeId;

    private Integer point;

    private Integer remainRecommendsCnt;

    private Date deletedAt;
}