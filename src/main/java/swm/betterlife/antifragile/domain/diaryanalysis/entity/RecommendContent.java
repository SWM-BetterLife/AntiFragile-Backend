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

    private String id;

    private String title;

    private String url;

    private LocalDateTime recommendAt;

    public static RecommendContent of(Content content) {
        return RecommendContent.builder()
            .id(content.getId())
            .title(content.getTitle())
            .url(content.getUrl())
            .recommendAt(LocalDateTime.now())
            .build();
    }
}
