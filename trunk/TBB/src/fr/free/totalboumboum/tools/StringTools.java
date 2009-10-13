package fr.free.totalboumboum.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Set;

import fr.free.totalboumboum.game.GameConstants;

public class StringTools
{
	public static String formatTimeWithHours(long time)
	{	String result;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		String hours = nf.format(time/3600000);	time = time%3600000;
		String minutes = nf.format(time/60000);	time = time%60000;
		String seconds = nf.format(time/1000);	time = time%1000;
		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		String milliseconds = nf.format(time);
		result = hours+"h"+minutes+"m"+seconds+"s"+milliseconds+"ms";
		return result;
	}

	public static String formatTimeWithSeconds(long time)
	{	String result;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		String seconds = nf.format(time/1000);	time = time%1000;
		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		String milliseconds = nf.format(time);
		result = seconds+"''"+milliseconds;
		return result;
	}
	
	public static String formatAllowedPlayerNumbers(Set<Integer> playerNumbers)
	{	StringBuffer temp = new StringBuffer();
		if(playerNumbers.size()>0)
		{	int value = playerNumbers.iterator().next();
			temp.append(value);
			boolean serie = true;
			int first = value;
			for(int index=value+1;index<=GameConstants.MAX_PROFILES_COUNT+1;index++)
			{	if(playerNumbers.contains(index))
				{	if(!serie)
					{	serie = true;
						first = index;
						temp.append(";"+index);
					}					
				}
				else
				{	if(serie)
					{	serie = false;
						if(index-1!=first)
							temp.append("-"+(index-1));
					}
				}				
			}
		}
		else
			temp.append(0);
		return temp.toString();
	}
	
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
}
