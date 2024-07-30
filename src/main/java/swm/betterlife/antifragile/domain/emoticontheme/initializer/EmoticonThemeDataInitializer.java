package swm.betterlife.antifragile.domain.emoticontheme.initializer;

import static swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonThemeName.DEFAULT;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emoticon;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonThemeName;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;
import swm.betterlife.antifragile.domain.emoticontheme.repository.EmoticonThemeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmoticonThemeDataInitializer {

    @Value("${aws.s3.url}")
    private String s3Url;

    private final EmoticonThemeRepository emoticonThemeRepository;

    private final String prefixPath = "EMOTICON";

    @PostConstruct
    public void initEmoticonThemeData() {
        saveEmoticonTheme(DEFAULT, 0);
    }

    public void saveEmoticonTheme(EmoticonThemeName name, Integer price) {
        if (!emoticonThemeRepository.existsByName(name)) {
            emoticonThemeRepository.save(getEmoticonTheme(name, price));
        }
    }

    private EmoticonTheme getEmoticonTheme(EmoticonThemeName name, Integer price) {
        return EmoticonTheme.builder()
            .name(name)
            .price(price)
            .buyerIds(new ArrayList<>())
            .emoticons(getEmoticons(name)).build();
    }

    private List<Emoticon> getEmoticons(EmoticonThemeName name) {
        List<Emoticon> emoticons = new ArrayList<>();
        for (Emotion emotion : Emotion.values()) {
            String imgUrl
                = s3Url + "/" + prefixPath + "/" + name + "/" + emotion.name() + ".png";
            emoticons.add(Emoticon.builder().emotion(emotion).imgUrl(imgUrl).build());
        }
        return emoticons;
    }

}
