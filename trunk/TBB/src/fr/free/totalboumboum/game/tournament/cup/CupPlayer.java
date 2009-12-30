package fr.free.totalboumboum.game.tournament.cup;

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

import java.io.Serializable;

public class CupPlayer implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// PART			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int part;
	
	public int getPart()
	{	return part;
	}
	
	public void setPart(int part)
	{	this.part = part;
	}

	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int rank;

	public void setRank(int rank)
	{	this.rank = rank;
	}

	public int getRank()
	{	return rank;
	}

	/////////////////////////////////////////////////////////////////
	// USED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean used;
	private int usedRank;
	
	public void setUsed(boolean used)
	{	this.used = used;
	}

	public boolean getUsed()
	{	return used;
	}
	
	public void setUsedRank(int usedRank)
	{	this.usedRank = usedRank;
	}

	public int getUsedRank()
	{	return usedRank;
	}
}
