package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class Move 
{
	AiZone zone;
	int[][] matrix;
	ArtificialIntelligence ai;
	
	public Move(AiZone zone, int[][] matrix, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.matrix=matrix;
	}
	
	//La methode qui trouve la direction suivante en utilisant l'astar pour trouver le chemin le plus interesent
	@SuppressWarnings("deprecation")
	public Direction getDirection()
	{	Cost  cost = new Cost(matrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(ai, zone.getOwnHero(), cost, heuristic);
		AiPath path = null;
		Direction resultat;
		AiTile target = targetTile();
		try 
		{
			path = astar.processShortestPath(zone.getOwnHero().getTile(), target);
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		AiPath pathtempo1 = path;
		AiPath pathtempo2;
		pathtempo2 = path;
		List<AiTile> casesList = new ArrayList<AiTile>();
		casesList = path.getTiles();
		Iterator<AiTile> cases = casesList.iterator();
		AiTile tile;
		while(cases.hasNext())
		{
			tile = cases.next();
			if(tile != zone.getOwnHero().getTile())
			{			
				try {
					pathtempo1 = astar.processShortestPath(zone.getOwnHero().getTile(), tile);
				} catch (StopRequestException e) {
					// 
					e.printStackTrace();
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}
				if(pathtempo1.isShorterThan(pathtempo2) && !pathtempo1.isEmpty())
					pathtempo2 = pathtempo1;
			}
		}
		resultat = zone.getDirection(zone.getOwnHero().getTile(), pathtempo2.getLastTile());
		return resultat;
	}
	
	
	//La methode qui trouve la case qui a la valeur la plus eleve dans la matrice d'interet
	public AiTile targetTile ()
	{
		int max=0, maxi=0, maxj=0;
		AiTile resultat;
		for (int i=0;i<zone.getHeight();i++)
		{
			for(int j=0;j<zone.getWidth();j++)
			{
				if(matrix[i][j]>max)
				{
					maxi=i;
					maxj=j;
					max = matrix[i][j];
				}
			
			}
		}
		resultat= zone.getTile(maxi, maxj);
		return resultat;
	}
}
