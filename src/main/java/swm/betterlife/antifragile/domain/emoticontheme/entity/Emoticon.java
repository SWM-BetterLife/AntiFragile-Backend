package swm.betterlife.antifragile.domain.emoticontheme.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emoticon {

    private Emotion emotion;

    private String imgUrl;
}
