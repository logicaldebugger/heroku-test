package com.vieztech.utils;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	public static Date getDateTimeZone(Date date, String requiretimezone) throws ParseException {
		Date d = new Date();

		long currenttime = d.getTime() - (TimeZone.getDefault().getRawOffset() - getOffSet(requiretimezone));
		long expectedtime = date.getTime();

		long diff = expectedtime - currenttime;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(d.getTime() + diff);
		return c.getTime();
	}

	public static long getOffSet(String offset) {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of(offset));
		long msFromEpochGmt = new Date().getTime();

		return tz.getOffset(msFromEpochGmt);
	}
}
