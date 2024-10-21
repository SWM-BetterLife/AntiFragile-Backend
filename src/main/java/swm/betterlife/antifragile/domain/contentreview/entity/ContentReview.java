package swm.betterlife.antifragile.domain.contentreview.entity;

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
@Document(collection = "content_reviews")
public class ContentReview extends BaseTimeEntity {

    @Id
    private String id;

    private ReviewMember reviewMember;

    private String contentId;

    private String reviewContent;
}
