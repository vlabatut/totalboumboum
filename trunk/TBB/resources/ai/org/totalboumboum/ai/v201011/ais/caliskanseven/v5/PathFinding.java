package org.totalboumboum.ai.v201011.ais.caliskanseven.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class PathFinding {
	public boolean canReachHeros(CaliskanSeven ai) throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		//Checking if we can reach at least one of the heros
		BasicCostCalculator bsc = new BasicCostCalculator();
		BasicHeuristicCalculator bhc = new BasicHeuristicCalculator();
		
		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				bsc,bhc);
		
		List<AiHero> heros = ai.getPercepts().getHeroes();
		
		for(AiHero h:heros){
			ai.checkInterruption();
			
			if(h.equals(ai.getPercepts().getOwnHero()))
				continue;
			
			if(h.hasEnded())
				continue;
			
			AiPath p = null;
			try{
			p = astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(),
					h.getTile());
			}catch(Exception e){ 
				
			}
			
			if(p==null){
				continue;
			}
			
			if(!p.isEmpty())
				return true;
		}
		
		return false;
	}
	
	public AiPath findPath(CaliskanSeven ai,AiTile target,double[][] matrix) throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				new BasicCostCalculator(),
				new BasicHeuristicCalculator());
		
		return astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), target);
		
	}
	
	public boolean canEscapeBomb(List<AiTile> closed){
		if(closed.size() > 0)
			return true;
		else return false;
	}
	
	
	
	public List<AiTile> listEscapeBomb(CaliskanSeven ai) throws StopRequestException{
		ai.checkInterruption();
		AiTile tile = ai.getPercepts().getOwnHero().getTile();
		AiHero hero = ai.getPercepts().getOwnHero();
		ArrayList<AiTile> open = new ArrayList<AiTile>();
		ArrayList<AiTile> closed = new ArrayList<AiTile>();
		List<AiTile> blist = Util.getBlastList(ai,false);
		open.add(tile);
		//Putting into the list all possible tiles that can be reached 
		while(!open.isEmpty()){
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for(AiTile t:open){
				for(Direction direction: Direction.getPrimaryValues())
				{	
					ai.checkInterruption();
					AiTile neighbor = t.getNeighbor(direction);
					if(neighbor.isCrossableBy(hero) && !closed.contains(neighbor) && !blist.contains(neighbor))
						toopen.add(neighbor);			
				}
				closed.add(t);
			}
			for(AiTile t:closed){
				ai.checkInterruption();
				if(open.contains(t)){
					open.remove(t);
				}
			}
			for(AiTile t:toopen){
				ai.checkInterruption();
				open.add(t);
			}
		} 
		
		
		int bombrange = hero.getBombRange();
		int h_line = hero.getLine();
		int h_col = hero.getCol();
		int maxline = ai.getPercepts().getHeight()-1;
		int maxcol = ai.getPercepts().getWidth()-1;
		for(int a = 0; a<= bombrange; a++){
			ai.checkInterruption();
			AiTile t1 = ai.getPercepts().getTile(Math.min(h_line+a,maxline), h_col);
			if(closed.contains(t1))
				closed.remove(t1);
			
			AiTile t2 = ai.getPercepts().getTile(Math.max(h_line-a,0), h_col);
			if(closed.contains(t2))
				closed.remove(t2);
			
			AiTile t3 = ai.getPercepts().getTile(h_line, Math.min(h_col+a,maxcol));
			if(closed.contains(t3))
				closed.remove(t3);
			
			AiTile t4 = ai.getPercepts().getTile(h_line, Math.max(h_col-a,0));
			if(closed.contains(t4))
				closed.remove(t4);
		}
		
		
		
		
		 
		
		return closed;
		
	}
	
	public AiTile findTarget(CaliskanSeven ai,double matrix[][]) throws StopRequestException{
		ai.checkInterruption();
		AiTile tile = ai.getPercepts().getOwnHero().getTile();
		AiHero hero = ai.getPercepts().getOwnHero();
		ArrayList<AiTile> open = new ArrayList<AiTile>();
		ArrayList<AiTile> closed = new ArrayList<AiTile>();
		List<AiTile> blist = Util.getBlastList(ai,true);
		open.add(tile);
		//Putting into the list all possible tiles that can be reached 
		while(!open.isEmpty()){
			ai.checkInterruption();
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for(AiTile t:open){
				ai.checkInterruption();
				for(Direction direction: Direction.getPrimaryValues())
				{	AiTile neighbor = t.getNeighbor(direction);
					if(neighbor.isCrossableBy(hero) && !closed.contains(neighbor) && !blist.contains(neighbor))
						toopen.add(neighbor);			
				}
				closed.add(t);
			}
			for(AiTile t:closed){
				ai.checkInterruption();
				if(open.contains(t)){
					open.remove(t);
				}
			}
			for(AiTile t:toopen){
				ai.checkInterruption();
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
