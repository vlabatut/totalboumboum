package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class MoveHandler extends AiMoveHandler<CinarYalcin>
{	
	
	
	/** our zone*/
	protected AiZone zone = null;
	/** our hero */
	protected AiHero ownHero = null;
	/** our current tile*/
	protected AiTile currentTile = null;
	/**our next direction */
	protected AiTile nextTile = null;
	/**safe tile */
	protected AiTile safeTile = null;
	/** */
	protected Astar astarPrecise = null;
	/** */
	protected Astar astarApproximation = null;
	/** */
	protected Dijkstra dijkstra = null;	
	/**  */
	protected AiTile safeTilekont = null;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(CinarYalcin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
		costCalculator.setOpponentCost(500); // on assimile la traversée d'un adversaire à un détour de 1 seconde
		costCalculator.setMalusCost(1000);
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
	

	

	/**
	 * @return returns true if there is a tile secure 
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	protected boolean getSafeTile2() throws StopRequestException, LimitReachedException{
		ai.checkInterruption();
		boolean result = false;
		AiBomb ownBomb = ownHero.getBombPrototype();
		AiPath kontrol = null;
		List<AiBomb> bombL = null;
		Set<AiTile> list = new TreeSet<AiTile>();
		AiBomb baska = null;
		int kont=0;
		int kont2=0;
		
		for(int i=-5;i<5;i++)
		{	
			ai.checkInterruption();	
			for(int y=-5;y<5;y++)
			{
				ai.checkInterruption();
				int row=ownHero.getRow()+i;
				int col=ownHero.getCol()+y;
				ai.checkInterruption();
			
				if((0<row)&&(0<col)&&(row<zone.getHeight())&&(col<zone.getWidth()))
				{
					if(zone.getTile(row,col).isCrossableBy(ownHero, false, false, false, false, true, true))
						list.add(zone.getTile(row,col));
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
			 kontrol= astarPrecise.startProcess(ownLoca, ilk);
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
			try{
				kontrol= astarPrecise.startProcess(ownLoca, ilk);
			}catch(LimitReachedException e){
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

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	this.ai.checkInterruption();
	
	zone = this.ai.getZone();
	ownHero = zone.getOwnHero();
	ArrayList<AiTile> safeTiles=ai.updateSafeTiles(ownHero,null,false,false);
	ArrayList<AiTile> currentDangerousTiles=ai.getCurrentDangerousTiles();
	

	Map<Float,List<AiTile>> utilitiesByValue =  ai.utilityHandler.getUtilitiesByValue();
	TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
	Map<AiTile,Float> utilitiesByTile =  ai.utilityHandler.getUtilitiesByTile();
	Iterator<Float> it = values.iterator();
	boolean herocontrol=false;
	for(AiHero hero: ai.getZone().getRemainingOpponents())
	{
		ai.checkInterruption();
		if(ai.ownZoneSearch.contains(hero.getTile()))
			herocontrol=true;
	}
	
	
	if(!herocontrol)
	{
		if(currentDestination==null || currentDestination.equals(ownHero.getTile())||currentDangerousTiles.contains(currentDestination))
		{
			float utility=0;
			while(it.hasNext())
			{
				ai.checkInterruption();	
				utility = it.next();
			}
		    
			if(utility>0)
			{
				List<AiTile> tiles = utilitiesByValue.get(utility);	
				Collections.sort(tiles);	
				if(!tiles.isEmpty()&&(safeTiles.contains(tiles.get(0))))
				{	
					currentDestination = tiles.get(0);
				}
			}
			else{
				AiTile runForrest=ownHero.getTile();
				int tiledist = 10000;
				for(AiTile tile:safeTiles)
				{	
					ai.checkInterruption();
					int dist=ai.getDist(tile, ownHero.getTile());
					if(tiledist>dist)
					{
						tiledist=dist;
						runForrest=tile;
					}
				}
				currentDestination=runForrest;
			}
		}
	}
	
	else{
		if(ai.updateSafeTiles(ownHero,null,false,false).isEmpty()||ai.containsNull(safeTiles))
		{
			ArrayList<AiBomb> bombList=new ArrayList<AiBomb>();
			Set<AiTile> reachableLand=ai.getTilesReachableWithBombs(ownHero);
			AiTile itsmytile = null;
			if(!zone.getBombs().isEmpty())
			{
				for(AiBomb bomb:zone.getBombs())
				{
					ai.checkInterruption();
					if(reachableLand.contains(bomb.getTile())&&!bombList.contains(bomb))
						bombList.add(bomb);
					else{
						for(AiTile tileb:bomb.getBlast())
						{
							ai.checkInterruption();
							if(reachableLand.contains(tileb)&&!bombList.contains(bomb))
								bombList.add(bomb);
						}
					}
					
				}
				AiBomb itsmybomb = null;
				long timeexplosion=0;
				
				for(AiBomb bomb:bombList)
				{
					ai.checkInterruption();
					long time=ai.processBomb(bomb);
					
					if(timeexplosion<time)
					{
						itsmybomb=bomb;
						timeexplosion=time;
					}
						
				}
				
				bombList.remove(itsmybomb);
				ArrayList<AiTile> bombsafe=new ArrayList<AiTile>();
				if(itsmybomb!=null)
				{
					for(AiTile tile:itsmybomb.getBlast())
					{
						ai.checkInterruption();
						if(!bombsafe.contains(tile))
							bombsafe.add(tile);
					}
				}
				
				for(AiBomb bomb:bombList)
				{
					ai.checkInterruption();
					for(AiTile tile:bomb.getBlast())
					{
						ai.checkInterruption();
						if(bombsafe.contains(tile))
							bombsafe.remove(tile);
					}
				}
				
				int distanceoftile=10000;
				if(!bombsafe.isEmpty())
				{
					for(AiTile tile: bombsafe)
					{
						ai.checkInterruption();
						
						int dist=ai.getDist(tile, ownHero.getTile());
						if(distanceoftile>dist&&reachableLand.contains(tile))
						{
							distanceoftile=dist;
							itsmytile=tile;
						}
					}
				}
				
			}
			
			currentDestination=itsmytile;
		}
		else
		{
			float utility = 0;
			List<AiTile> tiles = null;
			
			while(it.hasNext())
			{
				ai.checkInterruption();	
				utility = it.next();			
			}
			tiles = utilitiesByValue.get(utility);
			Collections.sort(tiles);
				if(utility>0)
				{
					if(utilitiesByTile.get(ownHero.getTile())!=null)
					{
						if(utilitiesByTile.get(ownHero.getTile())!=utility)
						{
							if(!tiles.isEmpty())
							{	
								if(safeTiles.contains(tiles.get(0)))
								currentDestination = tiles.get(0);
							}
						}
						else currentDestination=ownHero.getTile();
					}
					else
					{
						if(!tiles.isEmpty())
						{	
							if(safeTiles.contains(tiles.get(0)))
							currentDestination = tiles.get(0);
						}
					}

				}
				else
				{	
					AiTile runForrest=ownHero.getTile();
					int tiledist = 10000;
					for(AiTile tile:safeTiles)
					{	
						ai.checkInterruption();
						int dist=ai.getDist(tile, ownHero.getTile());
						if(tiledist>dist)
						{
							tiledist=dist;
							runForrest=tile;
						}
					}
					currentDestination=runForrest;
					
				}
			
		}	
	}
		return currentDestination;
}
	

/**
 * your hero's walking speed
 */
private double currentSpeed = 0;
	
	/**
	 * @param tile
	 * @return
	 * 		returns true if selected tile is dangerous for passing by
	 * @throws StopRequestException
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
			long timeRemaining = bomb.getNormalDuration() - bomb.getElapsedTime();
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	this.ai.checkInterruption();
	
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
		
		
		currentPath = astarPrecise.startProcess(ownLoca, safeTile);
		
		
	} catch (LimitReachedException e) {
		e.printStackTrace();
	}
	}
}

if(safeTile==null)
{
	try
	{	currentPath = astarPrecise.startProcess(ownLocation,currentDestination);
	
	}
	catch (LimitReachedException e)
	{	//e.printStackTrace();
	}
	if(currentPath==null || currentPath.isEmpty())
	{
		
		
			try
			{	currentPath = astarApproximation.startProcess(ownLocation,currentDestination);
			
			
			}
			catch (LimitReachedException e)
			{	e.printStackTrace();
			}
	}
	
	
	
}
	
		
		return currentPath;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException
	{	
				
			ai.checkInterruption();
		
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
				else currentDirection=Direction.NONE;
			}else currentDirection=Direction.NONE;
		
			int cont=0;
			int cont2 = 0;
		
		    if(!bombL.isEmpty())
		    {
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
			
			
			
			if(nextTile==null || !nextTile.getFires().isEmpty() || nextTile.equals(currentTile))
				currentDirection = Direction.NONE;
		    }	
		    
		    if(!currentTile.getNeighbor(currentDirection).getFires().isEmpty())
		    	currentDirection=Direction.NONE;
		    
				return currentDirection;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
