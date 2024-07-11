package swm.betterlife.antifragile.domain.content.entity;

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
@Document(collection = "contents")
public class Content extends BaseTimeEntity {

    @Id
    private String id;

    private String title;

    private String description;

    private List<String> likeMemberIds = new ArrayList<>();

    private List<SaveMember> saveMembers = new ArrayList<>();

    private String thumbnailImgUrl;

    private YoutubeInfo youTubeInfo;

    @Builder.Default
    private Long appViewNumber = 0L;

    @Builder.Default
    private Long appLikeNumber = 0L;

    private String url;
}
