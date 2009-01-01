package fr.free.totalboumboum.engine.container.zone;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.container.tile.ValueTile;
import fr.free.totalboumboum.engine.container.tile.VariableTile;

public class Zone implements Serializable
{	private static final long serialVersionUID = 1L;

	private int globalWidth;
	private int globalHeight;
	
	public Zone(int globalWidth, int globalHeight)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// VARIABLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,VariableTile> variableTiles;
	
	public void setVariableTiles(HashMap<String,VariableTile> variables)
	{	this.variableTiles = variables;		
	}
	public HashMap<String,VariableTile> getVariableTiles()
	{	return variableTiles;		
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<ZoneTile> tiles = new ArrayList<ZoneTile>();
	
	public void addTile(ZoneTile tile)
	{	tiles.add(tile);
	}	
	
	/////////////////////////////////////////////////////////////////
	// ZONE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<String[][]> matrices;
	
	public ArrayList<String[][]> getMatrices()
	{	return matrices;		
	}
	public void makeMatrix()
	{	// init
		matrices = new ArrayList<String[][]>();
		String[][] floors = new String[globalHeight][globalWidth];
		String[][] blocks = new String[globalHeight][globalWidth];
		String[][] items = new String[globalHeight][globalWidth];
		for(int i=0;i<globalHeight;i++)
		{	for(int j=0;j<globalWidth;j++)
			{	floors[i][j] = null;
	        	blocks[i][j] = null;
	        	items[i][j] = null;
			}
		}
		
		Iterator<Entry<String,VariableTile>> iter = variableTiles.entrySet().iterator();
		while(iter.hasNext())
		{	Entry<String,VariableTile> temp = iter.next();
			VariableTile value = temp.getValue();
			value.init();
		}
		
		// matrix
    	Iterator<ZoneTile> it = tiles.iterator();
    	while(it.hasNext())
    	{	String[] content = {null,null,null};
    		ZoneTile tile = it.next();
    		int col = tile.getCol();
    		int line = tile.getLine();
    		// variable tile
    		String name = tile.getVariable();
    		if(name!=null)
    		{	VariableTile vt = variableTiles.get(name);
    			ValueTile vit = vt.getNext();
    			String itm = vit.getItem();
				String blck = vit.getBlock();
				String flr = vit.getFloor();
				content[0] = flr;
				content[1] = blck;
				content[2] = itm;
    		}
    		// constant tile
    		else
    		{	content[0] = tile.getFloor();
    			content[1] = tile.getBlock();
    			content[2] = tile.getItem();     			
    		}
    		// values
			{	// floor
				floors[line][col] = content[0];
				// block
				blocks[line][col] = content[1];
				// item
				items[line][col] = content[2];
    		}
    	}
		
		// result
    	matrices.add(floors);
    	matrices.add(blocks);
    	matrices.add(items);
	}
	
	public HashMap<String,Integer> getItemCount()
	{	HashMap<String,Integer> result = new HashMap<String,Integer>();
		String[][] matrix = matrices.get(2);
		for(int i=0;i<globalHeight;i++)
		{	for(int j=0;j<globalWidth;j++)
			{	if(matrix[i][j]!=null)
				{	int value;
					if(result.containsKey(matrix[i][j]))
					{	value = result.get(matrix[i][j]);
						value ++;
					}
					else
						value = 1;
					result.put(matrix[i][j],value);
				}
			}
		}
		return result;
	}
}
