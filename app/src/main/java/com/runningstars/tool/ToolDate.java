package com.runningstars.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolDate {

	private static SimpleDateFormat dfCompactedDatetime = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat dfMySql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DateFormat dfTextDefault = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL, SimpleDateFormat.MEDIUM);

	/**
	 * Format a Date to a MySQL String representation
	 * @param date Date to format
	 * @return MySQL Date String representation
	 */
	public static String toMySqlDate(Date date) {
		return dfMySql.format(date);
	}

	/**
	 * Format a Time in milliseconds to a MySQL String representation
	 * @param time Time (in milliseconds)
	 * @return MySQL Date String representation
	 */
	public static String toMySqlDate(long time) {
		return dfMySql.format(new Date(time));
	}

	/**
	 * Format a Date to a MySQL String representation
	 * @param date Date to format
	 * @return MySQL Date String representation
	 */
	public static String toTextDefault(Date date) {
		return dfTextDefault.format(date);
	}

	/**
	 * Format a Date to a compacted datetime String representation
	 * @param date Date to format
	 * @return Compacted datetime String representation
	 */
	public static String toCompactedDatetime(Date date) {
		return dfCompactedDatetime.format(date);
	}
}
