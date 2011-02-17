package org.totalboumboum.ai.v201011.ais.caliskanseven.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class PathFinding {
	public boolean canReachHeros(CaliskanSeven ai) throws StopRequestException, LimitReachedException{
		//Checking if we can reach at least one of the heros
		BasicCostCalculator bsc = new BasicCostCalculator();
		BasicHeuristicCalculator bhc = new BasicHeuristicCalculator();
		
		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				bsc,bhc);
		
		List<AiHero> heros = ai.getPercepts().getHeroes();
		
		for(AiHero h:heros){
			if(h.equals(ai.getPercepts().getOwnHero()))
				continue;
			
			AiPath p = astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(),
					h.getTile());
			
			if(p==null){
				continue;
			}
			
			if(!p.isEmpty())
				return true;
		}
		
		return false;
	}
	
	public AiPath findPath(CaliskanSeven ai,AiTile target,double[][] matrix) throws StopRequestException, LimitReachedException{
		MatrixCostCalculator bsc = new MatrixCostCalculator(matrix);
		BasicHeuristicCalculator bhc = new BasicHeuristicCalculator();
		//Path finding with Astar algorithm , the target and matrix 
		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				bsc,bhc);
		
		return astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), target);
		
	}
	
	public AiTile findTarget(CaliskanSeven ai,double matrix[][]){
		AiTile tile = ai.getPercepts().getOwnHero().getTile();
		AiHero hero = ai.getPercepts().getOwnHero();
		ArrayList<AiTile> open = new ArrayList<AiTile>();
		ArrayList<AiTile> closed = new ArrayList<AiTile>();
		
		open.add(tile);
		//Putting into the list all possible tiles that can be reached 
		while(!open.isEmpty()){
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for(AiTile t:open){
				for(Direction direction: Direction.getPrimaryValues())
				{	AiTile neighbor = t.getNeighbor(direction);
					if(neighbor.isCrossableBy(hero) && !closed.contains(neighbor))
						toopen.add(neighbor);			
				}
				closed.add(t);
			}
			for(AiTile t:closed){
				if(open.contains(t)){
					open.remove(t);
				}
			}
			for(AiTile t:toopen){
				open.add(t);
			}
		}
	
		
		
		
		// Finding the most valuable tile in the list
		double mp = matrix[closed.get(0).getLine()][closed.get(0).getCol()];
		int ml = closed.get(0).getLine();
		int mc = closed.get(0).getCol();
		for(AiTile t:closed){
			if (mp<matrix[t.getLine()][t.getCol()]){
				mp = matrix[t.getLine()][t.getCol()];
				ml = t.getLine();
				mc = t.getCol();
			}
		}
		
		tile = ai.getPercepts().getTile(ml, mc);
		
		return tile;
		
		
		
	}
}
