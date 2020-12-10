package com.fa.ebs.delivery.util;

import com.fa.core.exception.BusinessException;
import com.fa.core.exception.ErrCodeEnum;
import com.fa.ebs.pay.payenum.PayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description: 时间类型转换
 * @author: 胡斌
 * @date: 2020年4月1日
 */
@Slf4j
public class DateTimeUtils {

	/**
	 * @Title ObjectToDate
	 * @Description 将Long、String(yyyy-MM-dd HH:mm:ss格式或yyyy-MM-dd格式)转成时间类型
	 * @param time
	 * @return Date类型的时间
	 * @throws Exception
	 * @return Date
	 * @author 胡斌
	 * @date: 2020年4月1日
	 */
	public static Date ObjectToDate(Object time) throws Exception {
		Date date = null;
		if (time == null) {
			return null;
		} else if (time instanceof Long) {
			long l = (long)time;
			if (l<=0) {
				return null;
			}
			date = new Date((Long) time);
		} else if (time instanceof Date) {
			date = (Date) time;
		} else if (time instanceof String) {
			try {
				Long t = Long.parseLong((String) time);
				date = new Date(t);
			} catch (Exception e) {
				if (((String) time).length() > 10) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = sdf1.parse((String) time);
				} else {
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					date = sdf2.parse((String) time);
				}
			}
		} else if (time instanceof Integer) {
			if ((Integer) time > 0) {
				date = new Date((Integer) time);
			}
		} else {
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "时间类型数据错误");
		}
		return date;
	}

	/**
	 * @Title ObjectToLong
	 * @Description 将Date、String(yyyy-MM-dd HH:mm:ss格式或yyyy-MM-dd格式)转成Long
	 * @param time
	 * @return Long类型时间戳
	 * @author 胡斌
	 * @throws Exception
	 * @date: 2020年4月1日
	 */
	public static Long ObjectToLong(Object time) throws Exception {
		Long date = null;
		if (time == null) {
			return null;
		} else if (time instanceof Long) {
			date = (Long) time;
		} else if (time instanceof Date) {
			date = ((Date) time).getTime();
		} else if (time instanceof String) {
			try {
				date = Long.parseLong((String) time);
			} catch (Exception e) {
				if (((String) time).length() > 10) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = sdf1.parse((String) time).getTime();
				} else {
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					date = sdf2.parse((String) time).getTime();
				}
			}
		} else if (time instanceof Integer) {
			if ((Integer) time > 0) {
				date = (Long) time;
			}
		} else {
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "请输入正确时间类型");
		}
		return date;
	}

	/**
	 * @Title ObjectToString
	 * @Description 将Date、Long转成String
	 * @param time
	 * @param type 返回String类型的格式：time:yyyy-MM-dd HH:mm:ss date:yyyy-MM-dd
	 * @return String
	 * @author 胡斌
	 * @throws BusinessException
	 * @date: 2020年4月1日
	 */
	public static String ObjectToString(Object time, String type) throws BusinessException {
		String date = null;
		if (time == null) {
			return null;
		} else if (type.equals("time")) {
			date = "yyyy-MM-dd HH:mm:ss";
		} else {
			date = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(date);
		if (time instanceof Long) {
			date = sdf.format(new Date((Long) time));
		} else if (time instanceof Date) {
			date = sdf.format((Date) time);
		} else if (time instanceof String) {
			try {
				Long t = Long.parseLong((String) time);
				date = sdf.format(new Date(t));
			} catch (Exception e) {
				date = (String) time;
			}
		} else if (time instanceof Integer) {
			if ((Integer) time > 0) {
				date = sdf.format(new Date((Long) time));
			}
		} else {
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "请输入正确时间类型");
		}
		return date;
	}

	/**
	 * @Title getDate
	 * @Description 获取与当前时间相差某个时间段的日期
	 * @param year  年
	 * @param month 月
	 * @param day   日
	 * @param type  time:yyyy-MM-dd HH:mm:ss date:yyyy-MM-dd
	 * @throws ParseException
	 * @return Date
	 * @author 胡斌
	 * @date: 2020年4月11日
	 */
	public static Date getDate(int year, int month, int day, String type) throws ParseException {
		String format = "yyyy-MM-dd";
		if (type.equals("time")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, day);
		date = calendar.getTime();
		date = df.parse(df.format(date));
		return date;
	}

	/**
	 * @Title getTime
	 * @Description 获取与当前时间相差某个时间段的时间
	 * @param hour   时
	 * @param minute 分
	 * @param second 秒
	 * @param type   time:yyyy-MM-dd HH:mm:ss date:yyyy-MM-dd
	 * @throws ParseException
	 * @return Date
	 * @author 胡斌
	 * @date: 2020年4月11日
	 */
	public static Date getTime(int hour, int minute, int second, String type) throws ParseException {
		String format = "yyyy-MM-dd";
		if (type.equals("time")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minute);
		calendar.add(Calendar.SECOND, second);
		date = calendar.getTime();
		date = df.parse(df.format(date));
		return date;
	}


	/**
	 * 
	 * @Title 通过已经格式化的时间类型的字符串和格式获取到对应的时间Date
	 * @Description
	 * @param formatDate 格式化完成的时间字符串
	 * @param format     格式
	 * @return
	 * @throws ParseException
	 * @return Date
	 * @author 伍成林
	 * @date: 2020年4月21日
	 */
	public static Date getDate(String formatDate, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);

		return df.parse(formatDate);
	}

	/**
	 * @Title getDateFromTime
	 * @Description 去除时分秒，只保留日期
	 * @param time
	 * @throws ParseException
	 * @throws BusinessException
	 * @return Date
	 * @author 胡斌
	 * @date: 2020年4月20日
	 */
	public static Date getDateFromTime(Date time) throws ParseException, BusinessException {
		if (time == null) {
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "传入时间为null");
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		time = df.parse(df.format(time));
		return time;
	}

	/**
	 * @Title compareTo
	 * @Description 判断两个时间大小(date1-date2)
	 * @param date1
	 * @param date2
	 * @param timeType  对比单位(year,month,day,hour,minutes,second,millisecond)
	 * @return long 时间间隔数
	 * @author 胡斌
	 * @throws Exception
	 * @date: 2020年4月14日
	 */
	public static long compareTo(Date date1, Date date2, TimeTypes timeType) throws Exception {
		if (date1 == null && date2 == null) {
			return 0;
		} else if ((date1 == null && date2 != null) || (date1 != null && date2 == null)) {
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "传入时间不同时为null，无法进行比较");
		}
		// 获取两个时间代表的毫秒数
		long d1 = ObjectToLong(date1);
		long d2 = ObjectToLong(date2);
		return (d1 - d2) / timeType.getTime();
	}

	/**
	 * 
	 * @Title 通过毫秒数判断两个时间相差几天
	 * @Description TODO
	 * @param date1
	 * @param date2
	 * @return
	 * @return int
	 * @author 伍成林
	 * @throws BusinessException
	 * @date: 2020年4月21日
	 */
	public static int differentDaysByMillisecond(Date smallDate, Date bigDate) throws BusinessException {
		if (smallDate == null || bigDate == null) {
			throw new BusinessException("时间不能为null");
		}
		int days = (int) ((bigDate.getTime() - smallDate.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	/**
	 * 获取 前天的时间
	 * 
	 * @Title getYesterday
	 * @Description 如果传递进来的String为null则返回格式为"yyyy-MM-dd"的时间格式否则则按按照需要的支付方式的返回
	 * @return
	 * @return String
	 * @author 伍成林
	 * @date: 2020年3月19日
	 */
	public static String getBeforeYesterday(String payMethod) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.DATE, -2);
		Date date = ca.getTime();

		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	/**
	 * 
	 * @Title 获取前天的日期
	 * @Description TODO
	 * @param payMethod
	 * @return
	 * @throws ParseException
	 * @return Date
	 * @author 伍成林
	 * @date: 2020年4月21日
	 */
	public static Date getBeforeYesterday() throws ParseException {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.DATE, -2);
		Date date = ca.getTime();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = simpleDateFormat.format(date);
		return simpleDateFormat.parse(format);
	}

	/**
	 * 
	 * @Title getNextDay
	 * @Description 获取下一天的日期
	 * @param date
	 * @return
	 * @return String
	 * @author 伍成林
	 * @date: 2020年4月21日
	 */
	public static String getNextDay(Date date) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(date); // 设置时间为当前时间
		ca.add(Calendar.DATE, 1);
		Date nextDate = ca.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(nextDate);
	}

	/**
	 * 
	 * @Title 根据不同的支付方式获取不同对账单的格式方便对账
	 * @Description TODO
	 * @param date
	 * @param payMethod
	 * @return
	 * @return String
	 * @author 伍成林
	 * @date: 2020年4月21日
	 */
	public static String getNextDay(Date date, String payMethod) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(date); // 设置时间为当前时间
		ca.add(Calendar.DATE, 1);
		Date nextDate = ca.getTime();
		SimpleDateFormat simpleDateFormat;
		if (payMethod.equals(PayTypeEnum.ALIPAY.getValue())) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		}
		return simpleDateFormat.format(nextDate);
	}

