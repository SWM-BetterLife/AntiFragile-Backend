package swm.betterlife.antifragile.common.util;

import java.time.LocalDate;

public class AgeConverter {

    public static Integer convertDateToAge(LocalDate date) {
        LocalDate currentDate = LocalDate.now();

        int yearDiff = currentDate.getYear() - date.getYear();

        if (date.isAfter(currentDate.withYear(date.getYear()))) {
            return yearDiff;
        } else {
            return yearDiff + 1;
        }
    }
}
