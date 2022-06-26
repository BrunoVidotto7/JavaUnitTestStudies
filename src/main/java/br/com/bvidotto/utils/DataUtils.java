package br.com.bvidotto.utils;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;

public class DataUtils {
	
	/**
	 * Retorna a data enviada por parametro com a adição dos dias desejado
	 * 	a Data pode estar no futuro (dias > 0) ou no passado (dias < 0)
	 * 
	 * @param data
	 * @param dias
	 * @return
	 */
	public static Date adicionarDias(Date data, int dias) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(DAY_OF_MONTH, dias);
		return calendar.getTime();
	}
	
	/**
	 * Returns the current data with the difference of days sent per parameter
	 *   the Date can be in the future (positive parameter) or in the past (negative parameter)
	 * 
	 * @param days Number of days to be incremented/decremented
	 * @return updated date
	 */
	public static Date getDataWithDifferentDate(int days) {
		return adicionarDias(new Date(), days);
	}
	
	/**
	 * Returns an instance of <code>Date</code> reflecting the values passed by parameter
	 * 
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static Date obterData(int day, int month, int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, day);
		calendar.set(MONTH, month - 1);
		calendar.set(YEAR, year);
		return calendar.getTime();
	}
	
	/**
	 * Verify if one date is equal another
	 * 	This comparison considers only day, month and year
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return (calendar1.get(DAY_OF_MONTH) == calendar2.get(DAY_OF_MONTH))
				&& (calendar1.get(MONTH) == calendar2.get(MONTH))
				&& (calendar1.get(YEAR) == calendar2.get(YEAR));
	}
	
	/**
	 * Checks if a given date is the desired day of the week
	 * 
	 * @param date Date to be evaluated
	 * @param weekDay <code>true</code> if it is the desired day of the week, <code>false</code> otherwise
	 * @return
	 */
	public static boolean verifyDayOfWeek(Date date, int weekDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(DAY_OF_WEEK) == weekDay;
	}
}
