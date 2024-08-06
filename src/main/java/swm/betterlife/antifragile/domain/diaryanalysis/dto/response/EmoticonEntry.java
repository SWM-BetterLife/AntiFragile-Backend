package swm.betterlife.antifragile.domain.diaryanalysis.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record EmoticonEntry(
    String imgUrl,
    LocalDate diaryDate
) {}