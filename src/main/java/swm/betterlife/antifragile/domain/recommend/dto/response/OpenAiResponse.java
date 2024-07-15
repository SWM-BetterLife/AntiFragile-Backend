package swm.betterlife.antifragile.domain.recommend.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.recommend.dto.OpenAiMessage;

@Builder
public record OpenAiResponse(
    List<Choice> choices
) {
    @Builder
    public record Choice(
        int index,
        OpenAiMessage message
    ) {
    }
}