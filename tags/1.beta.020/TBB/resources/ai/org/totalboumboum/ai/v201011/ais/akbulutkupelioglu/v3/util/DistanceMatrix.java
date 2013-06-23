package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util;

import java.util.ArrayList;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.breadthfirstsearch.BreadthFirstSearch;

public class DistanceMatrix extends Matrix
{

	private AiTile centerTile;
	private AkbulutKupelioglu monIa;
	public DistanceMatrix(int width, int height, AkbulutKupelioglu ia)
			throws StopRequestException
	{
		super(width, height, ia);
		ia.checkInterruption();
		monIa = ia;
		centerTile = null;
	}

	public AiTile getCenterTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return centerTile;
	}

	public void setCenterTile(AiTile centerTile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.centerTile = centerTile;
	}
	
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
