package org.totalboumboum.ai.v201213.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.List;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AiSuddenDeathEvent implements Comparable<AiSuddenDeathEvent>
{	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Instant associé à cet évènement */
	private long time;
	
	/**
	 * Renvoie l'instant associé à cet évènement.
	 * 
	 * @return
	 * 		Instant exprimé en ms.
	 */
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste de sprites apparaissant
	 * à l'instant associé à cet évènement. On
	 * peut récupérer la position des sprites simplement
	 * en accédant à leur case.
	 * 
	 * @return	
	 * 		Liste de sprite.
	 */
	public abstract List<AiSprite> getSprites();

	/////////////////////////////////////////////////////////////////
	// COMPARABLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(AiSuddenDeathEvent o)
	{	long time2 = o.getTime();
		int result = (int)Math.signum(time - time2);
		return result;
	}

	@Override
	public int hashCode()
	{	Long temp = new Long(time);
		return temp.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{	boolean result = false;
		
		if(obj!=null)
		{	if(obj instanceof AiSuddenDeathEvent)
			{	AiSuddenDeathEvent se = (AiSuddenDeathEvent) obj;
				result = time==se.getTime();
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(time + ":");
		for(AiSprite sprite: getSprites())
			result.append(" " + sprite);
		return result.toString();
	}
}
