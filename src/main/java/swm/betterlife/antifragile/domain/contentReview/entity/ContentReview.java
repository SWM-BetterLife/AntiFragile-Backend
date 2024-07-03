package swm.betterlife.antifragile.domain.contentReview.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "content_reviews")
public class ContentReview extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private Number memberId;

    private Number contentId;

    private String review;
}