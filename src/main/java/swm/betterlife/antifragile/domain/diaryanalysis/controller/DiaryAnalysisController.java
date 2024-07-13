package swm.betterlife.antifragile.domain.diaryanalysis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.ModifyDiaryAnalysisRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.SaveDiaryAnalysisRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.service.DiaryAnalysisService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary-analyses")
public class DiaryAnalysisController {

    private final DiaryAnalysisService diaryAnalysisService;

    @PostMapping
    public ResponseBody<Void> saveDiaryAnalysis(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody SaveDiaryAnalysisRequest request
    ) {
        diaryAnalysisService.saveDiaryAnalysis(
            principalDetails.memberId(), request
        );
        return ResponseBody.ok();
    }

    @PutMapping
    public ResponseBody<Void> modifyDiaryAnalysis(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody ModifyDiaryAnalysisRequest request,
        @RequestParam DateTime date
    ) {
        diaryAnalysisService.modifyDiaryAnalysis(
            principalDetails.memberId(), request, date
        );
        return ResponseBody.ok();
    }
}
