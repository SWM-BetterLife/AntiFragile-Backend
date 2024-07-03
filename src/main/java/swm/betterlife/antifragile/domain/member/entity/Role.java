package swm.betterlife.antifragile.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"), USER("유저");

    private final String toKorean;
}
