package swm.betterlife.antifragile.domain.contentreview.entity;

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
@Document(collection = "content_reviews")
public class ContentReview extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private ReviewMember reviewMember;

    private ObjectId contentId;

    private String reviewContent;
}
