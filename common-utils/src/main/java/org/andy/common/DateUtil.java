package org.andy.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期处理工具类。
 * <p>处理日期</p>
 *
 * @author huguangsheng
 * @version V1.0
 * @date 18/6/11 下午9:01
 */
public class DateUtil {

    public static final String YYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYMMDD = "yyyy-MM-dd";

    public static final String YMD = "yyyy/MM/dd";

    private static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat SIMPLE_FORMAT_SPOT = new SimpleDateFormat("yyyy.MM.dd");

    private static final SimpleDateFormat FULL_FORMAR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final SimpleDateFormat NO_FULL_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private static final Long ONE_MINUTE = 60000L;
    private static final Long ONE_HOUR = 3600000L;
    private static final Long HALF_HOUR = 1800000L;
    private static final Long ONE_SECOND = 1L;

    /**
     * 通用日期格式化方法，将日期格式化为yyyy-MM-dd形式。
     * 例：1900-01-01
     *
     * @param date
     * @return 格式化后的日期字符串。
     */
    public static String toSimpleFormat(Date date) {
        return SIMPLE_FORMAT.format(date);
    }

    public static String toSimpleFormat_Spot(String dateStr) {
        Date date = null;
        try {
            date = SIMPLE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return SIMPLE_FORMAT_SPOT.format(date);
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 通用日期格式化方法，将给定时间毫秒值格式化为yyyy-MM-dd形式。
     * 例：1900-01-01
     *
     * @param date
     * @return 格式化后的日期字符串。
     */
    public static String toSimpleFormat(long timeMillis) {
        Timestamp ts = new Timestamp(timeMillis);
        return SIMPLE_FORMAT.format(ts);
    }

    /**
     * 通用时间格式化方法，将日期格式化为yyyy-MM-dd hh:mm:ss形式。
     * 例：1900-01-01 13:00:00
     *
     * @param date 给定日期
     * @return String
     */
    public static String toFullTimeFormat(Date date) {
        return FULL_FORMAR.format(date);
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss形式。
     */
    public static String toFullTimeFormat() {
        return FULL_FORMAT.format(new Date());
    }

    /**
     * yyyyMMdd 格式，当前时间
     */
    public static String toNoFullTimeFormat() {
        return NO_FULL_FORMAT.format(new Date());
    }

    /**
     * 通用时间格式化方法，将给定时间毫秒值格式化为yyyy-MM-dd hh:mm:ss形式。
     * 例：1900-01-01 13:00:00
     *
     * @param timeMillis 时间戳
     * @return 格式化后的时间字符串。
     */
    public static String toFullTimeFormat(long timeMillis) {
        Timestamp ts = new Timestamp(timeMillis);
        return FULL_FORMAR.format(ts);
    }

    /**
     * 将Date按指定格式进行格式化处理。
     *
     * @param date  日期
     * @param format    格式化方法
     * @return 格式化后的date字符串。
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将 yyyy-MM-dd格式转换为java.util.Date 日期对象
     */
    public static Date parse4SimpleFormat(String dateValue, Date defaultVal) {
        try {
            return SIMPLE_FORMAT.parse(dateValue);
        } catch (ParseException e) {
            return defaultVal;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss 格式转换为java.util.Date 日期对象
     *
     * @param dateValue
     * @param defaultVal
     * @return
     */
    public static Date parse4FullFormat(String dateValue, Date defaultVal) {
        try {
            return FULL_FORMAR.parse(dateValue);
        } catch (ParseException e) {
            return defaultVal;
        }
    }

    /**
     * 计算某天处于一年中的第几周
     * 一周起始为星期一，截止为星期日。
     *
     * @param date 天
     * @return 周数 最小返回1（第一周）
     */
    public static int weekOfYear(Calendar date) {
        Calendar firstDayOfFirstWeekOnyear =
            firstDayOfFirstWeekOnyear(date.get(Calendar.YEAR));
        // 本年度第一周起始时间 大于 要计算的天，则将该天归入上一年度最后一周
        if (firstDayOfFirstWeekOnyear.getTimeInMillis() > date.getTimeInMillis()) {
            firstDayOfFirstWeekOnyear =
                firstDayOfFirstWeekOnyear(date.get(Calendar.YEAR) - 1);
        }
        long diff = date.getTimeInMillis()
            - firstDayOfFirstWeekOnyear.getTimeInMillis();
        int d = (int)(diff / 86400000) + 1;
        return (int)Math.ceil((double)d / 7);
    }

    private static Calendar firstDayOfFirstWeekOnyear(int year) {
        Timestamp firstDayOfYear = Timestamp.valueOf(year + "-01-01 00:00:00");
        Calendar firstDayOfFirstWeek = Calendar.getInstance();
        firstDayOfFirstWeek.setTime(firstDayOfYear);
        int diffD = 0; // 每年的 1月1号 与星期一的便宜，如果1月1日就是星期一，则本年起始周为本周，否则以下周一为第一周起始
        switch (firstDayOfFirstWeek.get(Calendar.DAY_OF_WEEK)) {
            case 1: // 周天 编译一天 到周一
                diffD = 1;
                break;
            case 2: // 周一 一年的第一周
                diffD = 0;
                break;
            case 3: // 周二  编译 4天 到下周一
                diffD = 6;
                break;
            case 4:
                diffD = 5;
                break;
            case 5:
                diffD = 4;
                break;
            case 6:
                diffD = 3;
                break;
            case 7:
                diffD = 2;
                break;
        }
        firstDayOfFirstWeek.setTimeInMillis(firstDayOfFirstWeek.getTimeInMillis()
            + diffD * 24 * 60 * 60 * 1000);
        return firstDayOfFirstWeek;
    }

    /**
     * 计算某天处于一年中的第几周
     *
     * @param year  年
     * @param month 月   1-12
     * @param day   日   1-31
     * @return 周数 最小返回1（第一周）
     */
    public static int weekOfYear(int year, int month, int day) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DAY_OF_MONTH, day);
        return weekOfYear(date);
    }

    /**
     * 计算某天处于一年中的第几周
     *
     * @param date 要计算的日期
     * @return 周数 最小返回1（第一周）
     */
    public static int weekOfYear(Date date) {
        Calendar d = Calendar.getInstance();
        d.setTime(date);
        return weekOfYear(d);
    }

    /**
     * 计算某天处于一年中的第几周
     *
     * @param millis 时期的毫秒值
     * @return 周数 最小返回1（第一周）
     */
    public static int weekOfYear(long millis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(millis);
        return weekOfYear(date);
    }

    /**
     * 根据年和周数，计算出给定年指定周的第一天日期（仅精确到天，小时后不准确）
     * 周一 ~ 周日 为一周。
     *
     * @param year 年
     * @param week 周 1 - 53
     * @return 某年第n周的 第一天。
     */
    public static Date firstDayOfWeek(int year, int week) {
        Calendar firstDayOfFirstWeekOnyear =
            firstDayOfFirstWeekOnyear(year);
        long diff = (long)(week - 1) * 7 * 86400000;
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(diff + firstDayOfFirstWeekOnyear.getTimeInMillis());
        return date.getTime();
    }

    /**
     * 计算两个时间点之间的差
     *
     * @return
     */
    public static Long accumulatedTime(Date startTime, String endTime) {
        if (StringUtil.isBlank(endTime)) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(YYMMDDHHMMSS);
        try {
            Date date = df.parse(endTime);
            Long accumulate = startTime.getTime() - date.getTime();
            Long resultseconds = accumulate / 1000;
            return resultseconds;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证字符串是否为日期
     *
     * @param value
     * @return
     */
    public static boolean isDate(String value) {
        boolean isDate = false;
        try {
            if (null == value || "".equals(value.trim())) {
                isDate = false;
            }
            value = value.replaceAll("-", "/");
            DateFormat df = new SimpleDateFormat(YMD);
            Date date = df.parse(value);
            isDate = true;
        } catch (Exception e) {
        }
        return isDate;
    }

    /**
     * 验证字符串是否为日期
     *
     * @param value
     * @return
     */
    public static boolean isLegal(String value) {
        boolean isLengal = false;
        try {
            if (null == value || "".equals(value.trim())) {
                isLengal = false;
            }
            value = value.replaceAll("-", "/");
            SimpleDateFormat df = new SimpleDateFormat(YMD);
            df.setLenient(false);//强制性验证日期是否为越界
            Date date = df.parse(value);
            isLengal = true;
        } catch (Exception e) {
        }
        return isLengal;
    }

    /**
     * 通过年份和月份获取当月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String format(String time) {
        if (StringUtil.isBlank(time)) {
            return null;
        }
        time = time.replaceAll("\\.", "-");
        Date date = null;
        DateFormat format = new SimpleDateFormat(YYMMDD);
        try {
            if (!isLegal(time)) {
                String[] times = time.split("[-]");
                if (times.length > 1) {
                    time = time + "-" + getDaysByYearMonth(Integer.valueOf(times[0]), Integer.valueOf(times[1]));
                }
            }
            date = format.parse(time);
        } catch (Exception e) {
        }
        if (null != date) {
            return toSimpleFormat(date);
        }
        return null;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay  指定日期
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
            .format(c.getTime());
        return dayAfter;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay  指定日期
     * @return
     */
    public static Date findSpecifiedDayBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    public static int getCurrentYear(Integer year) {
        Calendar calendar = Calendar.getInstance();
        if (null != year) {
            calendar.add(Calendar.YEAR, year);
        }
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth(int offset) {
        Calendar calendar = Calendar.getInstance();
        if (offset > 0) {
            calendar.add(Calendar.MONTH, offset);
        }
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static String targetDate(int i) {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);//当前时间前去一个月，即一个月前的时间
        return toFullTimeFormat(calendar.getTime());
    }

    public static Date halfHourAndOneSecond(Date date) {

        Date newDate = new Date();

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //Date date = sd.parse("2017-08-22 00:00:00");

        newDate.setTime(date.getTime() - HALF_HOUR - ONE_SECOND);

        //String halfHourAndOneSecondTime = sd.format(date);

        return newDate;
    }

    // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    // 获取当前时间所在月的开始日期
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取时间段内每周一的时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static List<Date> getPeriodFirstDayOfWeek(Date startTime, Date endTime) {
        List<Date> list = new LinkedList<Date>();
        //如果开始时间大于结束时间，直接返回
        if (startTime.getTime() > endTime.getTime()) {
            return list;
        }
        //获取开始时间的周一，添加到列表
        Date monday = getFirstDayOfWeek(startTime);
        list.add(monday);
        //获取下一个周一的日期
        Date nextMonday = DateUtils.addDays(monday, 7);
        //如果下一個周一小于等于结束时间，添加到列表，并且重新赋值下一周一时间，直到下一個周一大于结束时间
        while (nextMonday.getTime() <= endTime.getTime()) {
            list.add(nextMonday);
            nextMonday = DateUtils.addDays(nextMonday, 7);
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    public static Date getEpoch() {
        try {
            return DateUtils.parseDate("1970-01-01 00:00:00", new String[] {DateUtil.YYMMDDHHMMSS});
        } catch (ParseException e) {
        }
        return new Date(2017, 9, 19);
    }

    /**
     * 获取某个日期的开始时间
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) { calendar.setTime(d); }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
            0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) { calendar.setTime(d); }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
            59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取本周的开始时间
     */
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     */
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     */
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取今年是哪一年
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar)Calendar.getInstance();
        gc.setTime(date);
        return gc.get(1);
    }

    /**
     * 获取本月是哪一月
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar)Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取本月是哪一月
     */
    public static int getNowDay() {
        Calendar instance = Calendar.getInstance();
        return instance.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取本月是哪一月
     */
    public static String getNowMonthAndDay() {
        int nowMonth = getNowMonth();
        int nowDay = getNowDay();
        return nowMonth + "-" + nowDay;
    }

    public static void main(String[] args) {
        System.out.println("获取今天事几号：" + getNowDay());
        System.out.println("获取当前是几月:" + getNowMonth());
        System.out.println("获取当前的 年 - 月：" + getNowMonthAndDay());

        System.out.println("获取当前月的结束时间：" + getEndDayOfMonth());
        System.out.println("获取当前月的开始时间：" + getBeginDayOfMonth());
        System.out.println("获取当前周的开始时间：" + getBeginDayOfWeek());
        System.out.println("获取当前周的结束时间：" + getEndDayOfWeek());
        System.out.println("获取本周的开始时间：" + getBeginDayOfWeek());
    }

}
