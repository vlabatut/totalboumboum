package org.totalboumboum.ai.v201213.ais.saglamseven.v4;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;




/**
 * @author cihan.seven
 *
 */
public class MoveHandler extends AiMoveHandler<SaglamSeven>{
	/**
	 * 
	 */
	protected AiHero ownHero;
	/**
	 * 
	 */
	protected AiZone gameZone;
	/**
	 * 
	 */
	protected AiZone coreZone;
	/**
	 */
	protected Astar aStarPrecise;
	
	/**
	 * 
	 */
	protected Astar aStarApproximation;
	
	/**
	 * 
	 */
	protected Dijkstra dijkstra;
	
	/**
	 * 
	 */
	protected AiTile currentTile;
	
	/**
	 * 
	 */
	protected AiTile nextTile;
	
	/**
	 * 
	 */
	protected AiTile secureTile;
	
	/**
	 * 
	 */
	protected AiTile secureTile2;
	
	/**
	 * 
	 */
	private double heroCurrentSpeed ;
	
	
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	protected MoveHandler(SaglamSeven ai) throws StopRequestException {
		super(ai);
		// TODO Auto-generated constructor stub
		this.ai.checkInterruption();
		ownHero = ai.getZone().getOwnHero();
		gameZone = ai.getZone();
		coreZone = null;
		
		//Astar precise calcul 
		CostCalculator costCalculator1 = new TimeCostCalculator(ai, ownHero);
		HeuristicCalculator heuristicCalculator1 = new TimeHeuristicCalculator(ai, ownHero);
		SuccessorCalculator successorCalculator1 = new TimePartialSuccessorCalculator(ai, TimePartialSuccessorCalculator.MODE_NOTREE);
		costCalculator1.setOpponentCost(5000);
		costCalculator1.setMalusCost(3000);
		aStarPrecise = new Astar(ai, ownHero, costCalculator1,heuristicCalculator1, successorCalculator1);
		
		//Astar approximation
		CostCalculator costCalculator2 = new TimeCostCalculator(ai, ownHero);
		HeuristicCalculator heuristicCalculator2 = new TimeHeuristicCalculator(ai, ownHero);
		SuccessorCalculator successorCalculator2 = new ApproximateSuccessorCalculator(ai);
		aStarApproximation = new Astar(ai, ownHero, costCalculator2, heuristicCalculator2, successorCalculator2);
		
		
		// Dijkstra calcul 
		CostCalculator costCalculator3 = new TimeCostCalculator(ai, ownHero);
		SuccessorCalculator successorCalculator3 = new TimePartialSuccessorCalculator(ai, TimePartialSuccessorCalculator.MODE_NOBRANCH);
		costCalculator3.setOpponentCost(5000);
		costCalculator3.setMalusCost(3000);
		//Dijkstra needs only CostCalculator and SuccessorCalculator
		dijkstra = new Dijkstra(ai, ownHero, costCalculator3,successorCalculator3);
	
		
	}

	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		// TODO Auto-generated method stub
		this.ai.checkInterruption();
		
