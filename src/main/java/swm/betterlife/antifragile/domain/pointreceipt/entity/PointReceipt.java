package swm.betterlife.antifragile.domain.pointReceipt.entity;

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
@Document(collection = "point_receipts")
public class PointReceipt extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private ObjectId memberId;

    private PointReceiptType type;

    private Integer amount;

    private ObjectId emoticonThemeId;
}
