package swm.betterlife.antifragile.domain.pointreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.entity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "point_receipts")
public class PointReceipt extends BaseTimeEntity {

    @Id
    private String id;

    private String memberId;

    private PointReceiptType type;

    private Integer amount;

    private String emoticonThemeId;
}
