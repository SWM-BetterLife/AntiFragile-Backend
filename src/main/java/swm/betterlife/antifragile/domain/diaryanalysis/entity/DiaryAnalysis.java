package swm.betterlife.antifragile.domain.diaryanalysis.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
@Document(collection = "diary_analyses")
public class DiaryAnalysis extends BaseTimeEntity {

    @Id
    private String id;

    private String memberId;

    private List<String> emotions = new ArrayList<>();

    private String event;

    private String thought;

    private String action;

    private String comment;

    private LocalDate diaryDate;

    private List<RecommendContent> recommendContents = new ArrayList<>();

    private SelectedEmoticon emoticon;
}
