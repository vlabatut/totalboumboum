package org.totalboumboum.engine.container.level.zone;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.totalboumboum.engine.container.level.variabletile.ValueTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Zone implements Serializable
{	private static final long serialVersionUID = 1L;
	
	public Zone(int globalWidth, int globalHeight)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIMENSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int globalWidth;
	private int globalHeight;
	
	public int getGlobalWidth()
	{	return globalWidth;
	}

	public void setGlobalWidth(int width)
	{	globalWidth = width;
	}
	
	public int getGlobalHeight()
	{	return globalHeight;
	}

	public void setGlobalHeight(int height)
	{	globalHeight = height;
	}
	
	/////////////////////////////////////////////////////////////////
	// VARIABLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,VariableTile> variableTiles = new HashMap<String, VariableTile>();
	
	public void setVariableTiles(HashMap<String,VariableTile> variables)
	{	this.variableTiles = variables;		
	}
	public HashMap<String,VariableTile> getVariableTiles()
	{	return variableTiles;		
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ZoneHollowTile> tiles = new ArrayList<ZoneHollowTile>();
	
	public void addTile(ZoneHollowTile tile)
	{	tiles.add(tile);
	}
	
	public void removeTile(ZoneHollowTile tile)
	{	tiles.remove(tile);
	}
	
	public List<ZoneHollowTile> getTiles()
	{	return tiles;
	}
	
	public ZoneHollowTile getTile(int row, int col)
	{	ZoneHollowTile result = null;
		
		Iterator<ZoneHollowTile> it = tiles.iterator();
		while(result==null && it.hasNext())
		{	ZoneHollowTile temp = it.next();
			if(temp.getRow()==row && temp.getCol()==col)
				result = temp;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of modifications to be applied after the round has begun */
	private HashMap<Long,List<ZoneHollowTile>> events = new HashMap<Long,List<ZoneHollowTile>>();
	/** List of initialized events */
	private HashMap<Long,List<ZoneTile>> eventsInit;

	/** Indicates if the step times are relative to the total duration, or are fixed */
	private boolean eventsRelative = false;
	/** Indicates the total duration. Always used if the game is set to no limit. Otherwise, can be overriden by game settings */
	private long eventsDuration = -1;
		
	/**
	 * Indicates if the event times are relative
	 * to the total duration (or not).
	 * 
	 * @return
	 * 		Event times relative or not.
	 */
	public boolean isEventsRelative()
	{	return eventsRelative;
	}

	/**
	 * Change the fact the event times are relative
	 * to the total duration (or not).
	 * 
	 * @param eventsRelative
	 * 		New value.
	 */
	public void setEventsRelative(boolean eventsRelative)
	{	this.eventsRelative = eventsRelative;
	}

	/**
	 * Returns the total duration for
	 * this game. Can be overriden by 
	 * game settings, but is used to process
	 * events times.
	 * 
	 * @return
	 * 		Total duration (in ms).
	 */
	public long getEventsDuration()
	{	return eventsDuration;
	}

	/**
	 * Change the total duration.
	 *  
	 * @param eventsDuration
	 * 		New total duration (in ms).
	 */
	public void setEventsDuration(long eventsDuration)
	{	this.eventsDuration = eventsDuration;
	}

	/**
	 * Add a new modification to the map.
	 * 
	 * @param time
	 * 		Time of the modification.
	 * @param tile
	 * 		Nature of the modification.
	 */
	public void addEvent(Long time, ZoneHollowTile tile)
	{	List<ZoneHollowTile> list = events.get(time);
		if(list==null)
		{	list = new ArrayList<ZoneHollowTile>();
			events.put(time,list);
		}
		list.add(tile);
	}
	
	/**
	 * Returns all remaining sudden death events.
	 * 
	 * @return
	 * 		A map containing all remaining sudden death events.
	 */
	public HashMap<Long,List<ZoneTile>> getEventsInit()
	{	return eventsInit;
	}
	
	/**
	 * Returns sudden death events occuring in
	 * the specified period.
	 * 
	 * @param currentGameTime
	 * 		Current game time.
	 * @return
	 * 		List of tiles containing sprites to appear during sudden death.
	 */
	public List<ZoneTile> getEventsInit(long currentGameTime)
	{	List<ZoneTile> result = new ArrayList<ZoneTile>();
		SortedSet<Long> times = new TreeSet<Long>(eventsInit.keySet());
		SortedSet<Long> head = times.headSet(currentGameTime);
		for(Long time: head)
		{	result.addAll(eventsInit.get(time));
			eventsInit.remove(time);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrix representing the content of this zone */
	private ZoneTile[][] matrix;
	
	/**
	 * 0: floors
	 * 1: blocks
	 * 2: items
	 * 3: bombs
	 * @return
	 */
	public ZoneTile[][] getMatrix()
	{	return matrix;		
	}
	
	public void makeMatrix(long timeLimit)
	{	// init matrix
		matrix = new ZoneTile[globalHeight][globalWidth];
		for(int i=0;i<globalHeight;i++)
		{	for(int j=0;j<globalWidth;j++)
			{	matrix[i][j] = null;
			}
		}
		
		// init variables
		Iterator<Entry<String,VariableTile>> iter = variableTiles.entrySet().iterator();
		while(iter.hasNext())
		{	Entry<String,VariableTile> temp = iter.next();
			VariableTile value = temp.getValue();
			value.init();
		}
		
		// populate matrices
    	Iterator<ZoneHollowTile> it = tiles.iterator();
    	while(it.hasNext())
    	{	ZoneHollowTile tile = it.next();
    		ZoneTile instance = initTile(tile);
        	matrix[instance.getRow()][instance.getCol()] = instance;
    	}
		
    	// init events
    	double factor = 1;
		if(!eventsRelative || timeLimit != Long.MAX_VALUE)
			factor = timeLimit / (double)eventsDuration;
    	//long maxTime = Collections.max(events.keySet());
    	eventsInit = new HashMap<Long,List<ZoneTile>>();
    	for(Entry<Long, List<ZoneHollowTile>> entry: events.entrySet())
    	{	// get the step
    		long time = Math.round(entry.getKey()*factor);
    		List<ZoneHollowTile> list = entry.getValue();
    		
    		// init
    		List<ZoneTile> listInit = new ArrayList<ZoneTile>();
    		for(ZoneHollowTile tile: list)
    		{	ZoneTile instance = initTile(tile);
    			listInit.add(instance);
    		}
    		eventsInit.put(time, listInit);
    	}
	}
	
	private ZoneTile initTile(ZoneHollowTile tile)
	{	// init instance tile
		int col = tile.getCol();
		int row = tile.getRow();
		ZoneTile result = new ZoneTile(row, col);
		
		// constant parts
		result.setFloor(tile.getFloor());
		result.setBlock(tile.getBlock());
		result.setItem(tile.getItem());
		result.setBomb(tile.getBomb());
		
		// variable part
		String name = tile.getVariable();
		if(name!=null)
		{	VariableTile vt = variableTiles.get(name);
			ValueTile vit = vt.getNext();
			String itm = vit.getItem();
			String blck = vit.getBlock();
			String flr = vit.getFloor();
			String bmb = vit.getBomb();
			if(result.getFloor()==null)
				result.setFloor(flr);
			if(result.getBlock()==null)
				result.setBlock(blck);
			if(result.getItem()==null)
				result.setItem(itm);
			if(result.getBomb()==null)
				result.setBomb(bmb);
		}
		
		return result;
	}
	
	public HashMap<String,Integer> getItemCount()
	{	HashMap<String,Integer> result = new HashMap<String,Integer>();
	
		// matrix
		for(int i=0;i<globalHeight;i++)
		{	for(int j=0;j<globalWidth;j++)
			{	ZoneTile tile = matrix[i][j];
				updateItemCount(tile,result);
			}
		}
		
		// events
    	for(List<ZoneTile> list: eventsInit.values())
    	{	for(ZoneTile tile: list)
	    	{	updateItemCount(tile,result);
	    	}
    	}
		
		return result;
	}
	
	private void updateItemCount(ZoneTile tile, HashMap<String,Integer> result)
	{	String item = tile.getItem();
		if(item!=null)
		{	int value;
			if(result.containsKey(item))
			{	value = result.get(item);
				value ++;
			}
			else
				value = 1;
			result.put(item,value);
		}
	}
}
