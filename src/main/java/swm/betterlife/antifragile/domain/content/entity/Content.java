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
@Document(collection = "content")
public class Content extends BaseTimeEntity {

    @Id
    private String id;

    private String title;

    private List<String> likeMemberIds = new ArrayList<>();

    private List<SaveMember> saveMembers = new ArrayList<>();

    private String thumbnailImgUrl;

    private YoutubeInfo youTubeInfo;

    private Integer appViewNumber; // 앱 자체 조회수

    private Integer appLikeNumber; // 앱 자체 좋아요 수

    private String url;
}
