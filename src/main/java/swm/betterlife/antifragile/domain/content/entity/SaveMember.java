package swm.betterlife.antifragile.domain.content.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveMember {

    private String memberId;

    private List<String> tags = new ArrayList<>();
}
