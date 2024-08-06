package swm.betterlife.antifragile.common.config;

import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void timeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")));
        Locale.setDefault(Locale.KOREA);
    }
}
