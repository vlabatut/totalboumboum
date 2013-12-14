package org.totalboumboum.engine.container.level.zone;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.variabletile.ValueTile;
import org.totalboumboum.engine.container.level.variabletile.VariableTile;
import org.totalboumboum.tools.collections.SetTools;

/**
 * Represents a zone, before the sprites are actually loaded
 * (after which the data is represented by a {@link Level} class).
 * 
 * @author Vincent Labatut
 */
public class Zone implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Buils a new zone using the specified dimensions.
	 * 
	 * @param globalWidth
	 * 		Width in tiles.
	 * @param globalHeight
	 * 		Height in tiles.
	 */
	public Zone(int globalWidth, int globalHeight)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIMENSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone width in tiles */
	private int globalWidth;
	/** Zone height in tiles */
	private int globalHeight;
	
	/**
	 * Returns the zone width in tiles.
	 * 
	 * @return
	 * 		Zone width in tiles.
	 */
	public int getGlobalWidth()
	{	return globalWidth;
	}

	/**
	 * Changes the zone width in tiles.
	 * 
	 * @param width
	 * 		New zone width in tiles.
	 */
	public void setGlobalWidth(int width)
	{	globalWidth = width;
	}
	
	/**
	 * Returns the zone height in tiles.
	 * 
	 * @return
	 * 		Zone height in tiles.
	 */
	public int getGlobalHeight()
	{	return globalHeight;
	}

	/**
	 * Changes the zone height in tiles.
	 * 
	 * @param height
	 * 		New zone height in tiles.
	 */
	public void setGlobalHeight(int height)
	{	globalHeight = height;
	}
	
	/////////////////////////////////////////////////////////////////
	// VARIABLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of variables defined for this zone */
	private Map<String,VariableTile> variableTiles = new HashMap<String, VariableTile>();
	
	/**
	 * Changes the variables defined for this zone.
	 * 
	 * @param variables
	 * 		New list of variables defined for this zone.
	 */
	public void setVariableTiles(Map<String,VariableTile> variables)
	{	this.variableTiles = variables;		
	}
	
	/**
	 * Returns the variables defined for this zone.
	 * 
	 * @return
	 * 		List of variables defined for this zone.
	 */
	public Map<String,VariableTile> getVariableTiles()
	{	return variableTiles;		
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tiles composing the zone */
	private List<ZoneHollowTile> tiles = new ArrayList<ZoneHollowTile>();
	
	/**
	 * Adds a new tile to the zone.
	 * 
	 * @param tile
	 * 		Tile to add.
	 */
	public void addTile(ZoneHollowTile tile)
	{	tiles.add(tile);
	}
	
	/**
	 * Remove an existing tile from the zone.
	 * 
	 * @param tile
	 * 		Tile to remove.
	 */
	public void removeTile(ZoneHollowTile tile)
	{	tiles.remove(tile);
	}
	
	/**
	 * Returns the list of tiles composing this zone.
	 * 
	 * @return
	 * 		List of tiles.
	 */
	public List<ZoneHollowTile> getTiles()
	{	return tiles;
	}
	
	/**
	 * Returns the tile at the specified location.
	 * 
	 * @param row
	 * 		Row number of the desired tile.
	 * @param col
	 * 		Col number of the desired tile.
	 * @return
	 * 		The tile at the specified positions.
	 */
	public ZoneHollowTile getTile(int row, int col)
	{	ZoneHollowTile result = null;
		
		Iterator<ZoneHollowTile> it = tiles.iterator();
		while(result==null && it.hasNext())
		{	ZoneHollowTile temp = it.next();
			int r = temp.getRows().iterator().next();	//TODO to be adapted for random positions?
			int c = temp.getCols().iterator().next();
			if(r==row && c==col)
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
	 * Add a new modification to the map.
	 * 
	 * @param time
	 * 		Time of the modification.
	 * @param tiles
	 * 		Nature of the modification.
	 */
	public void addEvents(Long time, List<ZoneHollowTile> tiles)
	{	List<ZoneHollowTile> list = events.get(time);
		if(list==null)
		{	list = new ArrayList<ZoneHollowTile>();
			events.put(time,list);
		}
		list.addAll(tiles);
	}

	/**
	 * Used to disable sudden death, depending
	 * on quickmatch configuration.
	 */
	public void resetSuddenDeath()
	{	events.clear();
	}
	
	/**
	 * Returns the list of non-initialized sudden
	 * death events.
	 * 
	 * @return
	 * 		A map of {@link ZoneHollowTile}.
	 */
	public Map<Long, List<ZoneHollowTile>> getEvents()
	{	return events;
	}
	
	/**
	 * Returns all remaining sudden death events.
	 * 
	 * @return
	 * 		A map containing all remaining sudden death events.
	 */
	public Map<Long,List<ZoneTile>> getEventsInit()
	{	return eventsInit;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrix representing the content of this zone */
	private ZoneTile[][] matrix;
	
	/**
	 * Returns the zone matrix.
	 * 
	 * @return
	 * 		Matrix of tiles.
	 */
	public ZoneTile[][] getMatrix()
	{	return matrix;		
	}
	
	/**
	 * Initializes the zone matrix.
	 * 
	 * @param timeLimit
	 * 		Time limit of the game.
	 */
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
		
		// populate matrix
    	Iterator<ZoneHollowTile> it = tiles.iterator();
    	while(it.hasNext())
    	{	ZoneHollowTile tile = it.next();
    		List<ZoneTile> instances = initTile(tile);
    		for(ZoneTile instance: instances)
    		{	int row = instance.getRow();
    			int col = instance.getCol();
    			matrix[row][col] = instance;
    		}
    	}
		
    	// init events
    	double factor = 1;
		if(eventsRelative && timeLimit != Long.MAX_VALUE)
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
    		{	List<ZoneTile> instances = initTile(tile);
    			for(ZoneTile instance: instances)
    				listInit.add(instance);
    		}
    		eventsInit.put(time, listInit);
    	}
	}
	
	/**
	 * Receives a hollow zole tile, and instantiates it to 
	 * produce a zone tile.
	 * 
	 * @param tile
	 * 		Hollow zone tile.
	 * @return
	 * 		Instantiated zone tile.
	 */
	private List<ZoneTile> initTile(ZoneHollowTile tile)
	{	// retrieve possible positions
		Set<Integer> possCols = tile.getCols();
		int nCols = tile.getColNumber();
		Set<Integer> possRows = tile.getRows();
		int nRows = tile.getRowNumber();
if(nCols>1)
	System.out.print("");
		
		// draw positions
		List<ZoneTile> result = new ArrayList<ZoneTile>();
		if(nRows!=0)
		{	List<Integer> drawnPosR = SetTools.drawPositions(possRows, nRows);
			for(int row: drawnPosR)
			{	List<Integer> drawnPosC = SetTools.drawPositions(possCols, nCols);
				for(int col: drawnPosC)
				{	ZoneTile instance = new ZoneTile(row, col);
					result.add(instance);
				}
			}
		}
		else
		{	List<ZoneTile> temp = new ArrayList<ZoneTile>();
			for(int row: possRows)
			{	for(int col: possCols)
				{	ZoneTile instance = new ZoneTile(row, col);
					temp.add(instance);
				}
			}
			result = SetTools.drawPositions(temp, nCols);
		}
		
		// init instance tiles
		for(ZoneTile instance: result)
		{	// constant parts
			String floorStr = tile.getFloor();
			instance.setFloor(floorStr);
			String blockStr = tile.getBlock();
			instance.setBlock(blockStr);
			String itemStr = tile.getItem();
			instance.setItem(itemStr);
			String bombStr = tile.getBomb();
			instance.setBomb(bombStr);
			
			// variable part
			String name = tile.getVariable();
			if(name!=null)
			{	VariableTile vt = variableTiles.get(name);
				ValueTile vit = vt.getNext();
				String itm = vit.getItem();
				String blck = vit.getBlock();
				String flr = vit.getFloor();
				String bmb = vit.getBomb();
				if(instance.getFloor()==null)
					instance.setFloor(flr);
				if(instance.getBlock()==null)
					instance.setBlock(blck);
				if(instance.getItem()==null)
					instance.setItem(itm);
				if(instance.getBomb()==null)
					instance.setBomb(bmb);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the number of items (both visible en hidden)
	 * in this zone. 
	 * <br/>
	 * <b>Note:</b> Items appearing during the sudden death
	 * are not counted.
	 *  
	 * @return
	 * 		The numbers of items in this zone.
	 */
	public Map<String,Integer> getItemCount()
	{	HashMap<String,Integer> result = new HashMap<String,Integer>();
	
		// matrix
		for(int i=0;i<globalHeight;i++)
		{	for(int j=0;j<globalWidth;j++)
			{	ZoneTile tile = matrix[i][j];
				if(tile!=null)
					updateItemCount(tile,result);
			}
		}
		
		// events
//		for(List<ZoneTile> list: eventsInit.values())
//    	{	for(ZoneTile tile: list)
//	    	{	updateItemCount(tile,result);
//	    	}
//		}
		
		return result;
	}
	
	/**
	 * Updates the specified item count depending on the specified tile.
	 * 
	 * @param tile
	 * 		Tile to be considered.
	 * @param result
	 * 		Resulting item count.
	 */
	private void updateItemCount(ZoneTile tile, Map<String,Integer> result)
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
