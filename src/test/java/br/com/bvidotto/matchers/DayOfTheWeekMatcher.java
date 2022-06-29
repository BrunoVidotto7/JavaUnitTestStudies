package br.com.bvidotto.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.bvidotto.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DayOfTheWeekMatcher extends TypeSafeMatcher<Date> {
    private final Integer dayOfTheWeek;

    public DayOfTheWeekMatcher(Integer dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public void describeTo(Description desc) {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        String dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
        desc.appendText(dataExtenso);
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.verifyDayOfWeek(data, dayOfTheWeek);
    }
}
