package swm.betterlife.antifragile.common.converter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class ReadingLocalDateToKstConverter implements Converter<Date, LocalDate> {
    @Override
    public LocalDate convert(Date source) {
        return source.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .minusHours(9).toLocalDate();
    }
}