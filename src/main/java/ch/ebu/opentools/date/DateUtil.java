package ch.ebu.opentools.date;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Date Util
 */
public final class DateUtil {
    public static final ZoneId ZONE_ID_OF_UTC = ZoneId.of("UTC");
    public static final LocalTime LOCAL_TIME_REFERENCE_FOR_DURATION = LocalTime.of(0, 0);
    public static final TimeZone TIME_ZONE;
    public static final ZoneOffset ZONE_OFFEST;
    public static final String HH_MM = "HH:mm";
    public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";
    public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_YYYY_MM_DD_HH_MM;
    public static final DateTimeFormatter DATE_TIME_YYYY_MM_DD_HH_MM_SS;
    public static final int MIN_YEAR = 0;
    public static final int MAX_YEAR = 9999;
    public static final int SEXAGESIMAL = 60;
    public static final long SEXAGESIMAL_LONG = 60L;

    public static final String DEFAULT_TIME_ZONE = "UTC";
    public static final DateTimeFormatter TIMEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    static {
        TIME_ZONE = TimeZone.getTimeZone(ZONE_ID_OF_UTC);
        ZONE_OFFEST = ZoneOffset.UTC;
        DATE_TIME_YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        DATE_TIME_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    private DateUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID_OF_UTC);
    }

    public static LocalDate today() {
        return now().toLocalDate();
    }

    public static LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        return Objects.isNull(instant) ? null : LocalDateTime.ofInstant(instant, ZONE_ID_OF_UTC);
    }

    private static Instant convertLocalDateTimeToInstant(LocalDateTime date) {
        return Objects.isNull(date) ? null : date.toInstant(ZONE_OFFEST);
    }

    public static Instant convertTimestampToInstant(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
            return convertLocalDateTimeToInstant(localDateTime);
        }
    }

    public static Duration convertLocalTimeToDuration(LocalTime time) {
        return Objects.isNull(time) ? null : Duration.between(LOCAL_TIME_REFERENCE_FOR_DURATION, time);
    }

    public static LocalTime convertDurationToLocalTime(Duration duration) {
        return Objects.isNull(duration) ? null : LOCAL_TIME_REFERENCE_FOR_DURATION.plus(duration);
    }

    public static LocalDateTime from(String string) {
        return localDateTimeFromString(string, YYYY_MM_DD_HH_MM);
    }

    public static LocalDateTime localDateTimeFromString(String string, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(string, formatter);
    }

    public static Date toDate(String string) {
        return Date.from(from(string).toInstant(ZONE_OFFEST));
    }

    public static LocalTime localtimeFrom(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH_MM);
        return LocalTime.parse(string, formatter);
    }

    public static String stringFrom(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH_MM);
        return localTime.format(formatter);
    }

    public static String formatHoursAndMinutes(LocalTime localTime) {
        if (localTime == null) {
            return "";
        } else {
            String formatString = "";
            if (localTime.getHour() != 0) {
                formatString = localTime.getHour() + " Hours ";
            }

            formatString = formatString + String.valueOf(localTime.getMinute()) + " Minutes ";
            return formatString;
        }
    }

    static String from(LocalDateTime localDateTime) {
        return from(localDateTime, YYYY_MM_DD_HH_MM);
    }

    static String from(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(localDateTime);
    }

    public static String ddMMyyyyfrom(LocalDateTime localDateTime) {
        return from(localDateTime, DD_MM_YYYY_HH_MM);
    }

    public static String ddMMyyyyfrom(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);
        return formatter.format(localDate);
    }

    public static LocalDate convertStringToLocalDateFormatddMMyyyy(String dateToconvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);
        return LocalDate.parse(dateToconvert, formatter);
    }

    public static LocalDate convertStringToLocalDateFormatYYYYMMDD(String dateToconvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(dateToconvert, formatter);
    }

    public static String yyyymmddFrom(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return formatter.format(localDate);
    }

    public static String yyyymmddFrom(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return formatter.format(localDateTime);
    }

    public static LocalDate convertStringToLocalDateFormat(String dateToconvert) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        return LocalDate.parse(dateToconvert, formatter);
    }

    public static Long getDurationInMinutesFrom(LocalDateTime startDate, LocalDateTime endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate) ? Long.valueOf(Math.max(0L, ChronoUnit.MINUTES.between(startDate, endDate))) : Long.valueOf(0L);
    }

    public static LocalDateTime getEarlierDate(LocalDateTime... localDateTimes) {
        LocalDateTime laterLocalDateTime = null;
        LocalDateTime[] var2 = localDateTimes;
        int var3 = localDateTimes.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            LocalDateTime localDateTime = var2[var4];
            if (laterLocalDateTime == null || laterLocalDateTime.isAfter(localDateTime)) {
                laterLocalDateTime = localDateTime;
            }
        }

        return laterLocalDateTime;
    }

    public static LocalDateTime getLaterDate(LocalDateTime... localDateTimes) {
        LocalDateTime laterLocalDateTime = null;
        LocalDateTime[] var2 = localDateTimes;
        int var3 = localDateTimes.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            LocalDateTime localDateTime = var2[var4];
            if (laterLocalDateTime == null || laterLocalDateTime.isBefore(localDateTime)) {
                laterLocalDateTime = localDateTime;
            }
        }

        return laterLocalDateTime;
    }

    public static LocalDateTime from(Date dateTimeSent) {
        return LocalDateTime.ofInstant(dateTimeSent.toInstant(), ZONE_ID_OF_UTC);
    }

    public static Date dateFromLocalDateTime(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZONE_ID_OF_UTC);
        return Date.from(zdt.toInstant());
    }

    public static Date dateFromLocalDateTime(LocalDate localDate) {
        ZonedDateTime zdt = localDate.atStartOfDay(ZONE_ID_OF_UTC);
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime convertToStartOfTheDay(LocalDate localDate) {
        return LocalDateTime.of(localDate, LocalTime.MIN);
    }

    public static LocalDateTime convertToEndOfTheDay(LocalDate localDate) {
        return localDate == null ? null : LocalDateTime.of(localDate, LocalTime.MAX);
    }

    public static boolean isBeforeOrEquals(LocalDateTime first, LocalDateTime second) {
        return first.isBefore(second) || first.equals(second);
    }

    public static boolean isBeforeOrEquals(LocalDate first, LocalDate second) {
        return first.isBefore(second) || first.equals(second);
    }

    public static boolean isSameDay(LocalDateTime first, LocalDateTime second) {
        return from(first).equals(from(second));
    }

    public static boolean isAfterOrEquals(LocalDateTime first, LocalDateTime second) {
        return first.isAfter(second) || first.equals(second);
    }

    public static boolean isAfterOrEquals(LocalDate first, LocalDate second) {
        return first.isAfter(second) || first.equals(second);
    }

    public static LocalDateTime of(Date date, Date time) {
        return date.toInstant().atZone(ZONE_ID_OF_UTC).toLocalDate().atTime(time.toInstant().atZone(ZONE_ID_OF_UTC).toLocalTime());
    }

    public static LocalDateTime toLocalDateTimeFromXMLGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public static String stringFromXMLGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        return from(xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime(), YYYY_MM_DD);
    }

    public static LocalDate toLocalDateFromXMLGregorianCalendar(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime().toLocalDate();
    }

    public static int localTimeToDurationInMinutes(LocalTime localTime) {
        return Objects.isNull(localTime) ? 0 : localTime.toSecondOfDay() / SEXAGESIMAL;
    }

    public static LocalDateTime convertToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendarDate, String stringTime) {
        LocalTime localTime = localtimeFrom(stringTime);
        xmlGregorianCalendarDate.setTime(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        return toLocalDateTimeFromXMLGregorianCalendar(xmlGregorianCalendarDate);
    }

    public static LocalTime convertToLocalTime(BigInteger bigInteger) {
        return LocalTime.ofSecondOfDay(bigInteger.longValue() * SEXAGESIMAL_LONG);
    }

    public static long getDurationInSecondes(LocalDateTime startDate, LocalDateTime endDate) {
        return Duration.between(startDate, endDate).getSeconds();
    }

    public static LocalDateTime convertToLocalDateTime(String s) {
        return OffsetDateTime.parse(s, TIMEFORMATTER).toLocalDateTime();
    }

    public static OffsetDateTime convertToOffsetDateTime(String s) {
        return OffsetDateTime.parse(s, TIMEFORMATTER);
    }

    public static String convertAsIsoFormat(LocalDateTime localDateTime) {
        return TIMEFORMATTER.format(localDateTime);
    }

    public static String convertAsIsoFormatFromOffset(Temporal localDateTime) {
        return TIMEFORMATTER.format(localDateTime);
    }

    public static Date convertLocalDateTimeToUtilDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * Validate date string format
     *
     * @param dateToValidate
     * @param param
     */
    public static boolean dateValidator(String dateToValidate, String param) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        sdf.setLenient(false);
        try {
            //if not valid, it will throw ParseException
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Used to convert one LocalDateTime to string by one determined pattern
     *
     * @param localDateTime the date
     * @param pattern       the pattern to be used
     * @return the converted string
     */
    public static String stringFrom(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
}