//	public static void main(String[] args) throws ParseException {
//		test(10,"2020-04-25");
//	}
//	
//	private static void test(int diffDay,String formatDate) throws ParseException {
//		log.info("获取对账单的时间为{}",formatDate);
//		if (diffDay == 0) {
//			return;
//		}
//		diffDay--;
//		test(diffDay,DateTimeUtils.getNextDay(DateTimeUtils.getDate(formatDate, "yyyy-MM-dd")));
//	}

	/**
	 * @Title Date1BeforeDate2
	 * @Description Date1是否是Date2之前的时间
	 * @param date1
	 * @param date2
	 * @param flag  时间为null是否抛异常
	 * @return boolean
	 * @author 胡斌
	 * @throws BusinessException
	 * @date: 2020年4月20日
	 */
	public static boolean Date1BeforeDate2(Date date1, Date date2, boolean flag) throws BusinessException {
		boolean result = true;
		if (date1 == null || date2 == null) {
			if (flag) {
				throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "传入时间为null!");
			}
			result = false;
		} else {
			if (date1.getTime() >= date2.getTime()) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * @Title dateBetween
	 * @Description date是否是该时间段的时间
	 * @param date
	 * @param beginTime
	 * @param endTime
	 * @param flag      时间为null是否抛异常
	 * @return boolean
	 * @author 胡斌
	 * @throws BusinessException
	 * @date: 2020年4月20日
	 */
	public static boolean dateBetween(Date date, Date beginTime, Date endTime, boolean flag) throws BusinessException {
		boolean result = true;
		if (date == null || beginTime == null || endTime == null) {
			if (flag) {
				throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(), "传入时间为null!");
			}
			result = false;
		} else {
			long time = date.getTime();
			if (time < beginTime.getTime() || time > endTime.getTime()) {
				result = false;
			}
		}
		return result;
	}
	/**
	 * 获得过去多久时间，多少分，多少秒
	 * @Title getTimeStr
	 * @Description:
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return String 返回过去了多久
	 * @throws
	 * @author 伍成林
	 * @date 2020/6/16
	 */
	public static String getTimeStr(Date begin, Date end) {
		long date = end.getTime() - begin.getTime();
		long dayf = 24 * 60 * 60 * 1000;
		long houf = 60 * 60 * 1000;
		long minf = 60 * 1000;
		long day = 0;
		long hour = 0;
		long min = 0;
		//计算天数
		day = date / dayf;
		//计算分钟
		hour = date % dayf / houf;
		//计算分钟
		min = date % dayf % houf / minf;
		StringBuilder str = new StringBuilder();
		if (day != 0) {
			str.append(day + "天");
		}
		if (hour != 0) {
			str.append(hour + "小时");
		}

		str.append(min + "分钟");
		return str.toString();
	}

	/**
	 * 获得过去多久时间，多少分，多少秒
	 * @Title getTimeStr
	 * @Description:
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @return String 返回过去了多久
	 * @throws
	 * @author 伍成林
	 * @date 2020/6/16
	 */
	public static String getTimeStrHour(Date begin, Date end) {
		long date = end.getTime() - begin.getTime();
		long dayf = 24 * 60 * 60 * 1000;
		long houf = 60 * 60 * 1000;
		long minf = 60 * 1000;
		long day = 0;
		long hour = 0;
		long min = 0;
		//计算天数
		day = date / dayf;
		//计算分钟
		hour = date % dayf / houf;
		//计算分钟
		min = date % dayf % houf / minf;
		StringBuilder str = new StringBuilder();
		if (day != 0) {
			str.append(day + "天");
		}
		if (hour != 0) {
			str.append(hour + "小时");
		}
		if (StringUtils.isBlank(str.toString())){
			str.append(min + "分");
		}
		return str.toString();
	}
	/**
	 * 根据传进来的年月字符串算出当月1号的0点和下月的0点
	 * @Title getOneMonth
	 * @Description:
	 * @param time 年月字符串
	 * @return Map<String,Date>
	 * @throws
	 * @author 伍成林
	 * @date 2020/6/17
	 */
	public static Map<String,Date> getOneMonth(String time) throws BusinessException, ParseException {
		if (StringUtils.isBlank(time)){
			throw new BusinessException(ErrCodeEnum.DATACHECK.getCode(),"时间为空");
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date month = simpleDateFormat.parse(time);
		String begin = time+"-01 00:00:00";

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.MONTH, 1);

		Date end = new Date(calendar.getTimeInMillis());
		Date beginDate = timeFormat.parse(begin);

		Map<String,Date> map = new HashMap<>();
		map.put("begin",beginDate);
		map.put("end",end);
		return map;
	}
	/**
	 * 将基础时间加上后面的小时数并将其格式化为中文
	 * @Title getChineseDateAddTogether
	 * @Description:
	 * @param basicDate 基础的时间
	 * @param hours 时长
	 * @return String
	 * @throws        
	 * @author 伍成林 
	 * @date 2020/6/30
	 */
	public static String getChineseDateAddTogether(Date basicDate,Integer hours){
		long orderCreate = basicDate.getTime();
		long hour = hours*60 * 60 * 1000;
		Date resultDate = new Date(orderCreate+hour);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return simpleDateFormat.format(resultDate);
	}

	/**
	 * 将基础时间加上后面的小时数并将其格式化为中文
	 * @Title getChineseDateAddTogether
	 * @Description:
	 * @param basicDate 基础的时间
	 * @return String
	 * @throws
	 * @author 伍成林
	 * @date 2020/6/30
	 */
	public static String getChineseDateAddTogether(Date basicDate){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		return simpleDateFormat.format(basicDate);
	}

	/**
	 * 获取与今天相差多少天的天数，diffDays为正则是当前时间加上相差天数+1的0点，为负则是当前时间之前的0点
	 * @Title getAgoDateBeforeNow
	 * @Description:
	 * @param nowDate 当前的时间
	 * @param diffDays 相差的天数
	 * @return Date
	 * @throws        
	 * @author 伍成林 
	 * @date 2020/7/9
	 */
	public static Date getAgoDateBeforeNow(Date nowDate,int diffDays){
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(nowDate); // 设置时间为当前时间
		ca.add(Calendar.DATE, diffDays);
		ca.set(Calendar.HOUR_OF_DAY,0 );
		// 分
		ca.set(Calendar.MINUTE, 0);
		// 秒
		ca.set(Calendar.SECOND, 0);
		// 毫秒
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
	/**
	 * 获取与当前时间的相差天数的最后一秒
	 * @Title getAgoDateBeforeNowLastSecond
	 * @Description:
	 * @param nowDate 当前时间
	 * @param diffDays 相差天数
	 * @return Date
	 * @throws        
	 * @author 伍成林 
	 * @date 2020/7/22
	 */
	public static Date getAgoDateBeforeNowLastSecond(Date nowDate,int diffDays){
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(nowDate); // 设置时间为当前时间
		ca.add(Calendar.DATE, diffDays);
		ca.set(Calendar.HOUR_OF_DAY,23 );
		// 分
		ca.set(Calendar.MINUTE, 59);
		// 秒
		ca.set(Calendar.SECOND, 59);
		// 毫秒
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

    public static String getMonthStr(Date beginTime) {
		return new SimpleDateFormat("yyyy-MM").format(beginTime);
	}
	/**
	 *
	 * @Title getBirthday2IdCard
	 * @Description: 通过身份证号返回生日
	 * @param idCard 身份证号
	 * @return Date
	 * @throws        
	 * @author 伍成林 
	 * @date 2020年09月24日
	 */
	public static Date getBirthday2IdCard(String idCard) throws ParseException {
		if (StringUtils.isBlank(idCard)){
			return null;
		}
		// 匹配身份证的正则表达式
		String regx = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
		if (!Pattern.matches(regx,idCard)){
			return null;
		}
		String str = idCard.substring(6,14);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.parse(str);
	}
	/**
	 *
	 * @Title getNowDateHold2Hour
	 * @Description: 获取当前时间保留到小时
	 * @return Date
	 * @throws ParseException
	 * @author 伍成林 
	 * @date 2020年09月29日
	 */
	public static Date getNowDateHold2Hour(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
		String formatStr = format.format(date);
		return format.parse(formatStr);
	}
	/**
	 *
	 * @Title addOrReduceDate
	 * @Description: 获取当前时间相差小时数的时间如果为负则是减
	 * @param basicDate 基础的时间
	 * @param diffHour 相差的小时数
	 * @return Date
	 * @author 伍成林
	 * @date 2020年09月29日
	 */
	public static Date addOrReduceDate(Date basicDate,int diffHour){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(basicDate);
		calendar.set(Calendar.HOUR,diffHour);
		return calendar.getTime();
	}

	/**
	 *
	 * @Title getLastMonthFirstDay
	 * @Description: 获取上个月的第一天
	 * @return Date
	 * @author 伍成林
	 * @date 2020年10月27日
	 */
	public static Date getLastMonthFirstDay(){
		Calendar cal=Calendar.getInstance();
		//获取当前时间上一个月
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY,0 );
		// 分
		cal.set(Calendar.MINUTE, 0);
		// 秒
		cal.set(Calendar.SECOND, 0);
		// 毫秒
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 *
	 * @Title getLastMonthLastDay
	 * @Description: 获取上个月的最后一天
	 * @return Date
	 * @author 伍成林
	 * @date 2020年10月27日
	 */
	public static Date getLastMonthLastDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY,23 );
		// 分
		calendar.set(Calendar.MINUTE, 59);
		// 秒
		calendar.set(Calendar.SECOND, 59);
		// 毫秒
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}

