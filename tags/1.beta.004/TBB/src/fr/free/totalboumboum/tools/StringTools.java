package fr.free.totalboumboum.tools;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
}
