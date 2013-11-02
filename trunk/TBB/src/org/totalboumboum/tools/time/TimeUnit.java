package org.totalboumboum.tools.time;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
 * Values used when formatting dates and durations.
 * 
 * @author Vincent Labatut
 */
public enum TimeUnit
{	/** ms */
	MILLISECOND("ms",""),
	/** s */
	SECOND("s","''"),
	/** min */
	MINUTE("min","'"),
	/** h */
	HOUR("h",":");

	/**
	 * New time unit.
	 * 
	 * @param letter
	 * 		SI representation of the time unit.
	 * @param symbol
	 * 		Commin representation of the time unit.
	 */
	TimeUnit(String letter, String symbol)
	{	this.letter = letter;
		this.symbol = symbol;
	}
	
	/////////////////////////////////////////////////////////////////
	// LETTER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** SI reprensentation of this time unit */
	private String letter;
	
	/**
	 * Returns the SI representation of this time unit.
	 * 
	 * @return
	 * 		SI representation of this time unit.
	 */
	public String getLetter()
	{	return letter;
	}

	/////////////////////////////////////////////////////////////////
	// SYMBOL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Usual symbolic reprensentation of this time unit */
	private String symbol;
	
	/**
	 * Returns the common representation of this time unit.
	 * 
	 * @return
	 * 		Common representation of this time unit.
	 */
	public String getSymbol()
	{	return symbol;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns a text representation of this time unit:
	 * single symbol or SI letter representation, depending
	 * on the specified parameter.	
	 * 
	 * @param letter
	 * 		If {@code true}, then the SI letters, otherwise the usual symbol.
	 * @return
	 * 		Text representation of this time unit.
	 */
	public String getText(boolean letter)
	{	String result;
		if(letter)
			result = getLetter();
		else
			result = getSymbol();
		return result;
	}
}
