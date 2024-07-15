package swm.betterlife.antifragile.domain.recommend.dto.response;

import java.util.List;
import lombok.Builder;
import swm.betterlife.antifragile.domain.recommend.dto.OpenAIMessage;

@Builder
public record OpenAIResponse(
    List<Choice> choices
) {
    @Builder
    public record Choice(
        int index,
        OpenAIMessage message
    ) {
    }
}