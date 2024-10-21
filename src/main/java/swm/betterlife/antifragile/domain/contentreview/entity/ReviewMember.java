package swm.betterlife.antifragile.domain.contentreview.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewMember {

    private String memberId;

    private String nickname;

    private String profileImgFilename;
}
