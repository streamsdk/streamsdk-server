package com.ugc.utils;




import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public final class DateUtils
{

    public static final Date DATE_MIN = new Date(-62167392000000L); //0000-01-01
    public static final Date DATE_MAX = new Date(253402214400000L); //9999-12-31

    public static final long MILLISECONDS_PER_MINUTE = 60 * 1000;
    public static final long MILLISECONDS_PER_HOUR = 60 * MILLISECONDS_PER_MINUTE;
    public static final long MILLISECONDS_PER_DAY = 24 * MILLISECONDS_PER_HOUR;
    public static final long MILLISECONDS_PER_FIVE_MINUTES = 5 * MILLISECONDS_PER_MINUTE;

    public static final int MONTHES_PRE_YEAR = 12;

    private static final String[] WEEK_DAYS_FULL = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", " THURSDAY", "FRIDAY", "SATURDAY"};
    private static final String[] WEEK_DAYS_ABREV = {"SUN", "MON", "TUES", "WEDS", " THURS", "FRI", "SAT"};
    private static final String[] RELETIVE_DAYS = {"TODAY", "YESTERDAY"};

    public static final Date BEGINNING_OF_TIME = new Date(0);
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static final Pattern LONG_YEAR_PATTERN = Pattern.compile("\\d{1,2}[/:-]\\d{1,2}[/:-]\\d{4}");
    private static final Pattern SHORT_YEAR_PATTERN = Pattern.compile("\\d{1,2}[/:-]\\d{1,2}[/:-]\\d{2}");
    private static final Pattern NO_YEAR_NUMBERIC_MONTH_PATTERN = Pattern.compile("\\d{1,2}[/:-]\\d{1,2}");
    private static final Pattern NO_YEAR_TEXT_MONTH_FIRST_PATTERN = Pattern.compile("[a-zA-Z]{3,4}\\s\\d{1,2}");
    private static final Pattern NO_YEAR_TEXT_MONTH_SECOND_PATTERN = Pattern.compile("\\d{1,2}\\s[a-zA-Z]{3,4}");
    private static final String SEPERATOR = "|";


    public static SimpleDateFormat getUTCFormatter(String pattern) {
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        f.setTimeZone(UTC);
        return f;
    }

    public static SimpleDateFormat getUTCInstance() {
        SimpleDateFormat f = new SimpleDateFormat();
        f.setTimeZone(UTC);
        return f;
    }

    public static Date convertDate(Date original, TimeZone from, TimeZone to) {
//        DateTime dateTime = new DateTime(original, DateTimeZone.forTimeZone(from));
//        DateTime convertedDateTime = dateTime.toDateTime(DateTimeZone.forTimeZone(to));
//        return convertedDateTime.toDate();

        long time = original.getTime();
        long adjustToUTC = from.getOffset(time);
        long adjustToNew = to.getOffset(time - adjustToUTC);


        return new Date(time - adjustToUTC + adjustToNew);
    }

    public static Date getLocalTime(Date utc, TimeZone tz) {
        //Make sure the time is utc time first.
        //Otherwise daylight saving may be applied more than once
        long localOffset = utc.getTimezoneOffset() * 60000;
        long utcTime = utc.getTime() + localOffset;
        return new Date(utcTime + tz.getOffset(utcTime));

    }

    public static Date getTruncatedDay(Date day) {
        Calendar cal = getCalendarUTC(day);
        truncateCalendarToBegginingOfDay(cal);
        return cal.getTime();
    }

    private static Calendar getCalendarUTC(Date day) {
        Calendar cal = Calendar.getInstance(UTC);
        cal.setTime(day);
        return cal;
    }

    public static Date getTruncatedMonth(Date day) {
        Calendar cal = getCalendarUTC(day);
        truncateCalendarToBegginingOfMonth(cal);
        return cal.getTime();
    }

    public static void truncateCalendarToBegginingOfMonth(Calendar cal) {
        truncateCalendarToBegginingOfDay(cal);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
    }

    public static void truncateCalendarToBegginingOfDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
    }

    public static String getDateTime() {
        return getDateTime(new Date());
    }

    public static String getDateTime(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
    }

    public static Date getDate(String dateString, SimpleDateFormat formatter) {
        return getDate(dateString, formatter, new ParsePosition(0));
    }

    public static Date getDate(String dateString, SimpleDateFormat formatter, ParsePosition position) {
        return formatter.parse(dateString, position);
    }

    public static Date previousDay(Date day) {
        return previousDay(day, 1);
    }

    public static Date previousDay(Date day, int numOfDays) {
        return offsetDay(day, -numOfDays);
    }

    public static Date nextDay(Date day) {
        return nextDay(day, 1);
    }

    public static Date nextDay(Date day, int numOfDays) {
        return offsetDay(day, numOfDays);
    }

    private static Date offsetDay(Date day, int numOfDays) {
        Calendar cal = getCalendarUTC(day);
        cal.add(Calendar.DAY_OF_YEAR, numOfDays);
        truncateCalendarToBegginingOfDay(cal);
        return cal.getTime();
    }

    public static Date tomorrowPlusXHours(int hours) {
        Calendar cal = getCalendarUTC(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        truncateCalendarToBegginingOfDay(cal);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        return cal.getTime();
    }

    public static boolean areSameDay(Date first, Date second) {
        if (first == null || second == null)
            return false;

        return getCalendarUTC(first).get(Calendar.DAY_OF_YEAR) == getCalendarUTC(second).get(Calendar.DAY_OF_YEAR) &&
               areSameYear(first, second);
    }

    private static boolean areSameYear(Date first, Date second) {
        return getCalendarUTC(first).get(Calendar.YEAR) == getCalendarUTC(second).get(Calendar.YEAR);
    }

    public static Date getToday() {
        return getTruncatedDay(new Date());
    }

    public static String getAbsStringDate(Date date) {
        return getSimpleDateFormatYYYYMMDD().format(date);
    }

    public static String getAbsStringDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date getToday(TimeZone tz) {
        return getTruncatedDay(getLocalTime(new Date(), tz));
    }

    public static Date getTimeMinusNHours(Calendar time, int nHours) {
        return getTimeMinusNHours(time.getTime(), nHours);
    }

    public static Date getTimeMinusNHours(Date time, int nHours) {
        return new Date(time.getTime() - (MILLISECONDS_PER_HOUR * nHours));
    }

    public static Date getTimePlusNHours(Calendar time, int nHours) {
        return getTimePlusNHours(time.getTime(), nHours);
    }

    public static Date getTimePlusNHours(Date time, int nHours) {
        return new Date(time.getTime() + (MILLISECONDS_PER_HOUR * nHours));
    }

    public static Date getReletiveDayAsDate(String reletiveDay) {
        if (getDateYesterdayToday(getTodayAsGregorianCalendar(), reletiveDay.toUpperCase().trim()) != null)
            return getDateYesterdayToday(getTodayAsGregorianCalendar(), reletiveDay.toUpperCase().trim());
        else
            return getPreviousOccuranceOfWeekday(getTodayAsGregorianCalendar(), reletiveDay.toUpperCase().trim());
    }

    private static GregorianCalendar getTodayAsGregorianCalendar() {
        return new GregorianCalendar(UTC);
    }

    private static Date getPreviousOccuranceOfWeekday(Calendar calendar, String weekday) {
        //Day 0-6
        for (int i = 0; i < WEEK_DAYS_FULL.length; i++) {
            if (weekday.equals(WEEK_DAYS_FULL[i]) || weekday.equals(WEEK_DAYS_ABREV[i])) {
                truncateCalendarToBegginingOfDay(calendar);
                //one added to day of week to convert to calendar array that starts at 1 not 0
                subtractToPreviousWeekday(calendar, (i + 1));
                return calendar.getTime();
            }
        }
        return null;
    }

    private static void subtractToPreviousWeekday(Calendar calendar, int dayOfWeek) {
        do {
            calendar.add(Calendar.DAY_OF_WEEK, -1);

            if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek)
                break;
        }
        while (true);
    }

    private static Date getDateYesterdayToday(Calendar calendar, String reletiveDay) {
        //Relative to today 0-1
        for (int i = 0; i < 2; i++) {
            if (reletiveDay.equals(RELETIVE_DAYS[i])) {
                if (i == 0)
                    return calendar.getTime();
                else {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    return calendar.getTime();
                }
            }
        }
        return null;
    }

    public static String getAllDatePatternString() {
        String constructedString = "";

        constructedString += getSeperatedArray(WEEK_DAYS_FULL) + SEPERATOR;
        constructedString += getSeperatedArray(WEEK_DAYS_ABREV) + SEPERATOR;
        constructedString += getSeperatedArray(RELETIVE_DAYS) + SEPERATOR;
        constructedString += LONG_YEAR_PATTERN.pattern() + SEPERATOR;
        constructedString += SHORT_YEAR_PATTERN.pattern() + SEPERATOR;
        constructedString += NO_YEAR_NUMBERIC_MONTH_PATTERN.pattern() + SEPERATOR;
        constructedString += NO_YEAR_TEXT_MONTH_FIRST_PATTERN.pattern() + SEPERATOR;
        constructedString += NO_YEAR_TEXT_MONTH_SECOND_PATTERN.pattern();

        return constructedString;
    }

    private static String getSeperatedArray(String[] array) {
        String constructedString = "";

        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                constructedString += SEPERATOR;
            constructedString += array[i];
        }

        return constructedString;
    }

    public static Date parseHumanReadableDate(String dateString) {
        Date date = null;

        if (LONG_YEAR_PATTERN.matcher(dateString).matches())
            date = parseDateYearFormat(dateString, "MM", "yyyy");
        else if (SHORT_YEAR_PATTERN.matcher(dateString).matches())
            date = parseDateYearFormat(dateString, "MM", "yy");
        else if (NO_YEAR_NUMBERIC_MONTH_PATTERN.matcher(dateString).matches())
            date = parseDateYearFormat(dateString, "MM", null);
        else if (NO_YEAR_TEXT_MONTH_FIRST_PATTERN.matcher(dateString).matches())
            date = parseDateYearFormat(dateString, "MMMM", null);
        else if (NO_YEAR_TEXT_MONTH_SECOND_PATTERN.matcher(dateString).matches())
            date = parseDateYearFormat(dateString, "MMMM", null);

        if (date == null)
            date = getReletiveDayAsDate(dateString);

        return date;
    }


    private static Date parseDateYearFormat(String dateString, String monthFormat, String yearFormat) {
        StringTokenizer findSeperator = new StringTokenizer(dateString, "\\/:- ", true);
        findSeperator.nextToken();
        String seperator = findSeperator.nextToken();

        String formatString;
        boolean setYear = false; //in case user didn't specify

        if (yearFormat == null) {
            if (monthFormat.length() > 2) // ie., Jan
                if (Character.isDigit(dateString.charAt(0)))
                    //12 jan
                    formatString = "dd" + seperator + monthFormat;
                else
                    //jan 12
                    formatString = monthFormat + seperator + "dd";
            else
                //eg, 2/12
                formatString = "dd" + seperator + monthFormat;

            setYear = true;
        } else
            formatString = "dd" + seperator + monthFormat + seperator + yearFormat;

        SimpleDateFormat formatter = getUTCFormatter(formatString);
        formatter.setLenient(true);
        Date date;
        try {
            date = formatter.parse(dateString);
        }
        catch (ParseException e) {
            return null;
        }

        if (setYear) {
            GregorianCalendar d = new GregorianCalendar();
            d.setTime(date);
            d.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            date = d.getTime();
        }
        return date;
    }

    public static int getAge(Date date) {
        if (date == null)
            return 0;
        int age;
        Calendar birthdate = Calendar.getInstance();
        birthdate.setTime(date);
        Calendar now = Calendar.getInstance();
        age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
        birthdate.add(Calendar.YEAR, age);
        if (now.before(birthdate))
            age--;
        return age < 0 ? 0 : age;
    }

    public static boolean isOldEnough(Calendar birthday, int requiredAge) {
        Calendar now = Calendar.getInstance();
        birthday.add(Calendar.YEAR, requiredAge);

        return now.after(birthday);
    }

    public static int getTimeInMonthes(Calendar time) {
        return time.get(Calendar.YEAR) * MONTHES_PRE_YEAR + time.get(Calendar.MONTH);
    }

    public static Calendar getMinDateInMonth(int year, int month) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DATE, date.getMinimum(Calendar.DATE));
        date.set(Calendar.HOUR_OF_DAY, date.getMinimum(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, date.getMinimum(Calendar.MINUTE));
        date.set(Calendar.SECOND, date.getMinimum(Calendar.SECOND));
        date.set(Calendar.MILLISECOND, date.getMinimum(Calendar.MILLISECOND));
        return date;
    }

    public static Date getFirstDayOfMonth(Date date) {
        String timeStr = DateUtils.getSimpleDateFormatYYYYMM().format(date) + "01";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(DateUtils.getSimpleDateFormatYYYYMMDD(), timeStr));

        int previousMonthDays = 2 - calendar.get(Calendar.DAY_OF_WEEK);
        if (previousMonthDays == 1)
            previousMonthDays = previousMonthDays - 7;
        calendar.add(Calendar.DAY_OF_MONTH, previousMonthDays);
        return calendar.getTime();
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int previousWeekDays = 2 - calendar.get(Calendar.DAY_OF_WEEK);
        if (previousWeekDays == 1)
            previousWeekDays = previousWeekDays - 7;
        calendar.add(Calendar.DAY_OF_MONTH, previousWeekDays);
        return calendar.getTime();
    }


    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 42);

        if (calendar.get(Calendar.DAY_OF_MONTH) == 14) // happen only first day of February is monday
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        if (calendar.get(Calendar.DAY_OF_MONTH) >= 7)
            calendar.add(Calendar.DAY_OF_MONTH, -7);
        return calendar.getTime();
    }

    public static Date parseDate(SimpleDateFormat sdf, String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {

        }
        return null;
    }

    public static SimpleDateFormat getSimpleDateFormatYYYYMM() {
        return new SimpleDateFormat("yyyyMM");
    }

    public static SimpleDateFormat getSimpleDateFormatYYYYMMDD() {
        return new SimpleDateFormat("yyyyMMdd");
    }

    public static SimpleDateFormat getSimpleDateFormatHH() {
        return new SimpleDateFormat("HH");
    }

    public static SimpleDateFormat getSimpleDateFormatMMDD() {
        return new SimpleDateFormat("MMdd");
    }

    public static SimpleDateFormat getSimpleDateFormatEHH() {
        return new SimpleDateFormat("EHH");
    }

    public static Date getDateHoursInFuture(int hoursForward) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hoursForward);
        return calendar.getTime();
    }
}

