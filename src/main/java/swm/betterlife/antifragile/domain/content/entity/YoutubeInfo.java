package swm.betterlife.antifragile.domain.content.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeInfo {

    private Integer subscriberNumber;

    private String channelName;

    private Integer viewNumber; // YouTube 조회수

    private Integer likeNumber; // YouTube 좋아요 수
}
