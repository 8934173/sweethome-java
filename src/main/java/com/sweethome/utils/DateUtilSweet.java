package com.sweethome.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtilSweet {

    public final static String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public final static String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    private final static int[] DATE_UNIT_ARR = new int[]{Calendar.MILLISECOND, Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY,
            Calendar.DATE, Calendar.MONTH, Calendar.YEAR};
    public static Calendar calendar = null;
    public static DateFormat dateFormat = null;
    public static Date date = null;
    /**
     * 将日期转为 字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }
    /**
     * 将日期转换为 字符串(转换的时间按照当前登录用户的时区)
     *
     * @param date
     * @param format
     * @param timeZone
     * @return
     */
    public static String dateToString(Date date, String format, String timeZone) {
        if (date == null) {
            return null;
        }
        //1、格式化日期
        return getTimeZoneSimpleDateFormat(format, timeZone).format(date);
    }
    /**
     * 获取当前登录用户的 日期格式化对象
     *
     * @param timeZone
     * @param format
     * @return
     */
    private static SimpleDateFormat getTimeZoneSimpleDateFormat(String format, String timeZone) {
        //1、获取对应时区的格式化器
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat;
    }

    /**
     * 获取当前时间的前十五天日期
     * @param format 格式化方式
     * @return 时间
     */
    public static String getFifteenDaysAgo(String format) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -15);
        return DateUtilSweet.dateToString(calendar.getTime(), format);
    }

    /**
     * 当前时间  距离当天晚上23:59:59  秒数 也就是今天还剩多少秒
     * @return 秒
     */
    public static Long getNowTo24Second() {
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
            long overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime()))/1000;
            //当前时间  距离当天晚上23:59:59  秒数 也就是今天还剩多少秒
            return 24*60*60 - overTime;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return 0L;
        }
    }

    public static Date stringToDate(String dateStr, String format, String timeZone) {
        if (dateStr == null || format == null) {
            return null;
        }
        try {
            return getTimeZoneSimpleDateFormat(format, timeZone).parse(dateStr);
        } catch (ParseException e) {
            throw BaseRuntimeException.getException(e);
        }
    }
    /**
     *将字符串转为日期
     * @param dateStr
     * @param format
     * @return
     */
    public static Date stringToDate_CTT(String dateStr, String format) {
        if (dateStr == null || format == null) {
            return null;
        }
        try {
            return getTimeZoneSimpleDateFormat(format, "CTT").parse(dateStr);
        } catch (ParseException e) {
            throw BaseRuntimeException.getException(e);
        }
    }
    /**
     * 获取最近在当前日期之前的最后一个日期单位
     *
     * @param date
     * @param calendarUnit 只支持 DateUtil.DATE_UNIT_ARR
     * @return
     */
    public static Date getFloorDate(Date date, int calendarUnit) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i <= DATE_UNIT_ARR.length - 1; i++) {
            if (DATE_UNIT_ARR[i] > calendarUnit) {
                if (Calendar.DATE == DATE_UNIT_ARR[i]) {
                    calendar.set(DATE_UNIT_ARR[i], 1);
                } else {
                    calendar.set(DATE_UNIT_ARR[i], 0);
                }
            }
            if (DATE_UNIT_ARR[i] == calendarUnit) {
                break;
            }
        }
        return calendar.getTime();
    }
    /**
     * 获取最近在当前日期之后的第一个日期单位
     *
     * @param date
     * @param calendarUnit 只支持 DateUtil.DATE_UNIT_ARR
     * @return
     */
    public static Date getCeilDate(Date date, int calendarUnit) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i <= DATE_UNIT_ARR.length - 1; i++) {
            if (DATE_UNIT_ARR[i] > calendarUnit) {
                if (Calendar.DATE == DATE_UNIT_ARR[i]) {
                    calendar.set(DATE_UNIT_ARR[i], 1);
                } else {
                    calendar.set(DATE_UNIT_ARR[i], 0);
                }
            }
            if (DATE_UNIT_ARR[i] == calendarUnit) {
                calendar.add(DATE_UNIT_ARR[i], 1);
                break;
            }
        }
        return calendar.getTime();
    }
    /**
     * 将开始时间、结束时间 根据日期单位划分成 时间段
     *
     *
     * @param startDate
     * @param endDate
     * @param calendarUnit Calendar.MONTH,Calendar.DATE
     * @param dateNum  指定的单位日期数量
     * @return 每一个数组第一个为开始时间,第二个为结束时间; 第一个元素结束时间总是等于第二元素开始时间
     */
    public static List<Date[]> splitDate(Date startDate, Date endDate, int calendarUnit, int dateNum) {
        List<Date[]> returnList = new ArrayList<>();
        if (startDate.getTime() > endDate.getTime()) {
            return null;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(startDate);
        c2.setTime(endDate);
        Calendar curC1 = Calendar.getInstance();
        Calendar curC2 = null;
        curC1.setTime(startDate);
        while (curC2 == null || curC2.before(c2)) {
            if (curC2 == null) {
                curC2 = Calendar.getInstance();
                curC2.setTime(startDate);
                curC2.add(calendarUnit, dateNum);
            } else {
                curC1.add(calendarUnit, dateNum);
                curC2.add(calendarUnit, dateNum);
            }
            returnList.add(new Date[]{curC1.getTime(), curC2.getTime()});
        }
        //设置最后一个区间的截至日期为endDate
        returnList.get(returnList.size() - 1)[1] = endDate;
        return returnList;
    }
    /**
     * 获取开始时间结束时间按照 日期单位 形成多个日期区间
     * 分割出来的时间区间以
     * 第一个区间开始时间为传入开始时间
     * 最后一个区间结束时间为传入结束时间
     * @param startDate
     * @param endDate
     * @param unit 1:代表按日;2:代表按周;3:代表按月
     * @return
     */

    public static List<Date[]> rangeDate(Date startDate, Date endDate, int unit){
        List<Date[]> returnList=new ArrayList<>();
        LocalDateTime ldt1= LocalDateTime.ofInstant(startDate.toInstant(),ZoneId.of("+8"));
        LocalDateTime ldt2= LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.of("+8"));
        switch (unit){
            case 1:{
                LocalDateTime start= ldt1.with(ChronoField.SECOND_OF_DAY,0);
                LocalDateTime end= ldt1.with(ChronoField.SECOND_OF_DAY, ChronoUnit.DAYS.getDuration().getSeconds()-1);
                while(true){
                    returnList.add(new Date[]{Date.from(start.toInstant(ZoneOffset.of("+8"))),Date.from(end.toInstant(ZoneOffset.of("+8")))});
                    if(!ldt2.isBefore(start)&&!ldt2.isAfter(end)){
                        break;
                    }else{
                        start=start.plusDays(1);
                        end=end.plusDays(1);
                    }
                }
                break;
            }
            case 2:{
                int dayOfWeek=ldt1.get(ChronoField.DAY_OF_WEEK);
                LocalDateTime start= ldt1.plusDays(1-dayOfWeek).with(ChronoField.SECOND_OF_DAY,0);
                LocalDateTime end= ldt1.plusDays(7-dayOfWeek).with(ChronoField.SECOND_OF_DAY, ChronoUnit.DAYS.getDuration().getSeconds()-1);
                while(true){
                    returnList.add(new Date[]{Date.from(start.toInstant(ZoneOffset.of("+8"))),Date.from(end.toInstant(ZoneOffset.of("+8")))});
                    if(!ldt2.isBefore(start)&&!ldt2.isAfter(end)){
                        break;
                    }else{
                        start=start.plusWeeks(1);
                        end=end.plusWeeks(1);
                    }
                }
                if(returnList.size()>0){
                    Date[] firstEle=returnList.get(0);
                    Date[] lastEle=returnList.get(returnList.size()-1);
                    firstEle[0]=Date.from(ldt1.with(ChronoField.SECOND_OF_DAY,0).toInstant(ZoneOffset.of("+8")));
                    lastEle[1]=Date.from(ldt2.with(ChronoField.SECOND_OF_DAY,0).toInstant(ZoneOffset.of("+8")));
                }
                break;
            }
            case 3:{
                LocalDateTime temp=ldt1;
                while(true) {
                    int dayOfMonth = temp.get(ChronoField.DAY_OF_MONTH);
                    int max = temp.getMonth().maxLength();
                    LocalDateTime start = temp.plusDays(1 - dayOfMonth).with(ChronoField.SECOND_OF_DAY, 0);
                    LocalDateTime end = temp.plusDays(max - dayOfMonth).with(ChronoField.SECOND_OF_DAY, ChronoUnit.DAYS.getDuration().getSeconds() - 1);
                    returnList.add(new Date[]{Date.from(start.toInstant(ZoneOffset.of("+8"))),Date.from(end.toInstant(ZoneOffset.of("+8")))});
                    if(!ldt2.isBefore(start)&&!ldt2.isAfter(end)){
                        break;
                    }else{
                        temp=temp.plusMonths(1);
                    }
                }
                if(returnList.size()>0){
                    Date[] firstEle=returnList.get(0);
                    Date[] lastEle=returnList.get(returnList.size()-1);
                    firstEle[0]=Date.from(ldt1.with(ChronoField.SECOND_OF_DAY,0).toInstant(ZoneOffset.of("+8")));
                    lastEle[1]=Date.from(ldt2.with(ChronoField.SECOND_OF_DAY,0).toInstant(ZoneOffset.of("+8")));
                }
                break;
            }
        }
        return returnList;
    }
    /**
     * 计算两个时间相差多少日期单位(不足一个日期单位的的按一个日期单位算)
     *
     * @param d1 开始时间
     * @param d2 结束时间
     * @return 相差日期单位数
     */
    public static int getDiff(Date d1, Date d2, int calendarUnit) {
        double diff;
        switch (calendarUnit) {
            case Calendar.DATE: {
                diff = 1000 * 60 * 60 * 24;
                break;
            }
            case Calendar.HOUR_OF_DAY: {
                diff = 1000 * 60 * 60;
                break;
            }
            case Calendar.MINUTE: {
                diff = 1000 * 60;
                break;
            }
            case Calendar.SECOND: {
                diff = 1000;
                break;
            }
            default: {
                throw BaseRuntimeException.getException("[DateUtil.getDiff],Calendar Unit Not Support!");
            }
        }
        Long begin = d1.getTime();
        Long end = d2.getTime();
        Double res = (end - begin) / diff;
        return (int) Math.ceil(res);
    }
    /**
     * 会改变参数值
     * 格式化日期参数开始日期和结束日期
     * 格式规则为:
     * 开始日期去掉时分秒
     * 结束日期设置为当天 23:59:59
     *
     * @param startDate
     * @param endDate
     */
    public static void formatDateParam(Date startDate, Date endDate) {
        if (startDate != null) {
            startDate.setTime(getFloorDate(startDate, Calendar.DATE).getTime());
        }
        if (endDate != null) {
            Date tempDate = getCeilDate(endDate, Calendar.DATE);
            Calendar endC = Calendar.getInstance();
            endC.setTime(tempDate);
            endC.add(Calendar.SECOND, -1);
            endDate.setTime(endC.getTimeInMillis());
        }
    }
    /**
     * 判断两个日期是否相等
     *
     * @param d1 date1
     * @param d2 date2
     * @param calendarUnit 对比的最小日期单位
     * @return boolean
     */
    public static boolean isEqual(Date d1, Date d2, int calendarUnit) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        for (int i = DATE_UNIT_ARR.length - 1; i >= 0; i--) {
            if (calendarUnit >= DATE_UNIT_ARR[i]) {
                int v1 = c1.get(DATE_UNIT_ARR[i]);
                int v2 = c2.get(DATE_UNIT_ARR[i]);
                if (v1 != v2) {
                    return false;
                }
            } else {
                break;
            }
        }
        return true;
    }
    /**
     * 获取当天初始时间
     *
     * @param date 时间
     * @return 初始时间 (yyyy-MM-dd 00:00:00)
     */
    public static Date getInitialTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String dateStr = dateFormat.format(date);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取当天最后时间
     *
     * @param date 时间
     * @return 最后时间 (yyyy-MM-dd 23:59:59)
     */
    public static Date getTerminalTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        dateStr = dateStr + " 23:59:59";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String date2Str(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    /**
     * 计算两个时间间隔多少秒
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long intervalTime(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        Long c = ((a - b) / 1000);
        return c;
    }
    /**
     * 检测一个日期是否在 小时之内,支持跨天的小时
     *
     * @param time
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean checkDateIn(Date time, Date startDate, Date endDate) {
        if (startDate == null || endDate == null || time == null) {
            return true;
        }
        return time.before(endDate) && time.after(startDate);
    }
    /**
     * 检测一个日期是否在 时分秒 之内,支持跨天的小时
     *
     * @param time
     * @param startHms
     * @param endHms
     * @return
     */
    public static boolean checkHmsIn(Date time, String startHms, String endHms) {
        if (startHms == null || endHms == null || time == null) {
            return true;
        }
        LocalTime startTime = LocalTime.of(
                Integer.valueOf(startHms.substring(0, 2)),
                Integer.valueOf(startHms.substring(2, 4)),
                Integer.valueOf(startHms.substring(4, 6))
        );
        LocalTime endTime = LocalTime.of(
                Integer.valueOf(endHms.substring(0, 2)),
                Integer.valueOf(endHms.substring(2, 4)),
                Integer.valueOf(endHms.substring(4, 6))
        );
        LocalTime curTime = LocalDateTime.ofInstant(time.toInstant(), ZoneId.of("+8")).toLocalTime();
        if (endTime.isAfter(startTime)) {
            return startTime.isBefore(curTime) && endTime.isAfter(curTime);
        } else {
            return (startTime.isBefore(curTime) && LocalTime.MAX.isAfter(curTime)) || (LocalTime.MIN.isBefore(curTime) && endTime.isAfter(curTime));
        }
    }
    /**
     * 功能描述：格式化日期
     *
     * @param dateStr 字符型日期：YYYY/MM/DD 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd");
    }
    /**
     * 功能描述：格式化日期
     *
     * @param dateStr 字符型日期
     * @param format 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        try {
            dateFormat = new SimpleDateFormat(format);
            String dt = dateStr.replaceAll("-", "/");
            if ((!dt.equals("")) && (dt.length() < format.length())) {
                dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
                        "0");
            }
            date = (Date) dateFormat.parse(dt);
        } catch (Exception e) {
            return null;
        }
        return date;
    }
    public static Date stringParseDate(String date) throws ParseException {
        //获取的值为"19570323"
        //1、定义转换格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
        //2、调用formatter2.parse(),将"19570323"转化为date类型 输出为：Sat Mar 23 00:00:00 GMT+08:00 1957
        Date parseDate = formatter2.parse(date);
        return parseDate;
    }
}
