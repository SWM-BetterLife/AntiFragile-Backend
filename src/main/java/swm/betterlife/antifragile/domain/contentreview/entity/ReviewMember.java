package swm.betterlife.antifragile.domain.contentreview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewMember {

    private ObjectId memberId;

    private String nickname;

    private String profileImgUrl;
}
