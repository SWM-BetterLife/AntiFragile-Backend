package swm.betterlife.antifragile.domain.diaryanalysis.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.betterlife.antifragile.domain.content.entity.Content;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendContent {

    private String contentId;

    private LocalDateTime recommendAt;

    public static RecommendContent of(Content content) {
        return RecommendContent.builder()
            .contentId(content.getId())
            .recommendAt(LocalDateTime.now())
            .build();
    }
}
