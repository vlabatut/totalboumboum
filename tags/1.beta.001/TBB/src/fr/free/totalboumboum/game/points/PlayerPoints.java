package fr.free.totalboumboum.game.points;

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

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerPoints implements Comparable<PlayerPoints>
{
	private String player;
	private final ArrayList<Float> points = new ArrayList<Float>();
	private int index;
	
	public PlayerPoints(String player, int index)
	{	this.player = player;
		this.index = index;
	}
	
	public void addPoint(float point)
	{	points.add(point);
	}

	@Override
	public int compareTo(PlayerPoints pp)
	{	return compareTo(pp,true);
	}
	public int compareTo(PlayerPoints pp, boolean absolute)
	{	int result = 0;
		int index = 0;
		//
		do
		{	// if pp has less different points, it is smaller
			if(pp.points.size()<index)
				result = +1;
			// else we compare the current points
			else
				result = (new Float(points.get(index))).compareTo(new Float(pp.points.get(index)));
			index ++;
		}
		while(result==0 && index<points.size());
		if(result==0)
		{	// if pp has more different points, it is greater
			if(pp.points.size()>points.size())
				result = -1;
			else if(absolute) // only if we need an absolute order, we consider the player's name
				result = -player.compareTo(pp.player); //on inverse car ce classement sera lui-même pris à rebours
		}
		//
		return result;
	}
	public int compareToPoints(PlayerPoints pp)
	{	return compareTo(pp,false);
	}
	public boolean equalsPoints(PlayerPoints pp)
	{	return compareToPoints(pp) == 0;		
	}

	public String getPlayer()
	{	return player;
	}
/*	public Iterator<Float> getPoints()
	{	return points.iterator();
	}*/
	public int getIndex()
	{	return index;
	}
	
	public String toString()
	{	String result = player+"("+index+")"+"[ ";
		Iterator<Float> i = points.iterator();
		while(i.hasNext())
			result = result+i.next()+" ";
		result = result+"]";
		return result;
	}
}
