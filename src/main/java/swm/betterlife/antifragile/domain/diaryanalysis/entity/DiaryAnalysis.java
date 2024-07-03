package swm.betterlife.antifragile.domain.diaryAnalysis.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "diary_analyses")
public class DiaryAnalysis extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private ObjectId memberId;

    private List<String> emotions;

    private String event;

    private String thought;

    private String action;

    private String comment;

    private List<RecommendContent> contents;

    private List<SelectedEmoticon> emoticons;
}
