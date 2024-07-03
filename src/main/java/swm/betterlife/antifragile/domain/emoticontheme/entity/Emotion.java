package swm.betterlife.antifragile.domain.emoticonTheme.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emotion {
    SAD("슬픔"),
    JOY("기쁨"),
    ANXIETY("불안"),
    ANGER("분노"),
    JEALOUSY("질투"),
    ENNUI("따분"),
    PANIC("당황"),
    PASSION("열정"),
    FLUTTER("설렘"),
    NORMAL("무표정"),
    PAIN("아픔"),
    FEAR("공포"),
    AMAZEMENT("놀람"),
    FATIGUE("피곤"),
    DEPRESSION("우울");

    private final String toKorean;
}
