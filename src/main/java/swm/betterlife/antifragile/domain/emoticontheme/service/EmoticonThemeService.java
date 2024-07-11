package swm.betterlife.antifragile.domain.emoticontheme.service;

import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import swm.betterlife.antifragile.common.exception.EmoticonThemeNotFoundException;
import swm.betterlife.antifragile.common.response.PagingResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonInfoResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnDetailResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeOwnEntireResponse;
import swm.betterlife.antifragile.domain.emoticontheme.dto.response.EmoticonThemeSummaryResponse;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;
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
    public void addMemberIdToEmoticonTheme(ObjectId memberId, ObjectId emoticonThemeId) {

        Query query = Query.query(Criteria.where("_id").is(emoticonThemeId));
        Update update = new Update().addToSet("buyer_ids", memberId);
        UpdateResult result = mongoTemplate.updateFirst(query, update, "emoticon_theme");

        if (result.getModifiedCount() == 0) {
            throw new EmoticonThemeNotFoundException();
        }
    }

}