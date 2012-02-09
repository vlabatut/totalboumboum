package org.totalboumboum.ai.v201112.ais.gungorkavus.v3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
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
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<GungorKavus>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected AiZone zone = null;
	protected AiHero ownHero = null;
	protected AiTile currentTile = null;
	protected AiTile nextTile = null;
	protected AiTile safeTile = null;
	protected Astar astarPrecise = null;
	protected Astar astarApproximation = null;
	protected Dijkstra dijkstra = null;	
	protected AiTile safeTilekont = null;

	protected MoveHandler(GungorKavus ai) throws StopRequestException
	{	super(ai);
	ai.checkInterruption();

	verbose = false;

	zone = ai.getZone();
	ownHero = zone.getOwnHero();
	
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

	{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
	costCalculator.setOpponentCost(1000); // on assimile la traversée d'un adversaire à un détour de 1 seconde
	SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOBRANCH);
	dijkstra = new Dijkstra(ai,ownHero, costCalculator,successorCalculator);
	}

	}
	
	
	
	/*
	 * 
	 * getSafeTile2() On controle qu'il y a une tile sure autour de nous (5x5)
	 *  
	 */
	
	protected boolean getSafeTile2() throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		AiBomb ownBomb = ownHero.getBombPrototype();
		AiPath kontrol = null;
		List<AiBomb> bombL = null;
		Set<AiTile> list = new TreeSet<AiTile>();
		AiBomb baska = null;
		int kont=0;
		int kont2=0;
		
		for(int i=0;i<5;i++)
		{
			
			ai.checkInterruption();
			
		for(int y=0;y<5;y++)
		{
			ai.checkInterruption();
			
			if(ownHero.getRow()+i<zone.getHeight() && ownHero.getCol()+y<zone.getWidth())
			{
			if(zone.getTile(ownHero.getRow()+i, ownHero.getCol()+y).isCrossableBy(ownHero, false, false, false, false, true, true))
				list.add(zone.getTile(ownHero.getRow()+i, ownHero.getCol()+y));
			}
			if(ownHero.getRow()-i>0 && ownHero.getCol()+y<zone.getWidth())
			{
			if(zone.getTile(ownHero.getRow()-i, ownHero.getCol()+y).isCrossableBy(ownHero, false, false, false, false, true, true))
				list.add(zone.getTile(ownHero.getRow()-i, ownHero.getCol()+y));
			}
			
			if(ownHero.getRow()-i>0 && ownHero.getCol()-y>0)
			{
			if(zone.getTile(ownHero.getRow()-i, ownHero.getCol()-y).isCrossableBy(ownHero, false, false, false, false, true, true))
				list.add(zone.getTile(ownHero.getRow()-i, ownHero.getCol()-y));
			}
			if(ownHero.getRow()+i<zone.getHeight() && ownHero.getCol()-y>0)
			{
			if(zone.getTile(ownHero.getRow()+i, ownHero.getCol()-y).isCrossableBy(ownHero, false, false, false, false, true, true))
				list.add(zone.getTile(ownHero.getRow()+i, ownHero.getCol()-y));
			}
			
		}
		}
		
		
		Iterator<AiTile> it = list.iterator();
		AiTile ilk = null;
		AiLocation ownLoca = new AiLocation(ownHero);
		bombL = zone.getBombs();
		while(it.hasNext())
		{
			ai.checkInterruption();
			ilk=it.next();
		
		try {
			 kontrol= astarPrecise.processShortestPath(ownLoca, ilk);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
		
		if(bombL.isEmpty())
		{
		if(kontrol!=null && !ownBomb.getBlast().contains(ilk)){
			kont2++;
			
		}
		}
		
		} //while
	
		while(it.hasNext())
		{
			ai.checkInterruption();
			ilk=it.next();
		
		try {
			 kontrol= astarPrecise.processShortestPath(ownLoca, ilk);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
		
		
		{
			if(!bombL.isEmpty())
			for(int i=0;i<bombL.size();i++)
			{
				ai.checkInterruption();
				baska=bombL.get(i);
				if(kontrol!=null && !ownBomb.getBlast().contains(ilk) && !baska.getBlast().contains(ilk)){
						kont++;
				}
				
			}
		}
			
		}//while
		
		
		
		if(kont>0 || kont2>0){
			result=true;
		}
			
	
		return result;
		
	}
	
	
	
	
	
	/*
	 * 
	 * updateDestination()
	 */
	
	
	protected void updateDestination() throws StopRequestException{
		this.ai.checkInterruption();
		
		zone = this.ai.getZone();
		ownHero = zone.getOwnHero();
		

		HashMap<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
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
			}

			
			
			
		}

		

	}
	
	private double currentSpeed = 0;
	
	/*
	 * isTileThreatened(AiTile tile)
	 * 
	 */
	
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		currentSpeed=ownHero.getWalkingSpeed();
		long crossTime = 0;
		
		
		crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		
		
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getTime();
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
	
	/*
	 * 
	 * updatePath()
	 * 
	 */
	
	protected void updatePath() throws StopRequestException {
		this.ai.checkInterruption();
		
		currentTile = ownHero.getTile();
		currentPath = null;
		AiLocation ownLocation = new AiLocation(ownHero);
		
		AiLocation ownLoca = new AiLocation(ownHero); 

		
		if(isTileThreatened(currentTile) && safeTile==null)
		{
			try
			{	currentPath = dijkstra.processEscapePath(ownLocation);
			
			
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
			
			
			if(currentPath!=null || !currentPath.isEmpty()){
			
				AiLocation lastLocation = currentPath.getLastLocation();
				safeTile=lastLocation.getTile();
				safeTilekont=safeTile;
				
			}
		}
		
		if(safeTile!=null)
		{
			
			if(currentTile.equals(safeTile))
				safeTile=null;
			else
			{
			try {
				
				
				currentPath = astarPrecise.processShortestPath(ownLoca, safeTile);
				
				
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
			}
		}
		
		if(safeTile==null)
		{
			try
			{	currentPath = astarPrecise.processShortestPath(ownLocation,currentDestination);
			
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
			if(currentPath==null || currentPath.isEmpty())
			{
				
				
					try
					{	currentPath = astarApproximation.processShortestPath(ownLocation,currentDestination);
					
					
					}
					catch (LimitReachedException e)
					{	e.printStackTrace();
					}
				}
			
			
			
		}
		

	}

	/**
	 * @throws StopRequestException
	 */
	
	/*
	 * 
	 * updateDirection()
	 * 
	 * 
	 */
	protected void updateDirection() throws StopRequestException{
		this.ai.checkInterruption();

		List<AiBomb> bombL = zone.getBombs();
		currentTile=ownHero.getTile();
		

		if(currentPath!=null)
		{	
			long wait = currentPath.getFirstPause();

			if(currentPath.getLength()>=2 && wait<=0) //
			{
				AiLocation source = currentPath.getFirstLocation();	
				AiLocation target = currentPath.getLocation(1);
				currentDirection = zone.getDirection(source.getTile(),target.getTile());
				nextTile=target.getTile();

			}
		}




		int cont=0;
		int cont2 = 0;


		for(int a = 0;a<bombL.size();a++){
			ai.checkInterruption();

			if(bombL.get(a).getBlast().contains(nextTile)){

				cont++;

			}		

		}

		for(int a = 0;a<bombL.size();a++){
			ai.checkInterruption();

			
			if(bombL.get(a).getBlast().contains(currentTile)){
				cont2++;
			}

		}
		
		
		

		/*if(isTileThreatened(nextTile) && !isTileThreatened(currentTile) && cont2==0)
			currentDirection = Direction.NONE;
		*/
		if(cont>=1&&cont2==0){
			currentDirection = Direction.NONE;
		}	
		
		
		
		if(!nextTile.getFires().isEmpty() || nextTile.equals(currentTile))
			currentDirection = Direction.NONE;


	}




	/////////////////////////////////////////////////////////////////
	// PROCESSING	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	this.ai.checkInterruption();

		updateDestination();
		updatePath();
		updateDirection();

	return currentDirection;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT	 /////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();

	super.updateOutput();
	}
}