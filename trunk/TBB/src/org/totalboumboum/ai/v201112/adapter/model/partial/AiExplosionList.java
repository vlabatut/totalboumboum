package org.totalboumboum.ai.v201112.adapter.model.partial;

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

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;

/**
 * Représente une liste d'explosions,
 * utilisée dans {@link AiPartialModel}
 * 
 * @author Vincent Labatut
 */
public class AiExplosionList extends TreeSet<AiExplosion>
{	private static final long serialVersionUID = 1L;
public AiExplosionList(){}

	public AiExplosionList(AiTile tile)
	{	this.tile = tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Case concernée par les explosions */
	private AiTile tile = null;
	
	/**
	 * Renvoie la case concernée
	 * par cette liste d'explosions.
	 * 
	 * @return
	 * 		La case concernée par cette liste d'explosions.
	 */
	public AiTile getTile()
	{	return tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// OPERATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean add(AiExplosion explosion)
	{	// init
		SortedSet<AiExplosion> head = headSet(explosion);
		SortedSet<AiExplosion> tail = tailSet(explosion);
		boolean emptyTail = tail.isEmpty();
		long explosionStart = explosion.getStart();
		long explosionEnd = explosion.getEnd();
		boolean wasMerged = false;
		
		// existing explosion starting before the begining of the new one
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
		if(!emptyTail)
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
		
		if(tile==null)
			tile = explosion.getTile();
	
		return true;
	}
	
	/**
	 * Détermine si la période définie par les deux
	 * instants donnés en paramètre
	 * intersecte l'une des explosions contenues
	 * dans cette liste. Si c'est le cas, elle est
	 * renvoyée, sinon c'est la valeur {@code null}
	 * qui est renvoyée.
	 * 
	 * @param start
	 * 		L'instant de début de l'intervalle à tester.
	 * @param time
	 * 		L'instant de fin de l'intervalle à tester.
	 * @return
	 * 		L'explosion intersectant le paramètre, ou bien {@code null} si l'intersection est vide.
	 */
	public AiExplosion getIntersection(long startTime, long endTime)
	{	// init
		AiExplosion explosion = new AiExplosion(startTime,endTime,tile);
		SortedSet<AiExplosion> head = headSet(explosion);
		SortedSet<AiExplosion> tail = tailSet(explosion);
		boolean emptyTail = tail.isEmpty();
		long explosionStart = Long.MAX_VALUE;
		long explosionEnd = Long.MIN_VALUE;
		
		// existing explosion starting before the begining of the new one
		if(!head.isEmpty())
		{	AiExplosion previous = head.last();
			long previousEnd = previous.getEnd();
			if(startTime<=previousEnd)
			{	long previousStart = previous.getStart();
				explosionStart = previousStart;
				explosionEnd = previousEnd;
			}
		}
		
		// existing explosions starting after the begining of the new one
		if(!emptyTail)
		{	Iterator<AiExplosion> it = tail.iterator();
			boolean covers = true;
			while(it.hasNext() && covers)
			{	AiExplosion next = it.next();
				long nextStart = next.getStart();
				if(covers=nextStart<=endTime)
				{	explosionStart = Math.min(explosionStart,nextStart);
					long nextEnd = next.getEnd();
					explosionEnd = Math.max(explosionEnd,nextEnd);
				}
			}
		}
		
		// result
		AiExplosion result = null;
		if(explosionStart!=Long.MAX_VALUE)
			result = new AiExplosion(explosionStart,explosionEnd,tile);
		
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
	{	AiExplosionList result = new AiExplosionList(tile);
		for(AiExplosion explosion: this)
			result.add(new AiExplosion(explosion));
		return result;
	}
}
