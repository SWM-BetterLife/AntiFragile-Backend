package swm.betterlife.antifragile.domain.recommend.dto.request;

import java.util.ArrayList;
import java.util.List;
import swm.betterlife.antifragile.domain.recommend.dto.OpenAiMessage;

public record OpenAiRequest(
    String model,
    List<OpenAiMessage> messages,
    int temperature
) {
    public OpenAiRequest(
        String model,
        String prompt,
        int temperature
    ) {
        this(model,
            new ArrayList<>(List.of(new OpenAiMessage("user", prompt))),
            temperature);
    }
}