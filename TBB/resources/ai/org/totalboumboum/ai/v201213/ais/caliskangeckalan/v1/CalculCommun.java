package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe contient quelques méthodes
 * utilisées par les différents gestionnaires.
 * 
 * @author Vincent Labatut
 */
public class CalculCommun extends AiAbstractHandler<CaliskanGeckalan>
{	
	/**
	 * Initialise la classe avec l'IA
	 * passée en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected CalculCommun(CaliskanGeckalan ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		startLocation = new AiLocation(currentTile);
		

		{	//Astar for direct path
			CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); // set cost of rivals 1000
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
			astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		{	//Astar for indirect path
			CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
			astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		{ //Djikstra 
			CostCalculator costCalculator = new TileCostCalculator(ai);
			SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
			dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
		}
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** initialize the hero to null */
	public AiHero ownHero = null;
	/** initialize the map to null*/
	public AiZone zone = null;
	/** initialize the coordinates of our hero to null */
	public AiTile currentTile = null;
	/** Astar for direct path*/
	protected Astar astarPrecise = null;
	/** Astar for indirect path*/
	protected Astar astarApproximation = null;
	/** djikstra*/
	private Dijkstra dijkstra = null;
	/** initialize the location of our hero to null */
	AiLocation startLocation = null;
	/** initialize the speed of our hero to zero*/
	public double currentSpeed = 0; 

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On met à jour quelques variables.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		startLocation = new AiLocation(currentTile);
		currentSpeed = ownHero.getWalkingSpeed();
	}
	
	/////////////////////////////////////////////////////////////////
	// METHODS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @return control of we can bomb. If we put a bomb, can we find a escape path
	 * @throws StopRequestException
	 */
	public Boolean canBomb() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		Set<AiTile>  tiles = safeZoneForBomb();
		if(!tiles.isEmpty())
		{	
			Iterator<AiTile> it = tiles.iterator();
			
			while(it.hasNext() && !result) {
				ai.checkInterruption();
				AiTile tempTile = it.next();
				AiPath tempPath;
				try {
					tempPath = astarPrecise.startProcess(startLocation,tempTile);
					if(tempPath != null) {
						double currentSpeed = ownHero.getWalkingSpeed();
						long timeRemaining = ownHero.getBombDuration();
						long crossTime = Math.round(1000*tempPath.getLength()*tempTile.getSize()/currentSpeed);
						if(timeRemaining>crossTime)
							result = true;
						
					}
				}
					
				catch (LimitReachedException e) {
				}
				
			}
		}
		return result;
	}
