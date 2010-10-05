package org.totalboumboum.game.tournament.cup;

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

import java.io.Serializable;

import org.totalboumboum.game.profile.Profile;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class CupPlayer implements Serializable
{	private static final long serialVersionUID = 1L;

	public CupPlayer(CupPart part)
	{	this.part = part;	
	}

	/////////////////////////////////////////////////////////////////
	// PART				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupPart part;
	
	public CupPart getPart()
	{	return part;
	}
	
	/////////////////////////////////////////////////////////////////
	// PREVIOUS PART	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int prevPart;
	
	public int getPrevPart()
	{	return prevPart;
	}
	
	public void setPrevPart(int part)
	{	this.prevPart = part;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS RANK	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int prevRank;

	public void setPrevRank(int rank)
	{	this.prevRank = rank;
	}

	public int getPrevRank()
	{	return prevRank;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean used;
	private int simulatedRank;
	private int simulatedFinalRank;
	private int actualFinalRank;
	
	public void setUsed(boolean used)
	{	this.used = used;
	}

	public boolean getUsed()
	{	return used;
	}
	
	public void setSimulatedRank(int simulatedRank)
	{	this.simulatedRank = simulatedRank;
		simulatedFinalRank = 0;
		actualFinalRank = 0;
	}

	public int getSimulatedRank()
	{	return simulatedRank;
	}

	public void setSimulatedFinalRank(int simulatedFinalRank)
	{	this.simulatedFinalRank = simulatedFinalRank;
		CupLeg prevLeg = part.getLeg().getPreviousLeg();
		if(prevLeg!=null)
		{	CupPart prevPart = prevLeg.getPart(this.prevPart);
			CupPlayer prevPlayer = prevPart.getPlayerSimulatedRank(prevRank);
			prevPlayer.setSimulatedFinalRank(simulatedFinalRank);
		}
	}

	public int getSimulatedFinalRank()
	{	return simulatedFinalRank;
	}
	
	public void setActualFinalRank(int actualFinalRank)
	{	this.actualFinalRank = actualFinalRank;
		CupLeg prevLeg = part.getLeg().getPreviousLeg();
		if(prevLeg!=null)
		{	CupPart prevPart = prevLeg.getPart(this.prevPart);
			int index = part.getPlayers().indexOf(this);
			Profile profile = part.getProfileForIndex(index);
			CupPlayer prevPlayer = prevPart.getPlayerForProfile(profile);
			prevPlayer.setActualFinalRank(actualFinalRank);
		}
	}

	public int getActualFinalRank()
	{	return actualFinalRank;
	}
	
	public void reinitActualFinalRank()
	{	actualFinalRank = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "";
		result = result + "used: "+used;
		result = result + " simulatedRank: "+simulatedRank;
		result = result + " simulatedFinalRank: "+simulatedFinalRank;
		result = result + " actualFinalRank: "+actualFinalRank;
		return result;
	}
}
