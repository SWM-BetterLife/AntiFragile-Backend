package swm.betterlife.antifragile.domain.recentcontent.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentRecord {
    private String contentId;

    private LocalDateTime viewedAt;

    public static ContentRecord of(String contentId) {
        return ContentRecord.builder()
            .contentId(contentId)
            .viewedAt(LocalDateTime.now())
            .build();
    }
}
