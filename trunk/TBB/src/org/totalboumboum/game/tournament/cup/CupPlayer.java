package org.totalboumboum.game.tournament.cup;

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

import java.io.Serializable;

import org.totalboumboum.game.profile.Profile;

/**
 * Represents a player participating to a cup tournament.
 * 
 * @author Vincent Labatut
 */
public class CupPlayer implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard cup player.
	 * 
	 * @param part
	 * 		The part containing this player.
	 */
	public CupPlayer(CupPart part)
	{	this.part = part;	
	}

	/////////////////////////////////////////////////////////////////
	// PART				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The part containing this player */
	private CupPart part;
	
	/**
	 * Returns the part containing this player.
	 * 	
	 * @return
	 * 		The part of this player.
	 */
	public CupPart getPart()
	{	return part;
	}
	
	/////////////////////////////////////////////////////////////////
	// PREVIOUS PART	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Part this player comes from */
	private int prevPart;
	
	/**
	 * Returns the part this player comes from
	 * 	
	 * @return
	 * 		The part this player played before.
	 */
	public int getPrevPart()
	{	return prevPart;
	}
	
	/**
	 * Changes the part this player comes from.
	 * 
	 * @param part
	 * 		The new previous part for this player.
	 */
	public void setPrevPart(int part)
	{	this.prevPart = part;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS RANK	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The rank the player had in the previous part he played */
	private int prevRank;

	/**
	 * Returns the rank the player had 
	 * in the previous part he played.
	 * 
	 * @return rank
	 * 		Previous rank of the player.
	 */
	public int getPrevRank()
	{	return prevRank;
	}

	/**
	 * Changes the rank the player had 
	 * in the previous part he played.
	 * 
	 * @param rank
	 * 		New previous rank of the player.
	 */
	public void setPrevRank(int rank)
	{	this.prevRank = rank;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Flag used when performing simulations for player seeding */
	private boolean used;
	/** Fake rank used when performing simulations for player seeding */
	private int simulatedRank;
	/** Fake final rank used when performing simulations for player seeding */
	private int simulatedFinalRank;
	/** Actual rank obtained by the player (?) */
	private int actualFinalRank;
	
	/**
	 * Change the simulation flag of this player.
	 * 
	 * @param used
	 * 		New value for the flag.
	 */
	public void setUsed(boolean used)
	{	this.used = used;
	}

	/**
	 * Returns the simulation flag of this player.
	 * 
	 * @return
	 * 		A boolean flag.
	 */
	public boolean getUsed()
	{	return used;
	}
	
	/**
	 * Changes the simulated rank for this player.
	 * 
	 * @param simulatedRank
	 * 		New simulated rank.
	 */
	public void setSimulatedRank(int simulatedRank)
	{	this.simulatedRank = simulatedRank;
		simulatedFinalRank = 0;
		actualFinalRank = 0;
	}

	/**
	 * Returns the simulated rank for this player.
	 * 
	 * @return
	 * 		The simulated rank of this player.
	 */
	public int getSimulatedRank()
	{	return simulatedRank;
	}

	/**
	 * Changes the simulated final rank for this player.
	 * 
	 * @param simulatedFinalRank
	 * 		New simulated final rank.
	 */
	public void setSimulatedFinalRank(int simulatedFinalRank)
	{	this.simulatedFinalRank = simulatedFinalRank;
		CupLeg prevLeg = part.getLeg().getPreviousLeg();
		if(prevLeg!=null)
		{	CupPart prevPart = prevLeg.getPart(this.prevPart);
			CupPlayer prevPlayer = prevPart.getPlayerSimulatedRank(prevRank);
			prevPlayer.setSimulatedFinalRank(simulatedFinalRank);
		}
	}

	/**
	 * Returns the simulated final rank for this player.
	 * 
	 * @return
	 * 		Simulated final rank.
	 */
	public int getSimulatedFinalRank()
	{	return simulatedFinalRank;
	}
	
	/**
	 * Changes the actual final rank for this player.
	 * 
	 * @param actualFinalRank
	 * 		Actual final rank.
	 */
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

	/**
	 * Returns the the actual final rank for this player.
	 * 
	 * @return
	 * 		Actual final rank.
	 */
	public int getActualFinalRank()
	{	return actualFinalRank;
	}
	
	/**
	 * Reset the actual final rank for this player.
	 */
	public void reinitActualFinalRank()
	{	actualFinalRank = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		result = result + "used: "+used;
		result = result + " simulatedRank: "+simulatedRank;
		result = result + " simulatedFinalRank: "+simulatedFinalRank;
		result = result + " actualFinalRank: "+actualFinalRank;
		return result;
	}
}
