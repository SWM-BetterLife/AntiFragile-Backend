package swm.betterlife.antifragile.domain.recommend.dto.request;

import java.util.ArrayList;
import java.util.List;
import swm.betterlife.antifragile.domain.recommend.dto.OpenAiMessage;

public record OpenAiRequest(
    String model,
    List<OpenAiMessage> messages,
    int temperature,
    int maxTokens,
    int topP,
    int frequencyPenalty,
    int presencePenalty
) {
    public OpenAiRequest(
        String model,
        String prompt,
        int temperature,
        int maxTokens,
        int topP,
        int frequencyPenalty,
        int presencePenalty
    ) {
        this(model,
            new ArrayList<>(List.of(new OpenAiMessage("user", prompt))),
            temperature,
            maxTokens,
            topP,
            frequencyPenalty,
            presencePenalty);
    }
}