package swm.betterlife.antifragile.domain.diaryAnalysis.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendContent {

    private ObjectId id;

    private String title;

    private String url;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
