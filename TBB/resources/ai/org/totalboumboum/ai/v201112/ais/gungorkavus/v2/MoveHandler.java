package org.totalboumboum.ai.v201112.ais.gungorkavus.v2;

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
	protected AiTile safeTile2 = null;
	protected Astar astarPrecise = null;
	protected Astar astarApproximation = null;
	protected Dijkstra dijkstra = null;
	protected Set<AiTile> safeTileL=null;
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
	 * Nous n'utilisons pas cette methode maintenant
	 * 
	 */
	
	/*
	 * 
	 * getSafeTile()
	 *  
	 */
	
	protected AiTile getSafeTile() throws StopRequestException{
		this.ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		
		List<AiTile> tileL = zone.getTiles();
		List<AiBomb> bombL = zone.getBombs();
		
		
		safeTileL = new TreeSet<AiTile>();
		

		AiPath kontrolpa = null;
		
		
		
		for(int i = 0;i<tileL.size();i++)
		{
			ai.checkInterruption();
			boolean contt=false;
			int cont = 0;
			
			if(!bombL.isEmpty())
				contt=true;
			
			if(bombL.size()>=1){
				for(int j = 0;j<bombL.size();j++){
					ai.checkInterruption();
					if(bombL.get(j).getBlast().contains(tileL.get(i))){
						cont++;
					}
				}
				
				
				if(cont==0&&tileL.get(i).isCrossableBy(ownHero, false, false, false, false, true, true)&&contt){
					{
						AiLocation ownLoca = new AiLocation(ownHero); 
						try {
							 kontrolpa= astarPrecise.processShortestPath(ownLoca, tileL.get(i));
						} catch (LimitReachedException e) {
							e.printStackTrace();
						}
						
						if(kontrolpa!=null)
						safeTileL.add(tileL.get(i));
					}

				}
			}

		}
		
		
		
		
		if(!safeTileL.isEmpty())
		{	
		Iterator<AiTile> it = safeTileL.iterator();

		AiTile tile12=null;	
		int kontro = 1000;
		
		while(it.hasNext()){
			ai.checkInterruption();
			tile12=it.next();
			
			
			AiLocation ownLoca = new AiLocation(ownHero); 
			try {
				 kontrolpa= astarPrecise.processShortestPath(ownLoca, tile12); //precise değişti
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
			
			
			
			if(zone.getTileDistance(currentTile, tile12)< kontro ){ // değişti zone.getTileDistance(currentTile, safeTile)
				kontro = zone.getTileDistance(currentTile, tile12);
				safeTile2 = tile12;
				
			}
		}
		}else
			safeTile2=null;

		return safeTile2;
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
			for(int a = 0;a<bombL.size();a++){
				ai.checkInterruption();

				if(bombL.get(a).getBlast().contains(nextTile)&&!bombL.get(a).getBlast().contains(currentTile)){
					int cont2 =0;
					for(int b=0;b<bombL.size();b++){
						ai.checkInterruption();
						if(bombL.get(b).getBlast().contains(currentTile)){
							cont++;
						}
						if(bombL.get(b).getBlast().contains(nextTile)){
							cont2++;
						}
					}
					if(cont==0)
						currentDirection = Direction.NONE;
					else{ 
						if(nextTile.getFires().isEmpty()&&cont2<=1)
							currentDirection = Direction.NONE; 
						else 
							currentDirection = Direction.NONE;
					}
				}else if(bombL.get(a).getBlast().contains(currentTile)&&bombL.get(a).getBlast().contains(nextTile)){
					if(zone.getDirection(currentTile, bombL.get(a).getTile())==Direction.UP){
						if(zone.getTile(ownHero.getRow(), ownHero.getCol()+1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()+1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()+1).getFires().isEmpty()){
								currentDirection = Direction.RIGHT;
							}
						}else if(zone.getTile(ownHero.getRow(), ownHero.getCol()-1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()-1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()-1).getFires().isEmpty()){
								currentDirection = Direction.LEFT;
							}
						}else if(zone.getTile(ownHero.getRow()+1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()+1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()+1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.DOWN;
							}
						}else{
							if(zone.getTileDistance(currentTile, bombL.get(a).getTile())>zone.getTileDistance(nextTile, bombL.get(a).getTile())){
								currentDirection=currentDirection.getPrevious().getOpposite();
							}
						}
					}else if(zone.getDirection(currentTile, bombL.get(a).getTile())==Direction.DOWN){
						if(zone.getTile(ownHero.getRow(), ownHero.getCol()+1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()+1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()+1).getFires().isEmpty()){
								currentDirection = Direction.RIGHT;
							}
						}else if(zone.getTile(ownHero.getRow(), ownHero.getCol()-1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()-1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()-1).getFires().isEmpty()){
								currentDirection = Direction.LEFT;
							}
						}else if(zone.getTile(ownHero.getRow()-1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()-1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()-1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.UP;
							}
						}else{
							if(zone.getTileDistance(currentTile, bombL.get(a).getTile())>zone.getTileDistance(nextTile, bombL.get(a).getTile())){
								currentDirection=currentDirection.getPrevious().getOpposite();
							}
						}
					}else if(zone.getDirection(currentTile, bombL.get(a).getTile())==Direction.LEFT){
						if(zone.getTile(ownHero.getRow()-1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()-1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()-1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.UP;
							}
						}else if(zone.getTile(ownHero.getRow()+1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()+1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()+1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.DOWN;
							}
						}else if(zone.getTile(ownHero.getRow(), ownHero.getCol()+1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()+1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()+1).getFires().isEmpty()){
								currentDirection = Direction.RIGHT;
							}
						}else{
							if(zone.getTileDistance(currentTile, bombL.get(a).getTile())>zone.getTileDistance(nextTile, bombL.get(a).getTile())){
								currentDirection=currentDirection.getPrevious().getOpposite();
							}
						}
					}else if(zone.getDirection(currentTile, bombL.get(a).getTile())==Direction.RIGHT){
						if(zone.getTile(ownHero.getRow()-1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()-1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()-1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.UP;
							}
						}else if(zone.getTile(ownHero.getRow()+1, ownHero.getCol()).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow()+1, ownHero.getCol()))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow()+1, ownHero.getCol()).getFires().isEmpty()){
								currentDirection = Direction.DOWN;
							}
						}else if(zone.getTile(ownHero.getRow(), ownHero.getCol()-1).isCrossableBy(ownHero, false, false, false, false, true, true)){
							for(int b=0;b<bombL.size();b++){
								ai.checkInterruption();
								if(bombL.get(b).getBlast().contains(zone.getTile(ownHero.getRow(), ownHero.getCol()-1))){
									cont++;
								}
							}
							if(cont==0&&zone.getTile(ownHero.getRow(), ownHero.getCol()-1).getFires().isEmpty()){
								currentDirection = Direction.LEFT;
							}
						}else{
							if(zone.getTileDistance(currentTile, bombL.get(a).getTile())>zone.getTileDistance(nextTile, bombL.get(a).getTile())){
								currentDirection=currentDirection.getPrevious().getOpposite();
							}
						}
					}

				}
			}
		
			
			if(!nextTile.getFires().isEmpty()||ownHero.getTile().equals(safeTile2))
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