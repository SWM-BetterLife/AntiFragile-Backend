package swm.betterlife.antifragile.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import swm.betterlife.antifragile.domain.member.service.MemberService;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {
    private final MemberService memberService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void resetDailyRecommendCount() {
        memberService.resetRemainRecommendNumber();
    }
}
