package swm.betterlife.antifragile.domain.diaryanalysis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectedEmoticon {

    private ObjectId emoticonThemeId;

    private String emotion;
}
