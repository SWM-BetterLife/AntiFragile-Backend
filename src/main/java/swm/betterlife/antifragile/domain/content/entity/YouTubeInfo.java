package swm.betterlife.antifragile.domain.content.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeInfo {

    private Long subscriberNumber;

    private String channelName;

    private String channelImg;
}
