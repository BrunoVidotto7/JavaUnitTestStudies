package br.com.bvidotto.matchers;

import java.util.Date;

import br.com.bvidotto.utils.DateUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DateDifferentDaysMatcher extends TypeSafeMatcher<Date> {

    private final Integer numberOfDays;

    public DateDifferentDaysMatcher(Integer qtdDias) {
        this.numberOfDays = qtdDias;
    }

    public void describeTo(Description arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DateUtils.isSameDate(data, DateUtils.getDataWithDifferentDate(numberOfDays));
    }
}
