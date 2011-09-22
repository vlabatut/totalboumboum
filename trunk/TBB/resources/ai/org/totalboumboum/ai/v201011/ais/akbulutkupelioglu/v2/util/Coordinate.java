package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class Coordinate
{

	public int x;
	public int y;
	public int value;
	
	public Coordinate(int x, int y, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = x;
		this.y = y;
		this.value = 0;
	}
	public Coordinate(int x, int y, int value, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = x;
		this.y = y;
		this.value = value;
	}
	public Coordinate(AiTile tile, int value, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = tile.getLine();
		this.y = tile.getCol();
		this.value = value;
	}

	public Coordinate(AiTile tile, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = tile.getLine();
		this.y = tile.getCol();
		this.value = 0;
	}
	
	@Override
	public String toString()
	{
		// 
		return "("+this.x+","+this.y+"("+this.value+"))";
	}
}
