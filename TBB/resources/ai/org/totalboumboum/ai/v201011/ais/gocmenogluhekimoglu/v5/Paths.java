package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class Paths {
	static void colorTiles(GocmenogluHekimoglu ai,List<AiTile> tiles,Color c) throws StopRequestException{
		ai.checkInterruption();
		for(AiTile t:tiles){
			ai.checkInterruption();
			ai.getOutput().setTileColor(t, c);
		}
	}
	
	static AiTile kthHighestTile(GocmenogluHekimoglu ai,HashMap<AiTile,Double> accesibleTiles,int k) throws StopRequestException{
		ai.checkInterruption();
		
		AiTile result = null;
		HashMap<AiTile,Double> atiles = new HashMap<AiTile,Double>(accesibleTiles);
		
		for(int i=0;i<k;i++){
			ai.checkInterruption();
			Double maxval = -9999.0;
			for(AiTile t:atiles.keySet()){
				ai.checkInterruption();
				if(accesibleTiles.get(t) > maxval){
					result = t;
					maxval = atiles.get(t);
				}
			}
			atiles.remove(result);
		}
		
		return result;
	}
	
	static AiPath findPath(GocmenogluHekimoglu ai,AiTile target) throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		
		BasicCostCalculator bsc = new BasicCostCalculator();
		BasicHeuristicCalculator bhc = new BasicHeuristicCalculator();

		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				bsc,bhc);
		
		return astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), target);
		
	}
	
	static boolean areEnemiesAccesible(GocmenogluHekimoglu ai,List<AiTile> accesibleTiles) throws StopRequestException{
		ai.checkInterruption();
		
		for(AiHero h:ai.getPercepts().getHeroes()){
			ai.checkInterruption();
			if(h.equals(ai.getPercepts().getOwnHero())  || h.hasEnded())
				continue;
			
			if(accesibleTiles.contains(h.getTile()))
				return true;
		}
		return false;
	}
	
	static List<AiTile> accesibleTiles(GocmenogluHekimoglu ai,AiHero hero,boolean simBomb,boolean expBombs) throws StopRequestException{
		ai.checkInterruption();
		
		ArrayList<AiTile> lookup = new ArrayList<AiTile>();
		ArrayList<AiTile> accesible = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();
 		
		lookup.add(hero.getTile());
		
		if(expBombs){
			for(AiBomb b:ai.getPercepts().getBombs()){
				ai.checkInterruption();
				
				if(b.getBlast().contains(hero.getTile()))
					continue;
				
				for(AiTile t:b.getBlast()){
					blastflamelist.add(t);
				}
			}
			
			for(AiFire f:ai.getPercepts().getFires()){
				ai.checkInterruption();
				blastflamelist.add(f.getTile());
			}
		}
		
 
		while(!lookup.isEmpty()){
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for(AiTile t:lookup){
				for(Direction direction: Direction.getPrimaryValues())
				{	
					ai.checkInterruption();
					AiTile neighbor = t.getNeighbor(direction);
					
					if(expBombs){
						if(neighbor.isCrossableBy(hero) && !accesible.contains(neighbor)){
							if(!blastflamelist.contains(neighbor)){
								toopen.add(neighbor);	
							}
						}
					}else if(neighbor.isCrossableBy(hero) && !accesible.contains(neighbor)){
						toopen.add(neighbor);		
					}
					
				}
				accesible.add(t);
			}
			for(AiTile t:accesible){
				ai.checkInterruption();
				if(lookup.contains(t)){
					lookup.remove(t);
				}
			}
			for(AiTile t:toopen){
				ai.checkInterruption();
				lookup.add(t);
			}
		} 
		
		if(!simBomb)
			return accesible;
		
		AiBomb b = hero.getBombPrototype();
		accesible.removeAll(b.getBlast());
		return accesible;
	}
}
