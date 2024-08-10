package swm.betterlife.antifragile.common.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class WritingLocalDateToKstConverter implements Converter<LocalDate, Date> {
    @Override
    public Date convert(LocalDate source) {
        return Timestamp.valueOf(source.atStartOfDay().plusHours(9));
    }
}
