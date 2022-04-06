package by.morunov.test.old_system.note.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class DateCompareUtils {
    public static boolean isDateInPeriod(final LocalDate date,
                                         final LocalDate fromInclusive,
                                         final LocalDate toInclusive) {
        return date.isEqual(fromInclusive)
                || date.isEqual(toInclusive)
                || (date.isAfter(fromInclusive) && date.isBefore(toInclusive));
    }
}
