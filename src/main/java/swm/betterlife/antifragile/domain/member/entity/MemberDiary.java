package swm.betterlife.antifragile.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "member_diaries")
public class MemberDiary {

    String memberId;

    Integer diaryTotalNum;

}
