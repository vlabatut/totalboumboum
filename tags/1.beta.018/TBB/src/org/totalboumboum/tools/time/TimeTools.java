package org.totalboumboum.tools.time;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TimeTools
{
	public static Date dateXmlToJava(String xmlStr)
	{	// 2008-12-05T09:30:10.5
		String tab[] = xmlStr.split("[-T:.]");
		int year = Integer.parseInt(tab[0]);
		int month = Integer.parseInt(tab[1])-1;
		int day = Integer.parseInt(tab[2]);
		int hourOfDay = Integer.parseInt(tab[3]);
		int minute = Integer.parseInt(tab[4]);
		int second = Integer.parseInt(tab[5]);		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(year,month,day,hourOfDay,minute,second);
		Date result = calendar.getTime();
		return result;
	}

	public static String dateJavaToXml(Date date)
	{	// 2008-12-05T09:30:10.5
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);
		String result = year+"-"+nf.format(month)+"-"+nf.format(day)+"T"+nf.format(hourOfDay)+":"+nf.format(minute)+":"+nf.format(second);
		return result;
	}

	public static String formatTime(long time, TimeUnit biggest, TimeUnit smallest, boolean letter)
	{	String result = null;
		if(biggest.compareTo(smallest)>0)
		{	result = "";
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(2);
			if(biggest==TimeUnit.HOUR)
			{	String hours = Long.toString(time/3600000);
				time = time%3600000;			
				result = result + hours+TimeUnit.HOUR.getText(letter);
			}
			if(biggest==TimeUnit.MINUTE || smallest==TimeUnit.MINUTE || (biggest.compareTo(TimeUnit.MINUTE)>0 && smallest.compareTo(TimeUnit.MINUTE)<0))
			{	String minutes = nf.format(time/60000);
				time = time%60000;
				result = result + minutes;
				if(biggest==TimeUnit.MINUTE || smallest!=TimeUnit.MINUTE)
					result = result+TimeUnit.MINUTE.getText(letter);
			}
			if(biggest==TimeUnit.SECOND || smallest==TimeUnit.SECOND || (biggest.compareTo(TimeUnit.SECOND)>0 && smallest.compareTo(TimeUnit.SECOND)<0))
			{	String seconds = nf.format(time/1000);
				time = time%1000;
				result = result + seconds;
				if(biggest==TimeUnit.SECOND || smallest!=TimeUnit.SECOND)
					result = result+TimeUnit.SECOND.getText(letter);
			}
			if(smallest==TimeUnit.MILLISECOND)
			{	nf.setMinimumIntegerDigits(3);
				String milliseconds = nf.format(time);
				result = result + milliseconds;
				if(biggest==TimeUnit.MILLISECOND)
					result = result+TimeUnit.MILLISECOND.getText(letter);
			}
		}
		return result;
	}
}
