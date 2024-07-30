package swm.betterlife.antifragile.domain.emoticontheme.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Document(collection = "emoticon_themes")
public class EmoticonTheme extends BaseTimeEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private EmoticonThemeName name;

    private Integer price;

    private List<String> buyerIds = new ArrayList<>();

    private List<Emoticon> emoticons = new ArrayList<>();
}
