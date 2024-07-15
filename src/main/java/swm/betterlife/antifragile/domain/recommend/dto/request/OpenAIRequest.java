package swm.betterlife.antifragile.domain.recommend.dto.request;

import java.util.ArrayList;
import java.util.List;
import swm.betterlife.antifragile.domain.recommend.dto.OpenAIMessage;

public record OpenAIRequest(
    String model,
    List<OpenAIMessage> messages,
    int temperature,
    int maxTokens,
    int topP,
    int frequencyPenalty,
    int presencePenalty
) {
    public OpenAIRequest(
        String model,
        String prompt,
        int temperature,
        int maxTokens,
        int topP,
        int frequencyPenalty,
        int presencePenalty
    ) {
        this(model,
            new ArrayList<>(List.of(new OpenAIMessage("user", prompt))),
            temperature,
            maxTokens,
            topP,
            frequencyPenalty,
            presencePenalty);
    }
}