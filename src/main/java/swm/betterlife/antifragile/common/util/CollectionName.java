package swm.betterlife.antifragile.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CollectionName {
    CONTENTS("contents"),
    CONTENT_REVIEWS("content_reviews"),
    DIARY_ANALYSES("diary_analyses"),
    EMOTICON_THEMES("emoticon_themes"),
    MEMBERS("members"),
    POINT_RECEIPTS("point_receipts"),
    RECENT_CONTENTS("recent_contents"),
    ;
    private final String name;
}
