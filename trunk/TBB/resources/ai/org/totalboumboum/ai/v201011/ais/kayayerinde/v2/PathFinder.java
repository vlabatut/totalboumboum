package org.totalboumboum.ai.v201011.ais.kayayerinde.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
public class PathFinder {
	private AiZone zone;
	private AiHero ownHero;
	private AiTile loc;
	private List<AiHero> rivals;
	private Astar astar;
	private AiPath path;
	
	public PathFinder(KayaYerinde ai) throws StopRequestException{
		ai.checkInterruption();
		int x, y, xHero, yHero,a=5000, i;
		zone=ai.getZone();
		ownHero=zone.getOwnHero();
		loc=ownHero.getTile();
		xHero=loc.getCol();
		yHero=loc.getLine();
		rivals=new ArrayList<AiHero>();
		rivals=zone.getRemainingHeroes();
		AiHero rival;
		AiTile tile=null;
		Iterator<AiHero> itRival=rivals.iterator();
		for(i=0;itRival.hasNext();i++)
		{
			ai.checkInterruption();
			rival=itRival.next();
			x=rival.getTile().getCol();
			y=rival.getTile().getLine();
			if(a>(x-xHero)*(x-xHero)+(y-yHero)*(y-yHero))
			{
				a=(x-xHero)*(x-xHero)+(y-yHero)*(y-yHero);
				tile=rival.getTile();
			}
		}
		double costMatrix[][]=new double[zone.getHeight()][zone.getWidth()];
		CostCalculator costCalculator = new MatrixCostCalculator(costMatrix);
		HeuristicCalculator heuristicCalculator = new BasicHeuristicCalculator();
		astar=new Astar(ai, ownHero, costCalculator,heuristicCalculator);
		path=new AiPath();
		path=astar.processShortestPath(loc, tile);

		
	}
	
	public AiPath getPath() throws StopRequestException{
		return path;
		
	}

}
