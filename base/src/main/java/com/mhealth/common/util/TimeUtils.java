package com.mhealth.common.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class TimeUtils {
	
	private static SimpleDateFormat timeFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date getTime(String str){
		Date date=null;
		try {
			date=timeFormat.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}
	public static Date getDate(String str){
		Date date=null;
		try {
			date=dateFormat.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return date;
	}
	public static String getDateString(Date date){
		if(date==null){
			return null;
		}
		String dateStr=null;
		dateStr=dateFormat.format(date);
		return dateStr;
	}
	public static String getTimeString(Date date){
		if(date==null){
			return null;
		}
		String timeStr=null;
		timeStr=timeFormat.format(date);
		return timeStr;
	}
	
	public static Date getFirstDayOfWeek(Date date){
		
		Calendar cal=Calendar.getInstance();
		
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//将日期设置为周一 
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		return cal.getTime();
	}
	public static Date getLastDayOfWeek(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//将日期设置为周一 
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		return cal.getTime();
	}
	
	public static Date getFirstDayOfMonth(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH,1);//本月第一天 
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		
		return cal.getTime();
	}
	public static Date getLastDayOfMonth(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		
		cal.set(Calendar.DAY_OF_MONTH,1);//本月第一天 
		
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		
		return cal.getTime();
	}
	public static Date getNextDay(Date date){
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE,cal.get(Calendar.DATE)+1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		
		return cal.getTime();
		
	}
	public static Date setTimeLate(Date date, int i) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+i);
		return cal.getTime();
	}
	public static boolean isTheSameDay(Date lastDate) {//是否为当日
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date today =TimeUtils.formatTime(cal.getTime());
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(lastDate);
		cal2.set(Calendar.HOUR, 0);
		cal2.set(Calendar.MINUTE, 0);
		cal2.set(Calendar.SECOND, 0);
		Date lastDay = TimeUtils.formatTime(cal2.getTime());
		return today.compareTo(lastDay)==0?true:false;
	}
	public static Date formatTime(Date date) {
		String str=timeFormat.format(date);
		Date md=null;
		try {
			md= timeFormat.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return md;
	}
   public static Date getLastWeek(){//返回的时间为过去6天
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(new Date());
	   cal.set(Calendar.DATE,cal.get(Calendar.DATE)-6);
	   cal.set(Calendar.HOUR, 0);
	   cal.set(Calendar.MINUTE, 0);
	   cal.set(Calendar.SECOND, 0);
	   return cal.getTime();
   }
   public static Date getLastMonth(){//返回的时间为过去29天
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(new Date());
	   cal.set(Calendar.DATE,cal.get(Calendar.DATE)-29);
	   cal.set(Calendar.HOUR, 0);
	   cal.set(Calendar.MINUTE, 0);
	   cal.set(Calendar.SECOND, 0);
	   return cal.getTime();
   }
	

}
