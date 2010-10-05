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

/**
 * 
 * @author Vincent Labatut
 *
 */
public enum TimeUnit
{	MILLISECOND,
	SECOND,
	MINUTE,
	HOUR;

	public String getLetter()
	{	String result = null;
		if(this==MILLISECOND)
			result = "ms";
		else if(this==SECOND)
			result = "s";
		else if(this==MINUTE)
			result = "min";
		else if(this==HOUR)
			result = "h";
		return result;
	}

	public String getSymbol()
	{	String result = null;
		if(this==MILLISECOND)
			result = "";
		else if(this==SECOND)
			result = "''";
		else if(this==MINUTE)
			result = "'";
		else if(this==HOUR)
			result = ":";
		return result;
	}
	
	public String getText(boolean letter)
	{	String result;
		if(letter)
			result = getLetter();
		else
			result = getSymbol();
		return result;
	}
}
