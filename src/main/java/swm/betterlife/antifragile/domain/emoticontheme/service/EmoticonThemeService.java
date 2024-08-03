package swm.betterlife.antifragile.domain.emoticontheme.service;

import static swm.betterlife.antifragile.common.util.CollectionName.EMOTICON_THEMES;
import static swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonThemeName.DEFAULT;

import com.mongodb.client.result.UpdateResult;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import swm.betterlife.antifragile.common.exception.EmoticonThemeAlreadyHasMemberId;
import swm.betterlife.antifragile.common.exception.EmoticonThemeNotFoundException;
import swm.betterlife.antifragile.common.response.PagingResponse;
import swm.betterlife.antifragile.common.util.ObjectIdGenerator;
import swm.betterlife.antifragile.domain.diaryanalysis.entity.SelectedEmoticon;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonInfoFromEmotionResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonInfoResponse;
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
    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public PagingResponse<EmoticonThemeSummaryResponse> getAllEmoticonThemes(Pageable pageable) {
        Page<EmoticonTheme> emoticonThemes = emoticonThemeRepository.findAll(pageable);
        return PagingResponse.from(emoticonThemes.map(EmoticonThemeSummaryResponse::from));
    }

    @Transactional(readOnly = true)
    public EmoticonThemeOwnEntireResponse getAllOwnEmoticonThemes(String memberId) {
        List<EmoticonTheme> ownEmoticonThemes = getOwnEmoticonThemeEntityList(memberId);

        List<EmoticonThemeOwnDetailResponse> emoticonThemeDtoList = ownEmoticonThemes.stream()
            .map(EmoticonThemeOwnDetailResponse::from)
            .toList();

        return new EmoticonThemeOwnEntireResponse(emoticonThemeDtoList);
    }

    private List<EmoticonTheme> getOwnEmoticonThemeEntityList(String memberId) {
        Criteria priceCriteria = Criteria.where("name").is(DEFAULT.name());
        Criteria buyerIdsCriteria = Criteria.where("buyer_ids").in(memberId);
        Query query = new Query()
            .addCriteria(new Criteria().orOperator(priceCriteria, buyerIdsCriteria));

        return mongoTemplate.find(query, EmoticonTheme.class, EMOTICON_THEMES.getName());
    }

    @Transactional(readOnly = true)
    public EmoticonEntireResponse getAllEmoticonsByEmoticonThemeId(String emoticonThemeId) {
        EmoticonTheme emoticonTheme
            = emoticonThemeRepository.getEmoticonTheme(emoticonThemeId);
        List<EmoticonInfoResponse> emoticonDtoList
            = emoticonTheme.getEmoticons().stream().map(EmoticonInfoResponse::from).toList();
        return new EmoticonEntireResponse(emoticonDtoList);
    }

    @Transactional(readOnly = true)
    public List<EmoticonInfoFromEmotionResponse> getAllEmoticonsForAllThemesByEmotion(
        String memberId, Emotion emotion
    ) {
        List<EmoticonTheme> ownEmoticonThemes = getOwnEmoticonThemeEntityList(memberId);

        return ownEmoticonThemes.stream()
            .flatMap(
                emoticonTheme -> emoticonTheme.getEmoticons().stream()
                    .filter(emoticon -> emoticon.getEmotion().equals(emotion))
                    .map(emoticon -> EmoticonInfoFromEmotionResponse.builder()
                            .emoticonThemeId(emoticonTheme.getId())
                            .imgUrl(emoticon.getImgUrl())
                            .build()
                    )
            ).collect(Collectors.toList());
    }

    @Transactional
    public void addMemberIdToEmoticonTheme(String memberId, String emoticonThemeId) {

        Query query = Query.query(
            Criteria.where("_id").is(ObjectIdGenerator.generate(emoticonThemeId))
        );
        Update update = new Update().addToSet("buyer_ids", memberId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, EMOTICON_THEMES.getName());

        if (result.getMatchedCount() > 0 && result.getModifiedCount() == 0) {
            throw new EmoticonThemeAlreadyHasMemberId();
        } else if (result.getModifiedCount() == 0) {
            throw new EmoticonThemeNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public String getEmoticonImgUrl(SelectedEmoticon selectedEmoticon) {
        EmoticonTheme emoticonTheme = emoticonThemeRepository.getEmoticonTheme(
            selectedEmoticon.getEmoticonThemeId());

        return emoticonTheme.getEmoticons().stream()
            .filter(emoticon -> emoticon.getEmotion().equals(selectedEmoticon.getEmotion()))
            .map(Emoticon::getImgUrl)
            .findFirst()
            .orElse("");
    }
}
