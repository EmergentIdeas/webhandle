package com.emergentideas.utils;

import java.util.Calendar;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateUtilsTest {

	@Test
	public void testDateFormatting() throws Exception {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.YEAR, 2010);
		c.set(Calendar.DAY_OF_MONTH, 16);
		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 15);
		
		assertEquals("2010-01-16", DateUtils.html5DateFormat().format(c.getTime()));
		assertEquals("2010-01-16T00:15", DateUtils.html5DateTimeLocalFormat().format(c.getTime()));
		assertEquals("2010-01", DateUtils.html5MonthFormat().format(c.getTime()));
		assertEquals("2010-W03", DateUtils.html5WeekFormat().format(c.getTime()));
		assertEquals("00:15", DateUtils.html5TimeFormat().format(c.getTime()));
	}
}
