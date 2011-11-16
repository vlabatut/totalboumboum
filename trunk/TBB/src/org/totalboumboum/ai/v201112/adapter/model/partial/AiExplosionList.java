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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;

/**
 * Représente une liste d'explosions,
 * utilisée dans {@link AiPartialModel}
 * 
 * @author Vincent Labatut
 */
public class AiExplosionList extends TreeSet<AiExplosion>
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// OPERATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean add(AiExplosion explosion)
	{	// init
		SortedSet<AiExplosion> head = headSet(explosion);
		SortedSet<AiExplosion> tail = tailSet(explosion);
		long explosionStart = explosion.getStart();
		long explosionEnd = explosion.getEnd();
		boolean wasMerged = false;
		
		// existing explosions starting before the begining of the new one
		if(!head.isEmpty())
		{	AiExplosion previous = head.last();
			long previousEnd = previous.getEnd();
			if(explosionStart<=previousEnd)
			{	previous.setEnd(explosionEnd);
				explosion = previous;
				wasMerged = true;
			}
		}
		
		// existing explosions starting after the begining of the new one
		if(!tail.isEmpty())
		{	Iterator<AiExplosion> it = tail.iterator();
			boolean covers = true;
			while(it.hasNext() && covers)
			{	AiExplosion next = it.next();
				long nextStart = next.getStart();
				if(covers=nextStart<=explosionEnd)
				{	long nextEnd = next.getEnd();
					explosionEnd = Math.max(explosionEnd,nextEnd);
					if(wasMerged)
					{	explosion.setEnd(explosionEnd);
						it.remove();
					}
					else
					{	explosion = next;
						explosion.setStart(explosionStart);
						wasMerged = true;
					}
				}
			}
		}
		
		// no mergin at all -> insertion
		if(!wasMerged)
			super.add(explosion);
			
		return true;
	}
	
	/**
	 * Détermine si la période allant de l'instant
	 * courant à l'instant donné en paramètre
	 * intersecte l'une des explosions contenues
	 * dans cette liste.
	 * 
	 * @param time
	 * 		L'instant de fin de l'intervalle à tester.
	 * @return
	 * 		{@code true} ssi au moins une explosion est en
	 * 		intersection avec l'intervale spécifié.
	 */
	public boolean intersects(long time)
	{	boolean result = false;
		Iterator<AiExplosion> it = iterator();
		boolean goOn = true;
		while(it.hasNext() && goOn && !result)
		{	AiExplosion explosion = it.next();
			long startTime = explosion.getStart();
			long endTime = explosion.getEnd();
			if(startTime<=time)
			{	if(endTime>=time)
					result = true;
			}
			else //if(startTime>time)
				goOn = false;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Crée une liste d'explosions
	 * correspondant à une copie de celle
	 * passée en paramètre.
	 * 
	 * @param list
	 * 		La liste à copier
	 */
	public AiExplosionList copy()
	{	AiExplosionList result = new AiExplosionList();
		for(AiExplosion explosion: this)
			result.add(new AiExplosion(explosion));
		return result;
	}
}
