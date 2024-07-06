package swm.betterlife.antifragile.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_ADMIN("관리자"), ROLE_USER("사용자");

    private final String toKorean;
}
