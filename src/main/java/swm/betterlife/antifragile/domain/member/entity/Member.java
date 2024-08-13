package swm.betterlife.antifragile.domain.member.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Document(collection = "members")
public class Member extends BaseTimeEntity {

    @Id
    private String id;

    private String email;

    private String password;

    private String nickname;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String job;

    private String profileImgFilename;

    private List<String> emoticonThemeIds = new ArrayList<>();

    @Builder.Default
    private Integer remainRecommendNumber = 3;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public void decrementRemainRecommendNumber() {
        this.remainRecommendNumber--;
    }
}
