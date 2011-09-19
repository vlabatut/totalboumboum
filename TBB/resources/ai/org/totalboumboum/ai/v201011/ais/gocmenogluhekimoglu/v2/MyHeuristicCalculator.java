package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

public class MyHeuristicCalculator extends HeuristicCalculator
{
	public double[][] blastmatrix;
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * l'heuristique la plus simple consiste à prendre la distance
	 * de Manhattan entre la case courante tile et la case d'arriv�e endTile.
	 * cf. http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29#Distance_sur_des_espaces_vectoriels
	 * 
	 * @param tile	la case concern�e 
	 * @return	la distance de Manhattan entre tile et la plus proche des cases contenues dans endTiles
	 */
	@Override
	public double processHeuristic(AiTile tile) throws StopRequestException
	{	List<AiTile> endTiles = getEndTiles();
		AiZone zone = tile.getZone();
		double result = Integer.MAX_VALUE;
		for(AiTile endTile: endTiles)
		{	
			
			int dist = zone.getTileDistance(tile,endTile);
			if(blastmatrix[endTile.getLine()][endTile.getCol()]!=0){
				dist = Integer.MAX_VALUE;
			}
			if(dist<result)
				result = dist;
		}
		return result;
	}
}
