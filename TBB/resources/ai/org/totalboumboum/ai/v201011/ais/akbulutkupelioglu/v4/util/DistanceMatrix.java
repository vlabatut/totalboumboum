package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.util;

import java.util.ArrayList;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.breadthfirstsearch.BreadthFirstSearch;

/**
 * A matrix used to represent distances to a certain tile.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class DistanceMatrix extends Matrix
{

	private AiTile centerTile;
	private AkbulutKupelioglu monIa;
	/**
	 * Creates a new distance matrix.
	 * @param width Width of the matrix.
	 * @param height Height of the matrix.
	 * @param ia the AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public DistanceMatrix(int width, int height, AkbulutKupelioglu ia)
			throws StopRequestException
	{
		super(width, height, ia);
		ia.checkInterruption();
		monIa = ia;
		centerTile = null;
	}

	/**
	 * Gets the center tile of this distance matrix.
	 * @return The center tile.
	 * @throws StopRequestException
	 */
	public AiTile getCenterTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return centerTile;
	}

	/**
	 * Sets the center tile of this distance matrix.
	 * @param centerTile The center tile.
	 * @throws StopRequestException
	 */
	public void setCenterTile(AiTile centerTile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.centerTile = centerTile;
	}
	
	/**
	 * Recalculates the matrix for a given tile. The tile is registered as the center tile.
	 * @param centerTile The tile for which the distances will be calculated.
	 * @throws StopRequestException
	 */
	public void recalculate(AiTile centerTile) throws StopRequestException
	{
		monIa.checkInterruption();
		//long ms = System.currentTimeMillis();
		this.centerTile = centerTile;
		//System.out.println("recalculating distance matrix");
		BreadthFirstSearch search = new BreadthFirstSearch(monIa);
		ArrayList<Coordinate> coords =  search.search(centerTile);
		for(Coordinate coordinate : coords)
		{
			monIa.checkInterruption();
			setElement(coordinate.x, coordinate.y, coordinate.value);
		}
		//System.out.println("distance matrix recalculated. took "+(System.currentTimeMillis()-ms)+"ms.\n");
	}
	
	

}
