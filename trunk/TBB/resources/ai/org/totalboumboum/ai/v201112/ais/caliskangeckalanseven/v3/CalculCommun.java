package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class CalculCommun extends AiAbstractHandler<CaliskanGeckalanSeven>{

	
	AiZone zone = null;
	AiHero ownHero = null;
	AiTile currentTile = null;
	protected Astar astarPrecise = null;
	protected Astar astarApproximation = null;	
	AiLocation startLocation = null;
	
	protected CalculCommun(CaliskanGeckalanSeven ai)
			throws StopRequestException {
		
		super(ai);
		ai.checkInterruption();
	    zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		startLocation = new AiLocation(currentTile);
		

		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); // on assimile la traversée d'un adversaire à un détour de 1 seconde
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
			astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		{	CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
			astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}
		
	}
	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		
		currentTile = ownHero.getTile();
		
	}
	
	public boolean danger() throws StopRequestException {
		ai.checkInterruption();
		boolean danger = false;
		Set<AiTile> dangerTiles = dangerZone();
		if (dangerTiles.contains(ai.getZone().getOwnHero().getTile())) {
			danger = true;
		}
		return danger;
	}	
	
	public Set<AiTile> safeZone() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE		
		Set<AiTile> safeZone = new TreeSet<AiTile>();
		Set<AiTile>  dangerZone = new TreeSet<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		dangerZone = dangerZone();
		blocks = zone.getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			ai.checkInterruption();
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < zone.getWidth(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < zone.getHeight(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		return safeZone;

	}
	
	
	public Set<AiTile> safeZoneForBomb() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE		
		Set<AiTile> safeZone = new TreeSet<AiTile>();
		Set<AiTile>  dangerZone = new TreeSet<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		dangerZone = dangerZoneForBomb();
		blocks = zone.getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			ai.checkInterruption();
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < zone.getWidth(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < zone.getHeight(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		return safeZone;

	}
	
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
					tempPath = astarPrecise.processShortestPath(startLocation,tempTile);
					if(tempPath != null && checkPathValidity(tempPath)) {
						AiTile tile = tempPath.getLastLocation().getTile();
						AiPath path2 = astarApproximation.processShortestPath(startLocation,tile);
						if(path2 != null && checkPathValidity(path2)) {
							double currentSpeed = ownHero.getWalkingSpeed();
							long timeRemaining = ownHero.getBombDuration();
							long crossTime = Math.round(1000*path2.getLength()*tile.getSize()/currentSpeed);
							if(timeRemaining>crossTime)
								result = true;
						}
						}
					}
					
				catch (LimitReachedException e) {
				}
				
			}
		}
		return result;
	}
	
	
	
	public Set<AiTile> dangerZoneForBomb() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE	
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
	
	private boolean checkPathValidity(AiPath path) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		List<AiLocation> paths = path.getLocations();
		Iterator<AiLocation> it = paths.iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next().getTile();
			if(!tile.isCrossableBy(ownHero) || !isSafe(tile))
				result = false;			
		}
		return result;
	}
	
	private boolean isSafe(AiTile tile) throws StopRequestException {
		ai.checkInterruption();

		boolean result = true;
		if (dangerZone().contains(tile))
			result = false;
		return result;
	}
}
