package fr.free.totalboumboum.engine.container.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import fr.free.totalboumboum.engine.container.tile.ValueTile;
import fr.free.totalboumboum.engine.container.tile.VariableTile;

public class Zone
{
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
				boolean found = false;
				double proba = Math.random();
				float p = 0;
				ArrayList<ValueTile> valueTiles = vt.getValues();
				Iterator<ValueTile> j = valueTiles.iterator();
				while(j.hasNext() && !found)
				{	ValueTile vit = j.next();
					Float f = vit.getProba();
					String itm = vit.getItem();
					String blck = vit.getBlock();
					String flr = vit.getFloor();
					p = p + f;
					if(proba<=p)
					{	found = true;
						content[0] = flr;
						content[1] = blck;
						content[2] = itm;
					}    
				}
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
						value = 0;
					result.put(matrix[i][j],value);
				}
			}
		}
		return result;
	}
}
