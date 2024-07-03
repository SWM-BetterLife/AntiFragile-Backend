package swm.betterlife.antifragile.domain.recentcontent.entity;

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
@Document(collection = "recent_content")
public class RecentContent extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private ObjectId memberId;

    private List<ContentRecord> contentRecords = new ArrayList<>();
}
