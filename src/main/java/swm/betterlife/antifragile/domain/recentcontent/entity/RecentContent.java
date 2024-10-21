package swm.betterlife.antifragile.domain.recentcontent.entity;

import java.util.ArrayList;
import java.util.List;
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
@Document(collection = "recent_contents")
public class RecentContent extends BaseTimeEntity {

    @Id
    private String id;

    private String memberId;

    private List<ContentRecord> contentRecords = new ArrayList<>();
}
