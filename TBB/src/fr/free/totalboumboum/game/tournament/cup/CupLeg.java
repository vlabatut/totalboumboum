package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

public class CupLeg implements Serializable
{	private static final long serialVersionUID = 1L;

	public CupLeg(CupTournament tournament)
	{	this.tournament = tournament;	
	}

	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void init()
	{	// are parts in random order ?
		if(randomizeParts)
			randomizeParts();
		
		currentIndex = 0;
		currentPart = parts.get(currentIndex);
		currentPart.init();
	}
	
	public void progress()
	{	if(currentPart.isOver())
		{	currentIndex++;
			currentPart = parts.get(currentIndex);
			currentPart.init();
		}
		else
			currentPart.progress();
	}
	
	public void finish()
	{	// misc
		tournament = null;
		// parts
		parts.clear();
	}
	
	public void matchOver()
	{	currentPart.matchOver();
		if(currentPart.isOver() && currentIndex==parts.size()-1)
			setOver(true);			
	}

	/////////////////////////////////////////////////////////////////
	// LEG				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public CupLeg getPreviousLeg()
	{	CupLeg result = null;
		if(number>0)
			result = tournament.getLeg(number-1);
		return result;
	}
	
	public CupLeg getNextLeg()
	{	CupLeg result = null;
		if(number<tournament.getLegs().size()-1)
			result = tournament.getLeg(number+1);
		return result;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean legOver = false;

	public boolean isOver()
	{	return legOver;
	}
	public void setOver(boolean legOver)
	{	this.legOver = legOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupTournament tournament;
	
	public CupTournament getTournament()
	{	return tournament;
	}
	
	public void setTournament(CupTournament tournament)
	{	this.tournament = tournament;
	}

	/////////////////////////////////////////////////////////////////
	// PARTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<CupPart> parts = new ArrayList<CupPart>();
	private int currentIndex;
	private CupPart currentPart;
	private boolean randomizeParts;
	
	public ArrayList<CupPart> getParts()
	{	return parts;
	}
	
	public CupPart getPart(int index)
	{	return parts.get(index);
	}
	
	public void addPart(CupPart part)
	{	parts.add(part);
	}

	public CupPart getCurrentPart()
	{	return currentPart;
	}

	private void randomizeParts()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(parts,random);
	}
	
	public boolean getRandomizeParts()
	{	return randomizeParts;
	}
	public void setRandomizeParts(boolean randomizeParts)
	{	this.randomizeParts = randomizeParts;
	}

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
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int number;

	public void setNumber(int number)
	{	this.number = number;
	}

	public int getNumber()
	{	return number;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void simulatePlayerProgression(List<Integer> distribution)
	{	for(int i=0;i<distribution.size();i++)
		{	CupPart part = parts.get(i);
			int nbr = distribution.get(i);
			part.simulatePlayerProgression(nbr);
		}
			
		
	}
}
