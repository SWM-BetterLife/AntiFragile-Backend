package swm.betterlife.antifragile.domain.emoticontheme.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import swm.betterlife.antifragile.common.basetimeentity.BaseTimeEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "emoticon_themes")
public class EmoticonTheme extends BaseTimeEntity {

    @Id
    private ObjectId id;

    private String name;

    private Integer price;

    private List<ObjectId> memberIds;

    private List<Emoticon> emoticons;
}
