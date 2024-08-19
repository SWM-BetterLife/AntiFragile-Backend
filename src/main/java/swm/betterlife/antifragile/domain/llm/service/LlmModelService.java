package swm.betterlife.antifragile.domain.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.betterlife.antifragile.common.util.S3ImageComponent;
import swm.betterlife.antifragile.domain.llm.dto.response.LlmModelUrlResponse;

@Service
@RequiredArgsConstructor
public class LlmModelService {

    private final S3ImageComponent s3ImageComponent;

    public LlmModelUrlResponse getLlmModelUrl() {
        String modelUrl = s3ImageComponent.getModelUrl("gemma-1.1-2b-it-cpu-int4.bin");
        return new LlmModelUrlResponse(modelUrl);
    }
}
