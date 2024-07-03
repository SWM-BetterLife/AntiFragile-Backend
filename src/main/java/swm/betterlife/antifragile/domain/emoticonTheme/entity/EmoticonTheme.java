package swm.betterlife.antifragile.domain.emoticonTheme.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.baseTimeEntity.BaseTimeEntity;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "emoticon_themes")
public class EmoticonTheme extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private String name;

    private Number price;

    private List<ObjectId> userIds;

    private List<Emoticon> emoticons;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Emoticon {

        private String emotion;

        private String img;
    }
}