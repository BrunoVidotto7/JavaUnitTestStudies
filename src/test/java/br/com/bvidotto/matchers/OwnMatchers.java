package br.com.bvidotto.matchers;

import java.util.Calendar;

public class OwnMatchers {
    public static DayOfTheWeekMatcher isDay(Integer dayOfTheWeek) {
        return new DayOfTheWeekMatcher(dayOfTheWeek);
    }

    public static DayOfTheWeekMatcher isMonday(){
        return new DayOfTheWeekMatcher(Calendar.MONDAY);
    }

    public static DateDifferentDaysMatcher isTodayWithTheDifferentOf(Integer qtdDias) {
        return new DateDifferentDaysMatcher(qtdDias);
    }

    public static DateDifferentDaysMatcher isToday() {
        return new DateDifferentDaysMatcher(0);
    }
}
