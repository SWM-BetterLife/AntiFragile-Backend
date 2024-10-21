package swm.betterlife.antifragile.domain.diaryanalysis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectedEmoticon {

    private String emoticonThemeId;

    private Emotion emotion;
}
