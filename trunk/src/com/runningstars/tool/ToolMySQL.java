package com.runningstars.tool;

import java.util.Date;

import com.cameleon.common.tool.ToolDatetime;

public class ToolMySQL {

	/**
	 * Format a Date to a MySQL String representation
	 * @param date Date to format
	 * @return MySQL Date String representation
	 */
	public static String toFormatedDate(Date date) {
		return ToolDatetime.toMySqlDate(date);
	}

	/**
	 * Format a Time in milliseconds to a MySQL String representation
	 * @param time Time (in milliseconds)
	 * @return MySQL Date String representation
	 */
	public static String toFormatedDate(long time) {
		return ToolDatetime.toMySqlDate(new Date(time));
	}
}
