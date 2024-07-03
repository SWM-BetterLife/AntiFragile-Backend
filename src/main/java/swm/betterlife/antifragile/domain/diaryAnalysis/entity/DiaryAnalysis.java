package swm.betterlife.antifragile.domain.diaryAnalysis.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "diary_analysis")
public class DiaryAnalysis extends BaseTimeEntity {

    @Id
    private ObjectId id;

    @Field("member_id")
    private Long memberId;

    private List<String> emotions;

    private String event;
    private String thought;
    private String action;
    private String comment;

    private List<Content> contents;

    private List<Emoticon> emoticon;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Content {

        @Id
        private ObjectId id;

        private String title;

        private String url;

        private Date createdAt;

        private Date modifiedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Emoticon {

        @Field("emoticon_theme_id")
        private ObjectId emoticonThemeId;

        private String emotion;
    }
}