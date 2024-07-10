package swm.betterlife.antifragile.domain.emoticontheme.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonInfoResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnDetailResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeSummaryResponse;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emoticon;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;
import swm.betterlife.antifragile.domain.emoticontheme.entity.Emotion;
import swm.betterlife.antifragile.domain.emoticontheme.repository.EmoticonThemeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping("emoticon-themes")
public class EmoticonThemeService {

    private final EmoticonThemeRepository emoticonThemeRepository;

    @Transactional(readOnly = true)
    public EmoticonThemeEntireResponse getAllEmoticonThemes() {
        List<EmoticonTheme> emoticonThemes = emoticonThemeRepository.findAll();
        List<EmoticonThemeSummaryResponse> emoticonThemeDtoList
            = emoticonThemes.stream().map(EmoticonThemeSummaryResponse::from).toList();
        return new EmoticonThemeEntireResponse(emoticonThemeDtoList);
    }

    @Transactional(readOnly = true)
    public EmoticonThemeOwnEntireResponse getAllOwnEmoticonThemes(ObjectId memberId) {
        List<EmoticonTheme> emoticonThemes = emoticonThemeRepository.findAll();
        List<EmoticonTheme> ownEmoticonThemes = new ArrayList<>();

        for (EmoticonTheme emoticonTheme : emoticonThemes) {
            if (
                emoticonTheme.getPrice() == null || emoticonTheme.getBuyerIds().contains(memberId)
            ) {
                ownEmoticonThemes.add(emoticonTheme);
            }
        }

        List<EmoticonThemeOwnDetailResponse> emoticonThemeDtoList =
            ownEmoticonThemes.stream().map(EmoticonThemeOwnDetailResponse::from).toList();
        emoticonThemeDtoList.forEach(e -> log.info("_id: {}", e.id()));
        return new EmoticonThemeOwnEntireResponse(emoticonThemeDtoList);
    }

    @Transactional(readOnly = true)
    public EmoticonEntireResponse getAllEmoticons(ObjectId emoticonThemeId) {
        EmoticonTheme emoticonTheme
            = emoticonThemeRepository.getEmoticonTheme(emoticonThemeId);
        List<EmoticonInfoResponse> emoticonDtoList
            = emoticonTheme.getEmoticons().stream().map(EmoticonInfoResponse::from).toList();
        return new EmoticonEntireResponse(emoticonDtoList);
    }

    @Transactional
    public void saveMemberIdAtEmoticonTheme(ObjectId memberId, ObjectId emoticonThemeId) {
        EmoticonTheme emoticonTheme
            = emoticonThemeRepository.getEmoticonTheme(emoticonThemeId);

        List<ObjectId> buyerIds = emoticonTheme.getBuyerIds();
        buyerIds.add(memberId);
    }

}
