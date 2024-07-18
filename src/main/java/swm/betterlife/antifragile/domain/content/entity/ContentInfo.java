package swm.betterlife.antifragile.domain.content.entity;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "content_infos")
public class ContentInfo {

    String contentId;

    Long appLikeNumber;

    Long appViewNumber;

    Long appSaveNumber;
}
