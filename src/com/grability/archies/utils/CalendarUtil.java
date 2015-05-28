package com.grability.archies.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ParseException;

public class CalendarUtil {
	
	public static String getDateFormated(Calendar calendar, String template) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(template,
				Locale.US);
		
		return dateFormat.format(calendar.getTime()).toString();
		
	}
	
public static String getDateFormated(Date date, String template) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(template,
				Locale.US);
		
		return dateFormat.format(date).toString();
		
	}

public static Calendar getCalendarFromString(String dateString, String template) {
	
	if (dateString!= null) {
		SimpleDateFormat format = new SimpleDateFormat(template,
				Locale.US);

		try {

		    Date date = format.parse(dateString);
		    return DateToCalendar(date);
		} catch (ParseException e) {
		    e.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}else{
		return null;
	}
	return null;
}

public static Calendar DateToCalendar(Date date){ 
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(date);
	  return cal;
	}
}