		//Set<AiTile> availableTiles = ai.utilityHandler.selectTiles();
		AiTile currDest = null;
		Map<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
		TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
		Iterator<Float> it = values.descendingIterator();
		
		
		while(it.hasNext())
		{
			ai.checkInterruption();	

			float utility = it.next();

			List<AiTile> tiles = utilitiesByValue.get(utility);

			Collections.sort(tiles);	

			if(!tiles.isEmpty())
			{	
				currentDestination = tiles.get(0);
				currDest = currentDestination;
			}

	
		}
		return currDest;
	}

	/**
	 * @param tile
	 * @return threatened : true if tile is threatened
	 * @throws StopRequestException
	 */
	protected boolean isTileThreatened (AiTile tile) throws StopRequestException{
		
		this.ai.checkInterruption();
		boolean threatened = false;
		
		heroCurrentSpeed = ai.getZone().getOwnHero().getCurrentSpeed();
		long crossTime = 0;
		crossTime=Math.round(1000*tile.getSize()/heroCurrentSpeed);
		
		List<AiBomb> zoneBombs = ai.getZone().getBombs();
		Iterator<AiBomb> it = zoneBombs.iterator();
		
		while (it.hasNext() && !threatened){
			ai.checkInterruption();
			AiBomb b = it.next();
			
			long remainingTime = b.getNormalDuration()-b.getElapsedTime();
			if(remainingTime>crossTime){
				List<AiTile> blast = b.getBlast();
				threatened = blast.contains(tile);
			}
		}
		return threatened;
	}
	
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		// TODO Auto-generated method stub
		
		this.ai.checkInterruption();
		currentTile = ai.getZone().getOwnHero().getTile();
		currentPath = null;
		AiLocation heroLocation, location2 = null;
		location2 = new AiLocation(ownHero);
		heroLocation = new AiLocation(ownHero);
		if(secureTile==null && isTileThreatened(currentTile) )
		{
			try
			{	currentPath = dijkstra.processEscapePath(heroLocation);
			
			
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
			
			
			if(currentPath!=null || !currentPath.isEmpty()){
			
				AiLocation lastLocation = currentPath.getLastLocation();
				secureTile=lastLocation.getTile();
				secureTile2=secureTile;
				
			}
		}
		
		if(secureTile!=null)
		{
			
			if(currentTile.equals(secureTile))
				secureTile=null;
			else
			{
			try {
				
				
				currentPath = aStarPrecise.startProcess(location2, secureTile);
				
				
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
			}
		}
		
		if(secureTile==null)
		{
			try
			{	currentPath = aStarPrecise.startProcess(heroLocation, currentDestination);
			
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
			if(currentPath==null || currentPath.isEmpty())
			{
				
				
					try
					{	
						currentPath = aStarApproximation.startProcess(heroLocation, currentDestination);
					}
					catch (LimitReachedException e)
					{	e.printStackTrace();
					}
				}
			
			
			
		}		
		
		return currentPath;
	}

	@Override
	protected Direction updateCurrentDirection() throws StopRequestException {
		// TODO Auto-generated method stub
	/*
		ai.checkInterruption();
		Direction direction = null;
		if ( !this.ai.getCurrentDangerousTiles().contains( this.ai.getZone().getOwnHero().getTile() ) 
				&& this.ai.getCurrentDangerousTiles().contains( 
						this.ai.getZone().getOwnHero().getTile().getNeighbor(direction) ) ) 
			direction = Direction.NONE;
		
		return direction;
	 */
		ai.checkInterruption();
		currentTile = ownHero.getTile();
		List<AiBomb> bombList = ai.getZone().getBombs();		
		if (currentPath != null) {
			long wait = currentPath.getFirstPause();
			if (currentPath.getLength() >= 2 && wait <= 0)
			{
				AiLocation sourceLocation = currentPath.getFirstLocation();
				AiLocation targetLocation = currentPath.getLocation(1);
				currentDirection = ai.getZone().getDirection(sourceLocation.getTile(),targetLocation.getTile());
				nextTile = targetLocation.getTile();
			}
		}
		int controlBombNextTile = 0;
		int controlBombCurrentTile = 0;
		for (int a = 0; a < bombList.size(); a++) {
			ai.checkInterruption();
			if (bombList.get(a).getBlast().contains(nextTile)) {
				controlBombNextTile++;
			}

		}
		for (int a = 0; a < bombList.size(); a++) {
			ai.checkInterruption();

			if (bombList.get(a).getBlast().contains(currentTile)) {
				controlBombCurrentTile++;
			}
		}
		if (controlBombNextTile >= 1 && controlBombCurrentTile == 0) {
			currentDirection = Direction.NONE;
		}
		if (!nextTile.getFires().isEmpty() || nextTile.equals(currentTile)){
			currentDirection = Direction.NONE;
		}
		return currentDirection;
	}
}
