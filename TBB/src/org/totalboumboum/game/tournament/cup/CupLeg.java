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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents the settings of a leg in a 
 * cup tournament. It is formed of several 
 * parts. The tournament itself is made up 
 * of several legs.
 * 
 * @author Vincent Labatut
 *
 */
public class CupLeg implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard cup leg.
	 * 
	 * @param tournament
	 * 		The tournament containing this leg.
	 */
	public CupLeg(CupTournament tournament)
	{	this.tournament = tournament;	
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initializes this leg before starting it.
	 */
	public void init()
	{	// are parts in random order ?
		if(randomizeParts)
			randomizeParts();
		
		currentIndex = 0;
		currentPart = parts.get(currentIndex);
		currentPart.init();
	}
	
	/**
	 * Triggers progress in this leg,
	 * i.e. goes to the next part. 
	 */
	public void progress()
	{	if(currentPart.isOver())
		{	currentIndex++;
			currentPart = parts.get(currentIndex);
			currentPart.init();
		}
		else
			currentPart.progress();
	}
	
	/**
	 * Cleanly finishes this object.
	 */
	public void finish()
	{	// misc
		tournament = null;
		// parts
		parts.clear();
	}
	
	/**
	 * Signals to the current part
	 * that its match has just finished,
	 * in order to trigger an update.
	 */
	public void matchOver()
	{	currentPart.matchOver();
		if(currentPart.isOver() && currentIndex==parts.size()-1)
			setOver(true);			
	}

	/////////////////////////////////////////////////////////////////
	// LEG				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the previous leg in the tournament,
	 * or {@code null} if this one is the first.
	 * 
	 * @return
	 * 		Previous leg in the tournament.
	 */
	public CupLeg getPreviousLeg()
	{	CupLeg result = null;
		if(number>0)
			result = tournament.getLeg(number-1);
		return result;
	}
	
	/**
	 * Returns the next leg in the tournament,
	 * or {@code null} if this is the last one.
	 * 
	 * @return
	 * 		The next leg in the tournament.
	 */
	public CupLeg getNextLeg()
	{	CupLeg result = null;
		if(number<tournament.getLegs().size()-1)
			result = tournament.getLeg(number+1);
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if this leg is finished */
	private boolean legOver = false;

	/**
	 * Indices whether this leg is finished
	 * or not.
	 * 
	 * @return
	 * 		{@code true} iff this leg is finished.
	 */
	public boolean isOver()
	{	return legOver;
	}
	
	/**
	 * Changes the flag indicating if this
	 * leg is finished.
	 * 
	 * @param legOver
	 * 		New value for the flag.
	 */
	public void setOver(boolean legOver)
	{	this.legOver = legOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament containing this leg */
	private CupTournament tournament;
	
	/**
	 * Returns the tournament containing this leg.
	 *  
	 * @return
	 * 		The tournament containing this leg.
	 */
	public CupTournament getTournament()
	{	return tournament;
	}
	
	/**
	 * Changes the tournament containing this leg.
	 * 
	 * @param tournament
	 * 		New tournament containing this leg.
	 */
	public void setTournament(CupTournament tournament)
	{	this.tournament = tournament;
	}

	/////////////////////////////////////////////////////////////////
	// PARTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of parts constituting this leg */
	private final List<CupPart> parts = new ArrayList<CupPart>();
	/** Number of the current part */
	private int currentIndex;
	/** Part currently played */
	private CupPart currentPart;
	/**  Indicates if the parts should be played in a random order */
	private boolean randomizeParts;
	
	/**
	 * Returns the list of parts constituting this leg.
	 * 
	 * @return
	 * 		The list of parts constituting this leg.
	 */
	public List<CupPart> getParts()
	{	return parts;
	}
	
	/**
	 * Returns the part whose position
	 * is specified.
	 * 
	 * @param index
	 * 		Position of the part of interest.
	 * @return
	 * 		The requested part.
	 */
	public CupPart getPart(int index)
	{	return parts.get(index);
	}
	
	/**
	 * Adds a new part to this leg.
	 * 
	 * @param part
	 * 		The new part.
	 */
	public void addPart(CupPart part)
	{	parts.add(part);
	}

	/**
	 * Returns the part currently played.
	 * 
	 * @return
	 * 		The current part.
	 */
	public CupPart getCurrentPart()
	{	return currentPart;
	}

	/**
	 * Shuffles the parts.
	 */
	private void randomizeParts()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(parts,random);
	}
	
	/**
	 * Returns the flag indicating if the
	 * parts should be played in a random order.
	 * 
	 * @return
	 * 		{@code true} iff the part are randomized.
	 */
	public boolean getRandomizeParts()
	{	return randomizeParts;
	}
	
	/**
	 * Changes the flag indicating if the
	 * parts should be played in a random order.
	 * 
	 * @param randomizeParts
	 * 		New value for the flat.
	 */
	public void setRandomizeParts(boolean randomizeParts)
	{	this.randomizeParts = randomizeParts;
	}

	/**
	 * Returns the part corresponding to the
	 * specified final rank.
	 * 
	 * @param rank
	 * 		Final rank of the part of interest.
	 * @return
	 * 		The corresponding part.
	 */
	public CupPart getPartFromRank(int rank)
	{	CupPart result = null;
		Iterator<CupPart> it = parts.iterator();
		while(it.hasNext() && result==null)
		{	CupPart part = it.next();
			if(part.getRank()==rank)
				result = part;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the list of players appearing in this leg.
	 * 
	 * @return
	 * 		A list of cup player involved in this leg.
	 */
	public List<CupPlayer> getAllUsedPlayers()
	{	List<CupPlayer> result = new ArrayList<CupPlayer>();
		for(CupPart part: parts)
		{	List<CupPlayer> players = part.getUsedPlayers();
			result.addAll(players);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of this leg in the tournament */
	private int number;

	/**
	 * Changes the number of this leg in the tournament.
	 * 
	 * @param number
	 * 		New number for this leg.
	 */
	public void setNumber(int number)
	{	this.number = number;
	}

	/**
	 * Returns the number of this leg in the tournament.
	 * 
	 * @return
	 * 		Number of this leg in the tournament.
	 */
	public int getNumber()
	{	return number;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER DISTRIBUTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Counts the number of matches in this leg
	 * which includes players involved in their very
	 * first match of the tournament.
	 * 
	 * @return
	 * 		Number of matches in the leg accepting
	 * 		new players. 
	 */
	protected int countEntryMatches()
	{	int result = 0;
		
		for(CupPart part: parts)
		{	if(part.isEntryMatch())
				result++;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Simulates how some players would progress through the whole
	 * tournament. This updates various objects, including cup players.
	 * The results of this simulation are then used to sort the players
	 * in order to get seeds.
	 * 
	 *  @param distribution
	 *  	How the players are distributed in this leg.
	 *  @return
	 *  	{@code true} iff the distribution is allowed.
	 */
	public boolean simulatePlayerProgression(List<Integer> distribution)
	{	boolean result = true;
		
		// this leg parts
		Iterator<CupPart> it = parts.iterator();
		while(it.hasNext() && result)
		{	CupPart part = it.next();
			result = part.simulatePlayerProgression(distribution);
		}
	
		// next leg
		if(result)
		{	CupLeg nextLeg = getNextLeg();
			if(nextLeg!=null)
				result = nextLeg.simulatePlayerProgression(distribution);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINAL RANKS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Resets the actual final ranks of the players,
	 * in each part of this leg.
	 */
	public void resetPlayersActualFinalRanks()
	{	for(CupPart part: parts)
			part.resetPlayersActualFinalRanks();
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		result = result + ">> leg " + number + "\n";
		for(CupPart part: parts)
			result = result + part + "\n";
		return result;
	}
}
