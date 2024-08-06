package swm.betterlife.antifragile.domain.diaryanalysis.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.DiaryAnalysisModifyRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.request.DiaryAnalysisSaveRequest;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.response.EmoticonMonthlyResponse;
import swm.betterlife.antifragile.domain.diaryanalysis.dto.response.EmoticonDailyResponse;
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
        @RequestBody DiaryAnalysisSaveRequest request
    ) {
        diaryAnalysisService.saveDiaryAnalysis(
            principalDetails.memberId(), request
        );
        return ResponseBody.ok();
    }

    @PatchMapping
    public ResponseBody<Void> modifyDiaryAnalysis(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody DiaryAnalysisModifyRequest request,
        @RequestParam("date") LocalDate date
    ) {
        diaryAnalysisService.modifyDiaryAnalysis(
            principalDetails.memberId(), request, date
        );
        return ResponseBody.ok();
    }

    @GetMapping("/emotions")
    public ResponseBody<EmoticonDailyResponse> getDateEmotions(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("date") LocalDate date
    ) {
        return ResponseBody.ok(
            diaryAnalysisService.getDateEmotions(
                principalDetails.memberId(), date)
        );
    }

    @GetMapping("/emoticons") // 월간 감정일기 이모티콘 조회
    public ResponseBody<EmoticonMonthlyResponse> getMonthEmoticons(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam("year-month") YearMonth yearMonth
    ) {
        return ResponseBody.ok(
            diaryAnalysisService.getMonthEmoticons(
                principalDetails.memberId(), yearMonth)
        );
    }
}
