package swm.betterlife.antifragile.domain.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.betterlife.antifragile.domain.content.dto.response.ContentRecommendResponse;
import swm.betterlife.antifragile.domain.content.repository.ContentRepository;
import swm.betterlife.antifragile.domain.member.entity.LoginType;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public ContentRecommendResponse saveRecommendContents(String memberId, LocalDate date) {
        return null;
    }
}
