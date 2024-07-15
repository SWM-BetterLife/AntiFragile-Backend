package swm.betterlife.antifragile.domain.recentcontent.entity;

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
public class ContentRecord {
    private String contentId;

    private LocalDateTime viewedAt;

    public static ContentRecord of(Content content) {
        return ContentRecord.builder()
            .contentId(content.getId())
            .viewedAt(LocalDateTime.now())
            .build();
    }
}
