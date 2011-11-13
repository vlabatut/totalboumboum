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

		// before the explosion
		AiExplosion previous = head.last();
		long explosionStart = explosion.getStart();
		long explosionEnd = explosion.getEnd();
		long previousEnd = previous.getEnd();
		if(explosionStart<=previousEnd)
		{	previous.setEnd(explosionEnd);
			explosion = previous;
		}
		
		// after the explosion
		Iterator<AiExplosion> it = tail.iterator();
		boolean merged = true;
		while(it.hasNext() && merged)
		{	AiExplosion next = it.next();
			long nextStart = next.getStart();
			if(merged=nextStart<=explosionEnd)
			{	long nextEnd = next.getEnd();
				explosion.setEnd(nextEnd);
				explosionEnd = nextEnd;
				it.remove();
			}
		}
		
		return true;
	}
}
