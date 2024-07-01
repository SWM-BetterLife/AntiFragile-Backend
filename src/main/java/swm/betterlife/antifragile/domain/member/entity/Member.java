package swm.betterlife.antifragile.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.betterlife.antifragile.common.BaseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String nickName;
    private String password;
    @Enumerated(EnumType.STRING)
    private LoginType loginType;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
