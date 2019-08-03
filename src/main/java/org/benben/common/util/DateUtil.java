package org.benben.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateUtil {
	public static void main(String[] args) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date starte = format.parse("20190602");
		System.out.println("两个日期相差天数：" + getBetweenNowDays(starte,new Date()));
	}
	/**
	 * 当前时间和传来的时间比较
	 *
	 * @param endTime 传来的时间
	 * @return 相差天数
	 */
	public static Long getBetweenNowDays(Date startTime, Date endTime) {
		long dqTime = endTime.getTime();
		long nowTime = startTime.getTime();
		long dqcday = (long)((dqTime - nowTime) / (1000 * 60 * 60 *24) + 0.5);

		System.out.println("相差天数："+dqcday);
		return  dqcday;
	}

	/**
	 * 时间戳转日期
	 * @param endTime 传来的时间戳
	 * @return 日期格式
	 */
//	public static int getBetweenNowDays(long endTime) {
//		return getBetweenNowDays(new Timestamp(endTime));
//	}
}
