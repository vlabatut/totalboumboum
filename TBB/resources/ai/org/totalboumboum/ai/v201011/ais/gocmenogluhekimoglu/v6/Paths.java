package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v6;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * This class has functions related to pathfinding
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 *
 */
public class Paths {
	GocmenogluHekimoglu ai;
	
	public Paths(GocmenogluHekimoglu ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
	}
	
	/**
	 * Debug function, colors tiles in the list
	 * @param tiles
	 * @param c
	 * @throws StopRequestException
	 */
	void colorTiles(List<AiTile> tiles,Color c) throws StopRequestException{
		ai.checkInterruption();
		for(AiTile t:tiles){
			ai.checkInterruption();
			ai.getOutput().setTileColor(t, c);
		}
	}
	
	/**
	 * Find the k. highest valued tile in the given hashmap
	 * @param accesibleTiles
	 * @param k
	 * @return
	 * @throws StopRequestException
	 */
	AiTile kthHighestTile(HashMap<AiTile,Double> accesibleTiles,int k) throws StopRequestException{
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
	
	/**
	 * calculate a path to the target using A-star algorithm
	 * @param target
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	AiPath findPath(AiTile target) throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		
		BasicCostCalculator bsc = new BasicCostCalculator();
		BasicHeuristicCalculator bhc = new BasicHeuristicCalculator();

		Astar astar = new Astar(ai,ai.getPercepts().getOwnHero(),
				bsc,bhc);
		
		return astar.processShortestPath(ai.getPercepts().getOwnHero().getTile(), target);
		
	}
	
	/**
	 * check if we have any enemies in the given list of tiles
	 * @param accesibleTiles
	 * @return
	 * @throws StopRequestException
	 */
	boolean areEnemiesAccesible(List<AiTile> accesibleTiles) throws StopRequestException{
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
	
	/**
	 * check if we are in the blast area of any bomb currently on the map
	 * @return
	 * @throws StopRequestException
	 */
	boolean isOnFire() throws StopRequestException{
		ai.checkInterruption();
		
		for(AiBomb b:ai.getPercepts().getBombs()){
			ai.checkInterruption();
			if(b.getBlast().contains(ai.getPercepts().getOwnHero().getTile()))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Find all the accesible tiles
	 * @param hero hero to be used
	 * @param simBomb simulate our hero's prototype bomb
	 * @param expBombs calculate as if all the bombs have exploded
	 * @return
	 * @throws StopRequestException
	 */
	List<AiTile> accesibleTiles(AiHero hero,boolean simBomb,boolean expBombs) throws StopRequestException{
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
				
				if(!simBomb && b.getTime() <= 1500){
					//chain bomb check
					boolean chain = false;
					for(AiBomb b2:ai.getPercepts().getBombs()){
						ai.checkInterruption();
						if(!b2.equals(b) && b2.getBlast().contains(b) && b.getTime() >= 1500){
							chain = true;
						}
					}
					if(!chain)
						continue;
				}
				
				for(AiTile t:b.getBlast()){
					ai.checkInterruption();
					blastflamelist.add(t);
				}
			}
			
			for(AiFire f:ai.getPercepts().getFires()){
				ai.checkInterruption();
				blastflamelist.add(f.getTile());
			}
		}
		
 
		while(!lookup.isEmpty()){
			ai.checkInterruption();
			ArrayList<AiTile> toopen = new ArrayList<AiTile>();
			for(AiTile t:lookup){
				ai.checkInterruption();
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
		
		AiBomb b = ai.getPercepts().getOwnHero().getBombPrototype();
		accesible.removeAll(b.getBlast());
		return accesible;
	}
	
	/**
	 * Find the distance between two tiles, but using a non cyclic method
	 * @param tile1
	 * @param tile2
	 * @return
	 * @throws StopRequestException
	 */
	int tileDistNonCyc(AiTile tile1,AiTile tile2) throws StopRequestException{
		ai.checkInterruption();
		return Math.abs(tile1.getCol()-tile2.getCol())+Math.abs(tile1.getLine()-tile2.getLine());
	}
}
