package com.qnkj.common.utils;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 时间工具类,
 */
@Slf4j
public class DateTimeUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String SHORT_TIME_FORMAT = "HH:mm";

    public static final String YEARS_MONTHS_FORMAT = "yyyyMM";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public static final String FULL_FORMAT_SPEPARATE="yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获取格林威治时间戳
     */
    public static long gettimeStamp() {
        Calendar cal = Calendar.getInstance() ;
        cal.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        long timeStamp = cal.getTimeInMillis() - 28800000;
        return timeStamp / 1000;

    }
    /**
     * 获取格林威治时间戳
     */
    public static long getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance() ;
        cal.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return cal.getTimeInMillis() - 28800000;
    }

    /**
     * 时间戳转换为时间
     */
    public static Date stampToDate(long timeStamp) {
        return new Date(timeStamp*1000);
    }
    /**
     * 获取时间偏移量
     */
    public static int getzoneOffset() {
        Calendar cal = Calendar.getInstance() ;
        return cal.get(Calendar.ZONE_OFFSET);
    }
    /**
     * 获取时间偏移量
     */
    public static void timestampRequestDataBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (!text.isEmpty()) {
                    if (ValidationUtil.isDateTime(text)) {
                        setValue(Timestamp.valueOf(text));
                    } else if (ValidationUtil.isDate(text)) {
                        setValue(Timestamp.valueOf(text + " 00:00:00"));
                    } else if (ValidationUtil.isNumeric(text)) {
                        setValue(new Timestamp(Long.parseLong(text)));
                    }
                }
            }
        });

    }
    /**
     * 获取当前时间
     */
    public static String getDatetime() {
        return getDatetime(DATE_TIME_FORMAT);
    }

    /**
     * 获取指定格式的当前时间
     */
    public static String getDatetime(String foramat) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat(foramat);
        return dateFormat.format(date);
    }

    /**
     * 获取指定格式的指定时间
     */
    public static String getDatetime(Date date, String foramat) {
        SimpleDateFormat dateFormat= new SimpleDateFormat(foramat);
        return dateFormat.format(date);
    }

    /**
     * 获取指定格式的指定时间
     */
    public static String getDatetime(String datetime, String foramat) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        java.sql.Date date = null; //初始化
        try {
            Date udate = format.parse(datetime);
            date = new java.sql.Date(udate.getTime());
        } catch (ParseException e) {
            log.error("getDatetime {} format {} parse : {}",datetime,foramat,e.getMessage());
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat(foramat);
        return dateFormat.format(date);
    }

    /**
     * 获取指定格式的指定时间
     */
    public static String getDatetime(Date date) {
        SimpleDateFormat dateFormat= new SimpleDateFormat(DATE_TIME_FORMAT);
        return dateFormat.format(date);
    }
    /**
     * 字符串转时间
     */
    public static Date convert(String str) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        java.sql.Date date = null; //初始化
        try {
            Date udate = format.parse(str);
            date = new java.sql.Date(udate.getTime());
        } catch (ParseException e) {
            log.error("convert {} parse : {}",str, e.getMessage());
        }
        return date;
    }

    /**
     * @param value
     * String yyyy-MM-dd
     * @return Date
     */
    public static Date string2date(String value) {
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            log.error("string2date {} parse : {}",value, e.getMessage());
        }
        return null;
    }

    public static Date string2datetime(String value) {
        if (value == null || "".equals(value.trim())) {
            return null;
        }

        try {
            if (value.length()==8) {
                SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
                return sdf.parse(value);
            }else if (value.length()==19) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
                return sdf.parse(value);
            }else if (value.length() == 16) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_SHORT_TIME_FORMAT);
                return sdf.parse(value);
            }else if (value.length()==10) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                return sdf.parse(value);
            }
        } catch (ParseException e) {
            log.error("string2datetime {} parse : {}",value, e.getMessage());
        }
        return null;
    }

    public static Date toDateTime(String value) {
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        try{
            Date date;
            if(value.contains(":")) {
                date = string2datetime(value);
            } else {
                date = string2date(value);
            }
            return date;
        }catch (Exception e) {
            log.error("toDateTime {} parse : {}",value,e.getMessage());
        }
        return null;
    }

    /**
     * 将字符串转换成指定格式的日期值
     * @param value
     *   String 需转换为日期的字符串
     * @param format
     * String 日期格式
     * @return Date 返回日期值
     */
    public static Date string2date(String value, String format) {
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            log.error("string2date {} format : {} parse : {}",value,format,e.getMessage());
        }
        return null;
    }

    public static String date2string(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static String dateTime2string(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static Date string2time(String value){
        if(null == value){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            log.error("string2time {}  parse : {}",value,e.getMessage());
        }
        return null;
    }
    /**
     * 日期转指定格式字符串
     * @param date 日期
     * @param format 格式
     * @return
     */
    public static String date2string(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getNowDate(String format) {
        String dateTime = "";
        try {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            dateTime = sdf.format(now);
        } catch (Exception e) {
            log.error("getNowDate {}  parse : {}",format,e.getMessage());
        }
        return dateTime;
    }

    public static Date getCurrentDateTime(){
        return string2datetime(getNowDateTime());
    }

    public static String getNowDate() {
        return getNowDate(DATE_FORMAT);
    }

    public static String getNowDateTime() {
        return getNowDate(DATE_TIME_FORMAT);
    }

    public static Long getDaysInterval(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 86400000;
    }

    public static Long getSecondsInterval(Date startDate, Date endDate) {
        return ((endDate.getTime() - startDate.getTime()) / 1000);
    }

    public static Long getMinutesInterval(Date startDate, Date endDate) {
        return ((endDate.getTime() - startDate.getTime()) / 1000/ 60);
    }

    public static String getDate(int days) {
        Date date = new Date();
        date.setTime(date.getTime() + 86400000L * days);
        return date2string(date);
    }
    /**
     * 计算相差分钟的时间
     * @param minute
     * @return
     */
    public static Date getDifferTime(int minute){
        Date result = new Date();
        result.setTime(result.getTime() + 300000L * minute);

        return result;
    }

    public static String getDaydiffDate(Date date, int days) {
        Date result = new Date();
        result.setTime(date.getTime() + 86400000L * days);
        return date2string(result);
    }

    public static String getDaydiffDate(String date, int days) {
        Date result = new Date();
        Date temp = string2date(date);
        if(temp != null) {
            result.setTime(temp.getTime() + 86400000L * days);
        }
        return date2string(result);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getNowYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getNowMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getNowDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取24小时制 小时
     */
    public static int getHours(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getNowHours() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getNowMinutes() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MINUTE);
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * 根据日期（Date）获取哪年的第几季度
     * @param date 日期
     * @return 季度
     */
    public static Integer getQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int mouth = calendar.get(Calendar.MONTH) + 1;
        if(mouth<=3){
            return 1;
        }else if(mouth<=6){
            return 2;
        }else if(mouth<=9){
            return 3;
        }else if(mouth<=12){
            return 4;
        } else {
            return 0;
        }
    }

    /**
     * 获取指定季度的第一天
     * @param quarter 指定是哪一季度
     * @return 日期格式字符串
     */
    public static String getQuarterFirstDay(Date baseDate, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        switch (quarter) {
            case 2:
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DATE, 1);
                break;
            case 3:
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DATE, 1);
                break;
            case 4:
                calendar.set(Calendar.MONTH, 9);
                calendar.set(Calendar.DATE, 1);
                break;
            default:
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                break;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(calendar.getTime());
    }

    /**
     * 获取指定季度的最后一天
     * @param quarter 指定季度数
     * @return 返回日期字符串
     */
    public static String getQuarterLastDay(Date baseDate, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        switch (quarter) {
            case 2:
                calendar.set(Calendar.MONTH, 5);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case 3:
                calendar.set(Calendar.MONTH, 8);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case 4:
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            default:
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(calendar.getTime());
    }

    /**
     * 获取基准时间的周一
     * @param baseDate 指定日期
     * @return 返回日期字符串
     */
    public static String getWeekFirstDay(Date baseDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, 1 - day);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(calendar.getTime());
    }

    /**
     * 获取指基准时间的周日
     * @param baseDate
     * @return
     */
    public static String getWeekLastDay(Date baseDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseDate);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DATE, 7 - day);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(calendar.getTime());
    }

    public static int getAge(Date birthDay) {
        if(null != birthDay){
            Calendar sysCalendar = Calendar.getInstance();
            Calendar birCalendar = Calendar.getInstance();
            birCalendar.setTime(birthDay);
            if (sysCalendar.get(Calendar.MONTH) > birCalendar.get(Calendar.MONTH)){
                return sysCalendar.get(Calendar.YEAR) - birCalendar.get(Calendar.YEAR) + 1;
            } else {
                return sysCalendar.get(Calendar.YEAR) - birCalendar.get(Calendar.YEAR);
            }
        }
        return 0;
    }

    public static Date addDays(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addMonths(int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date addYears(int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date addYears(Date date, int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }


    public static Date getFullStartDateTime(Date time){
        String start = date2string(time, DATE_FORMAT)+" 00:00:00.000";
        return string2date(start, FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullStartDateTime(String date){
        return string2date(date+" 00:00:00.000", FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullEndDateTime(Date time){
        String end=date2string(time, DATE_FORMAT)+" 23:59:59.999";
        return string2date(end, FULL_FORMAT_SPEPARATE);
    }

    public static Date getFullEndDateTime(String date){
        return string2date(date+" 23:59:59.999", FULL_FORMAT_SPEPARATE);
    }

    private static final Pattern IS_DATE_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
    public static boolean isDate(String date) {
        Matcher matcher = IS_DATE_PATTERN.matcher(date);
        return matcher.matches();
    }

    private static final Pattern IS_MONTH_DATE_PATTERN = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
    public static boolean isMonthDate(String date) {
        Matcher matcher = IS_MONTH_DATE_PATTERN.matcher(date);
        return matcher.matches();
    }

    /**
     * 比较当前数据的时间比已有数据的时间是新还是旧
     * @param firstDateStr
     * @param secondDateStr
     * @return 如果firstDateStr是最新，返回true
     */
    public static boolean isLatestDate(String firstDateStr,String secondDateStr){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            Date date = sdf.parse(firstDateStr);
            Date existedDate = sdf.parse(secondDateStr);
            if(date.before(existedDate)){
                return false;
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return true;
    }

    public static boolean isNotLatestDate(String firstDateStr,String secondDateStr){
        return !isLatestDate(firstDateStr,secondDateStr);
    }

    /**
     * 时间整数戳转化为Date
     * new Date(millisecond)
     * Date转成整数
     * Date.getTime();
     */

    /**
     * 获得时间戳
     * @param date
     */
    public static Long getDateTimesTamp(String date){
        Date temp = string2date(date,DATE_TIME_FORMAT);
        if(temp != null){
            return temp.getTime();
        } else {
            return 0L;
        }
    }

    /**
     * @author HuangQR
     * 按天获得分段日期
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static List<String> splitDateStrByDay(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date startDate = toDateTime(startDateStr);
            Date endDate = toDateTime(endDateStr);
            Calendar caleEnd = Calendar.getInstance();
            caleEnd.setTime(endDate);
            Calendar caleMid = Calendar.getInstance();
            caleMid.setTime(startDate);

            List<String> dreList = new ArrayList<>(1);
            while (caleMid.before(caleEnd) || caleMid.equals(caleEnd)) {
                dreList.add(sdf.format(caleMid.getTime()));
                caleMid.add(Calendar.DATE,1);
            }
            return dreList;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>(1);
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay(Boolean isZh){
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        if(Boolean.TRUE.equals(isZh)) {
            weekDays = new String[] {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

    public static String getDateForDay(String datetime) {
        Date currDate = getCurrentDateTime();
        Date settDate = toDateTime(datetime);
        if(currDate != null && settDate != null) {
            long day = getDaysInterval(settDate, currDate);

            if (day == 0) {
                return "今天 " + getDatetime(toDateTime(datetime), SHORT_TIME_FORMAT);
            }
            if (day == 1) {
                return "昨天 " + getDatetime(toDateTime(datetime), SHORT_TIME_FORMAT);
            }
        }
        return datetime;
    }

    //region 日期处理的一些私有方法
    @ApiOperation("将指定日期按指定类型处理")
    public static Date getFormatDate(int type, int setp, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (setp != 0) {
            c.add(type, setp);
        }
        return c.getTime();
    }

    @ApiOperation("将指定日期转换成指定格式的字符串")
    public static String getDateStringByFormat(Date date, String format) {
        try {
            if(date == null) {
                log.error("getDateStringByFormat: date is null");
                return null;
            }
            return new SimpleDateFormat(format).format(date);
        }catch (Exception e) {
            log.error("getDateStringByFormat: {}", e.getMessage());
            return null;
        }
    }

    @ApiOperation("将指定的日期字符串转换成指定格式的日期")
    public static Date getDateByFormatString(String date, String format) {
        try {
            if(date.indexOf(':') < 0){
                date += " 00:00:00";
            }
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            log.error("getDateByFormatString: {}", e.getMessage());
            return null;
        }
    }

    @ApiOperation("获取指定日期相关部份")
    public static int getDateAttr(int type, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(type);
    }

    @ApiOperation("获取指定日期是在哪一季")
    public static int getSeason(Date date) {
        int season = 0;
        int month = getDateAttr(Calendar.MONTH, date);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    @ApiOperation("获取指定日期所在季的信息")
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

    @ApiOperation("获取指定日期月的第一天")
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    @ApiOperation("获取指定日期月的最后一天")
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    @ApiOperation("获取指定日期月的最后一天")
    public static Integer getMaxDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    /**
     * 按指定日期单位计算两个日期间的间隔
     *
     * @param timeInterval，
     * @param date1，
     * @param date2
     * @return date1-date2，
     */
    public static synchronized long diff(String timeInterval, Date date1, Date date2) {
        if ("year".equals(timeInterval)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long time = calendar.get(Calendar.YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.YEAR);
        }

        if ("quarter".equals(timeInterval)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long time = calendar.get(Calendar.YEAR) * 4L;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 4L;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH) / 3;
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH) / 3;
        }

        if ("month".equals(timeInterval)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long time = calendar.get(Calendar.YEAR) * 12L;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 12L;
            calendar.setTime(date1);
            time += calendar.get(Calendar.MONTH);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.MONTH);
        }

        if ("week".equals(timeInterval)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long time = calendar.get(Calendar.YEAR) * 52L;
            calendar.setTime(date2);
            time -= calendar.get(Calendar.YEAR) * 52L;
            calendar.setTime(date1);
            time += calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(date2);
            return time - calendar.get(Calendar.WEEK_OF_YEAR);
        }

        if ("day".equals(timeInterval)) {
            long time = date1.getTime() / 1000 / 60 / 60 / 24;
            return time - date2.getTime() / 1000 / 60 / 60 / 24;
        }

        if ("hour".equals(timeInterval)) {
            long time = date1.getTime() / 1000 / 60 / 60;
            return time - date2.getTime() / 1000 / 60 / 60;
        }

        if ("minute".equals(timeInterval)) {
            long time = date1.getTime() / 1000 / 60;
            return time - date2.getTime() / 1000 / 60;
        }

        if ("second".equals(timeInterval)) {
            long time = date1.getTime() / 1000;
            return time - date2.getTime() / 1000;
        }

        return date1.getTime() - date2.getTime();
    }

    public static String formatMinutes(long times) {
        long days = times / (60 * 60 * 24);
        long hours = (times % (60 * 60 * 24)) / (60 * 60);
        long minutes = (times % (60 * 60)) / 60;
        long seconds = times % 60;
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            return hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            return minutes + "分钟";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 获取指定日期当年第一天的日期字符串
     *
     * @param dateStr     yyyy-MM-dd HH:mm:ss
     * @param amountToAdd 加或减年，可以是负数
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfYearStr(String dateStr, Long amountToAdd) {
        LocalDateTime localDateTime = getPlusLocalDateTime(dateStr, amountToAdd, ChronoUnit.YEARS);
        return format(localDateTime.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0), DATE_TIME_FORMAT);
    }

    /**
     * 获取指定日期当年最后一天的日期字符串
     *
     * @param dateStr     yyyy-MM-dd HH:mm:ss
     * @param amountToAdd 加或减年，可以是负数
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfYearStr(String dateStr, Long amountToAdd) {
        LocalDateTime localDateTime = getPlusLocalDateTime(dateStr, amountToAdd, ChronoUnit.YEARS);
        return format(localDateTime.with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59), DATE_TIME_FORMAT);
    }

    /**
     * 获取指定日期当周第一天的日期字符串,这里第一天为周一
     *
     * @param dateStr     yyyy-MM-dd HH:mm:ss
     * @param amountToAdd 加或减年，可以是负数
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfWeekStr(String dateStr, Long amountToAdd) {
        LocalDateTime localDateTime = getPlusLocalDateTime(dateStr, amountToAdd, ChronoUnit.WEEKS);
        return format(localDateTime.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0), DATE_TIME_FORMAT);
    }

    /**
     * 获取指定日期当周最后一天的日期字符串,这里最后一天为周日
     *
     * @param dateStr     yyyy-MM-dd HH:mm:ss
     * @param amountToAdd 加或减年，可以是负数
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfWeekStr(String dateStr, Long amountToAdd) {
        LocalDateTime localDateTime = getPlusLocalDateTime(dateStr, amountToAdd, ChronoUnit.WEEKS);
        return format(localDateTime.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59), DATE_TIME_FORMAT);
    }

    /**
     * 获取加减年、月等后的LocalDateTime
     *
     * @param dateStr
     * @param amountToAdd
     * @param chronoUnit  （ChronoUnit.YEARS,ChronoUnit.MONTHS,ChronoUnit.WEEKS,ChronoUnit.DAYS）
     * @return
     */
    public static LocalDateTime getPlusLocalDateTime(String dateStr, Long amountToAdd, ChronoUnit chronoUnit) {
        LocalDateTime localDateTime = parseLocalDateTime(dateStr, DATE_TIME_FORMAT);
        if (Objects.nonNull(amountToAdd) && Objects.nonNull(chronoUnit)) {
            localDateTime = localDateTime.plus(amountToAdd, chronoUnit);
        }
        return localDateTime;
    }

    /**
     * 日期时间字符串转换为日期时间(java.time.LocalDateTime)
     *
     * @param localDateTimeStr 日期时间字符串
     * @param pattern          日期时间格式 例如DATETIME_PATTERN
     * @return LocalDateTime 日期时间
     */
    public static LocalDateTime parseLocalDateTime(String localDateTimeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(localDateTimeStr, dateTimeFormatter);
    }

    /**
     * 获取日期所在周
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static int getWeekOfYear(String dateStr, String pattern) {
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        //第一个参数代表周是从周一开始计算，第二个是限定第一个自然周最少要几天
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        return localDate.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * 格式化时间
     *
     * @param temporal
     * @param pattern
     * @return
     */
    public static String format(TemporalAccessor temporal, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return dateTimeFormatter.format(temporal);
    }
    /**
     * 将秒数转化为“天时分秒”的格式
     *
     * @param secondCount
     * @return
     */
    public static String convertDHMS(long secondCount) {
        String retval = "";
        long days = secondCount / (60 * 60 * 24);
        long hours = (secondCount % (60 * 60 * 24)) / (60 * 60);
        long minutes = (secondCount % (60 * 60)) / 60;
        long seconds = secondCount % 60;

        if (days > 0) {
            retval = days + "天" + hours + "小时";
        } else if (hours > 0) {
            retval = hours + "小时" + minutes + "分";
        } else if (minutes > 0) {
            retval = minutes + "分" + seconds + "秒";
        } else {
            retval = seconds + "秒";
        }
        return retval;
    }
}
