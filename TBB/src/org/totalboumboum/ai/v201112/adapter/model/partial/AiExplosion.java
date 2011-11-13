package org.totalboumboum.ai.v201112.adapter.model.partial;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
 * Représente une explosion simplement par
 * deux durées :
 * <ul>
 * 		<li>La durée en ms avant que l'explosion ne commence
 * 			(exprimée par rapport à l'instant courant)</li>
 * 		<li>La durée de l'explosion elle même</li>
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class AiExplosion implements Comparable<AiExplosion>
{	
	/**
	 * Construit une nouvelle représentation
	 * d'explosion.
	 * 
	 * @param start
	 * 		Moment de démarrage de l'explosion.
	 * @param end
	 * 		Moment de terminaison de l'explosion.
	 */
	public AiExplosion(long start, long end)
	{	this.start = start;
		this.end = end;
	}	
	
	/////////////////////////////////////////////////////////////////
	// START			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Moment de démarrage (en ms) de l'explosion */
	private long start;

	/**
	 * Renvoie le moment de démarrage
	 * (en ms) de l'explosion.
	 * 
	 * @return
	 * 		Le moment de démarrage de l'explosion.
	 */
	public long getStart()
	{	return start;
	}
	
	/////////////////////////////////////////////////////////////////
	// END				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Moment de terminaison (en ms) de l'explosion */
	private long end;

	/**
	 * Renvoie le moment de terminaison 
	 * (en ms) de l'explosion.
	 * 
	 * @return
	 * 		Le moment de terminaison de l'explosion.
	 */
	public long getEnd()
	{	return end;
	}

	/**
	 * Modifie le moment de terminaison de l'explosion.
	 * 
	 * @param end
	 * 		Le moment de terminaison de l'explosion (en ms).
	 */
	public void setEnd(long end)
	{	this.end = end;
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(AiExplosion o)
	{	Long start = new Long(this.start);
		int result = start.compareTo(o.start);
		if(result==0)
		{	Long duration = new Long(this.end);
			result = duration.compareTo(o.end);
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o)
	{	boolean result = false;;
		if(o!=null && o instanceof AiExplosion)
		{	AiExplosion explosion = (AiExplosion)o;
			result = compareTo(explosion) == 0;
		}
		return result;
	}
	
	@Override
    public int hashCode()
    {	String str = start+":"+end;
    	int result = str.hashCode();
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// TERMINAISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "[start"+"->"+"end]";
		return result;
	}
}
