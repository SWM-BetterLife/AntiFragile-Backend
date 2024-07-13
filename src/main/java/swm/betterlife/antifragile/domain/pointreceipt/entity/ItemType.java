package swm.betterlife.antifragile.domain.pointreceipt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType {
    EMOTICON_THEME("이모테콘 테마"),
    RE_RECOMMENDATION("콘텐츠 재추천"),
    ;

    private final String toKorean;
}