/*	this will implement for dropping a bomb to kill an rival
	public Boolean canKill() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		AiBomb bomb = ownHero.getBombPrototype();
		List<AiTile> tiles = bomb.getBlast();
		Iterator<AiTile> tileIt = tiles.iterator();
		while(tileIt.hasNext()) {
			ai.checkInterruption();
			AiTile tile = tileIt.next();
			List<AiHero> heroes = tile.getHeroes();
			Iterator<AiHero> heroesIt = heroes.iterator();
			while(heroesIt.hasNext()) {
				ai.checkInterruption();
				if(!heroesIt.next().equals(ownHero))
					result = true;
			}
			
		}
		
		return result;
	}
	*/
	/**
	 * @return get the safe tiles that we can go
	 * @throws StopRequestException
	 */
	public Set<AiTile> safeZoneForBomb() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE	
		Set<AiTile> safeZone = new TreeSet<AiTile>();
		Map<AiTile, AiSearchNode> map;
		Set<AiTile> dangerZoneForBomb = dangerZoneForBomb();
		try {
			map = dijkstra.startProcess(startLocation);
			Iterator<AiTile> tileIt = map.keySet().iterator();
			while(tileIt.hasNext()) {
				ai.checkInterruption();
				AiTile tile = tileIt.next();
				if(!dangerZoneForBomb.contains(tile))
					safeZone.add(tile);
			}
		} catch (LimitReachedException e) {
		}
		return safeZone;

	}
	/**
	 * @return the blast of our bombs
	 * @throws StopRequestException
	 */
	public Set<AiTile> dangerZoneForBomb() throws StopRequestException {
		ai.checkInterruption(); 	
		Set<AiTile> dangerZone = new TreeSet<AiTile>();
		Collection<AiBlock> blocks = zone.getBlocks();
		
		dangerZone = dangerZone();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		int k = ownHero.getBombRange();
		
		AiTile tempTile = currentTile;
		dangerZone.add(tempTile);
		int i = 0;
		while (i < k && !blocks.contains(tempTile)) {
			ai.checkInterruption();
			AiTile tile = tempTile.getNeighbor(Direction.DOWN);
			tempTile = tile;
			if (!blokTile.contains(tile)) {
				dangerZone.add(tile);
			} else
				break;
			i++;
		}
		i = 0;
		tempTile = currentTile;
		while (i < k && !blocks.contains(tempTile)) {
			ai.checkInterruption();
			AiTile tile = tempTile.getNeighbor(Direction.UP);
			tempTile = tile;
			if (!blokTile.contains(tile)) {
				dangerZone.add(tile);
			} else
				break;
			i++;
		}
		i = 0;
		tempTile = currentTile;
		while (i < k && !blocks.contains(tempTile)) {
			ai.checkInterruption();
			AiTile tile = tempTile.getNeighbor(Direction.RIGHT);
			tempTile = tile;
			if (!blokTile.contains(tile)) {
				dangerZone.add(tile);
			} else
				break;
			i++;
		}
		i = 0;
		tempTile = currentTile;
		while (i < k && !blocks.contains(tempTile)) {
			ai.checkInterruption();
			AiTile tile = tempTile.getNeighbor(Direction.LEFT);
			tempTile = tile;
			if (!blokTile.contains(tile)) {
				dangerZone.add(tile);
			} else
				break;
			i++;
		}
		i = 0;
		tempTile = currentTile;	
		return dangerZone;
	}
	
	/**
	 * @return all danger coordinates
	 * @throws StopRequestException
	 */
	public Set<AiTile> dangerZone() throws StopRequestException {

		ai.checkInterruption();
		AiZone zone = ai.getZone();

		Set<AiTile> dangerZone = new TreeSet<AiTile>();;
		Collection<AiBomb> bombs = zone.getBombs();
		Collection<AiFire> fires = zone.getFires();
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();
		Iterator<AiFire> itfires = fires.iterator();
		while (it_blocks.hasNext()) {
			ai.checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		if (fires.size() > 0) {
			while (itfires.hasNext()) {
				ai.checkInterruption();
				AiFire fire = itfires.next();
				AiTile temp = fire.getTile();
				dangerZone.add(temp);
			}
		}
		Iterator<AiBomb> it1 = bombs.iterator();
		if (bombs.size() > 0) {

			while (it1.hasNext()) {
				ai.checkInterruption();

				AiBomb bomb = it1.next();

				int k = bomb.getRange();
				int x = bomb.getCol();
				int y = bomb.getRow();

				AiTile tempTile = zone.getTile(y, x);
				dangerZone.add(tempTile);
				AiTile tile1 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.DOWN);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;

				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.UP);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.RIGHT);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.LEFT);

					tempTile = tile;
					if (!blokTile.contains(tile)) {

						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
			}

		}
		return dangerZone;
	}
	

	/**
	 * @param tile
	 * @return the tile is in Danger
	 * @throws StopRequestException
	 */
	public boolean isDanger(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		List<AiBomb> bombs = zone.getBombs();
		for(int i=0;i<bombs.size();i++){
			ai.checkInterruption();
			if(bombs.get(i).getBlast().contains(tile)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * @param map
	 * @return the tiles of all Items accessible
	 * @throws StopRequestException
	 */
	public Set<AiTile> getItemsAccessible(Map<AiTile,AiSearchNode> map) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		Iterator<AiTile> tileIt = map.keySet().iterator();
		while(tileIt.hasNext()) {
			ai.checkInterruption();
			AiTile tile = tileIt.next();
			List<AiItem> items = tile.getItems();
			for(int i = 0; i < items.size(); i++) {
				ai.checkInterruption();
				AiItem item = items.get(i);
				if(!item.getType().equals(AiItemType.ANTI_BOMB) && !item.getType().equals(AiItemType.ANTI_FLAME) &&  !item.getType().equals(AiItemType.ANTI_SPEED) ) {
					result.add(item.getTile());
				}
			}
		}
		
		return result;
	}
	
	
	
	/**
	 * @param map
	 * @return all neighboors of murs
	 * @throws StopRequestException
	 */
	public Set<AiTile> getMursNeighBoorsForCollecte(Map<AiTile,AiSearchNode> map) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		Iterator<AiTile> tileIt = map.keySet().iterator();
		while(tileIt.hasNext()) {
			ai.checkInterruption();
			AiTile tileNext = tileIt.next();
			int row = tileNext.getRow();
			int col = tileNext.getCol();
			int rowMap = zone.getHeight()-1;
			int colMap = zone.getWidth()-1;
			if(col+1 <= colMap && tileNext.getNeighbor(Direction.RIGHT).getBlocks().size()> 0 && tileNext.getNeighbor(Direction.RIGHT).getBlocks().get(0).isDestructible()) {
				result.add(tileNext);
			}
			if(col-1 >= 0 && tileNext.getNeighbor(Direction.LEFT).getBlocks().size() > 0 && tileNext.getNeighbor(Direction.LEFT).getBlocks().get(0).isDestructible()) {
				result.add(tileNext);
			}
			if(row + 1 <= rowMap ) {
				if(tileNext.getNeighbor(Direction.DOWN).getBlocks().size() >0 && tileNext.getNeighbor(Direction.DOWN).getBlocks().get(0).isDestructible())
					result.add(tileNext);
			}
			if(row - 1 >= 0 && tileNext.getNeighbor(Direction.UP).getBlocks().size()> 0 &&  tileNext.getNeighbor(Direction.UP).getBlocks().get(0).isDestructible()) {
				result.add(tileNext);
			}
		}
		
		return result;
	}
	
	/**
	 * @param map 
	 * @return the rivals accessibles
	 * @throws StopRequestException
	 */
	public Set<AiTile> getRivalsAccessible(Map<AiTile,AiSearchNode> map) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		Iterator<AiTile> tileIt = map.keySet().iterator();
		while(tileIt.hasNext()) {
			ai.checkInterruption();
			AiTile tile = tileIt.next();
			List<AiHero> heroes = tile.getHeroes();
			for(int i = 0; i < heroes.size(); i++) {
				ai.checkInterruption();
				AiHero hero = heroes.get(i);
				if(!hero.equals(ownHero))
					result.add(hero.getTile());
				
			}
		}
		
		return result;
	}
	
	/**
	 * @return if we cant find a path for rivals, this method sets the mur tiles for the rivals using indirect path
	 * @throws StopRequestException
	 */
	public Set<AiTile> getMursForRivals() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		List<AiHero> rivals = zone.getRemainingHeroes();
		Iterator<AiHero> rivalsIt = rivals.iterator();
		while(rivalsIt.hasNext()) {
			ai.checkInterruption();
			AiHero rival = rivalsIt.next();
			AiTile tempTile = rival.getTile(); 
			AiPath tempPath;
			try {
				tempPath = astarApproximation.startProcess(startLocation,tempTile);
				if(tempPath != null) {
					for(int i=0;i< tempPath.getLength();i++) {
						ai.checkInterruption();
						AiTile tileNext = tempPath.getLocation(i).getTile();
						int row = tileNext.getRow();
						int col = tileNext.getCol();
						int rowMap = zone.getHeight()-1;
						int colMap = zone.getWidth()-1;
						if(col+1 <= colMap && tileNext.getNeighbor(Direction.RIGHT).getBlocks().size()> 0 && tileNext.getNeighbor(Direction.RIGHT).getBlocks().get(0).isDestructible()) {
							result.add(tileNext);
							break;
						}
						if(col-1 >= 0 && tileNext.getNeighbor(Direction.LEFT).getBlocks().size() > 0 && tileNext.getNeighbor(Direction.LEFT).getBlocks().get(0).isDestructible()) {
							result.add(tileNext);
							break;
						}
						if(row + 1 <= rowMap && tileNext.getNeighbor(Direction.DOWN).getBlocks().size() >0 && tileNext.getNeighbor(Direction.DOWN).getBlocks().get(0).isDestructible()) {
							result.add(tileNext);
							break;
						}
						if(row - 1 >= 0 && tileNext.getNeighbor(Direction.UP).getBlocks().size()> 0 &&  tileNext.getNeighbor(Direction.UP).getBlocks().get(0).isDestructible()) {
							result.add(tileNext);
							break;
						}
					}
				}
			}
				
			catch (LimitReachedException e) {
			}	
		}
		
		return result;
	}
	
	
	
}
