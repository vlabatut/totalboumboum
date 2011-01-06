package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;

/**
 * Represents a coordinate. A coordinate is a tile within the level, with a value associated to it.
 * @author yasa
 *
 */
public class Coordinate
{

	/**
	 * The x (line) value of the coordinate
	 */
	public int x;
	/**
	 * The y (column) value of the coordinate.
	 */
	public int y;
	/**
	 * The value of the coordinate.
	 */
	public int value;
	
	/**
	 * Creates a new coordinate.
	 * @param x Line of the coordinate.
	 * @param y Column of the coordinate.
	 * @param ia AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public Coordinate(int x, int y, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = x;
		this.y = y;
		this.value = 0;
	}
	/**
	 * Creates a new coordinate.
	 * @param x Line of the coordinate.
	 * @param y Column of the coordinate.
	 * @param value Value of the coordinate.
	 * @param ia AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public Coordinate(int x, int y, int value, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = x;
		this.y = y;
		this.value = value;
	}
	/**
	 * Creates a new coordinate.
	 * @param tile The tile corresponding to the coordinate.
	 * @param value Value of the coordinate.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public Coordinate(AiTile tile, int value, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.x = tile.getLine();
		this.y = tile.getCol();
		this.value = value;
	}

	/**
	 * Creates a new coordinate.
	 * @param tile The tile corresponding to the coordinate.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
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
		// TODO Auto-generated method stub
		return "("+this.x+","+this.y+"("+this.value+"))";
	}
}
