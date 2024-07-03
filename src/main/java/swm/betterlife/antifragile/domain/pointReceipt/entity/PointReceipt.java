package swm.betterlife.antifragile.domain.pointReceipt.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "point_receipts")
public class PointReceipt extends BaseTimeEntity {

    @Id
    private ObjectId id;

    @Field("user_id")
    private ObjectId userId;

    private String type;

    private Number amount;

    @Field("emoticon_theme_id")
    private ObjectId emoticonThemeId;
}