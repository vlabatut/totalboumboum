package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
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
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<CaliskanGeckalan>
{	
	
	/** initialize the zone	 */
	protected AiZone zone = null;
	/** Le personnage contrôlé par l'agent */
	protected AiHero ownHero = null;
	/** La case courante */
	protected AiTile currentTile = null;
	/** L'objet a* utilisé pour le calcul des chemins directs */
	protected Astar astarPrecise = null;
	/** L'objet a* utilisé pour le calcul des chemins indirects */
	protected Astar astarApproximation = null;
	/** L'objet dijkstra utilisé pour le calcul des chemins de fuite */
	protected Dijkstra dijkstra = null;
	/** Le chemin indirect courant */
	protected AiPath indirectPath = null;
	/** La case de fuite courante */
	protected AiTile safeDestination = null;
	/** Indique si l'agent doit poser une bombe pour dégager le chemin indirect */
	protected boolean secondaryBombing = false;
	/** Indique si l'agent doit poser une bombe sur l'objectif */
	protected boolean bombDestination = false;
	/** initialize itemdestination to false, the variable for not dropping bomb */
	protected boolean itemDestination = false;
	/** initialize calculCommun to false, the class of commun functions */
	private CalculCommun calculCommun = null;
	/** Indique si l'agent doit poser une bombe sur l'objectif */
	protected boolean safeBombing = false;
	
	
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	protected MoveHandler(CaliskanGeckalan ai) throws StopRequestException
    {	super(ai);
	ai.checkInterruption();
	
	// on règle la sortie texte pour ce gestionnaire
	verbose = false;
	zone = ai.getZone();
	ownHero = zone.getOwnHero();
	currentTile = ownHero.getTile();
	calculCommun = new CalculCommun(ai);
	calculCommun.update();
	
	
	{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
		costCalculator.setOpponentCost(1000);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
	}

	{	CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
		astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		costCalculator.setMalusCost(1000);
	}

		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
	}

	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException
	{	
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		AiLocation startLocation = new AiLocation(ownHero);
		print("       Update Destination="+currentDestination + " currentTile"+ currentTile  );
		boolean change = false;
		if(currentDestination==null || currentDestination.equals(currentTile))
			change = true;
		if(!change) {
			//getting the values of tiles
			Map<AiTile,Float> utilitiesByTile = ai.utilityHandler.getUtilitiesByTile();
			// getting the value of our target tile
			Float destinationUtility = utilitiesByTile.get(currentDestination);
			if(destinationUtility == null)
				change = true;
		}
		if(change) {
			AiMode mode = ai.modeHandler.getMode();
			//boolean isMenace = calculCommun.isDanger(currentTile); // checking is safe
			if(mode.equals(AiMode.ATTACKING)) {
				
				Map<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
				TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
				Iterator<Float> it1 = values.descendingIterator();
				boolean goOn = true;
				int far = 1000;
				int temp = 1000; 
				while(it1.hasNext() && goOn) // find the maximum tile
				{	ai.checkInterruption();	
					float utility = it1.next();
					List<AiTile> tiles = utilitiesByValue.get(utility);
					if(!tiles.isEmpty())
					{	
						Iterator<AiTile> it = tiles.iterator();
						while(it.hasNext()) {
							ai.checkInterruption();
							AiTile tempTile = it.next();
							AiPath tempPath;
							try {
								tempPath = astarPrecise.startProcess(startLocation,tempTile);
								if(tempPath != null && checkPathValidity(tempPath)) {
									temp = tempPath.getLength();
									if(far>temp) {
										far = temp;
										currentDestination = tempPath.getLastLocation().getTile();
										if(currentDestination.equals(currentTile) && it.hasNext()) {
											goOn = true;										
										}
										else {
											goOn = false;
											safeBombing = false;
										}
											
									bombDestination = true;	
									}
								}
								
							} catch (LimitReachedException e) {
							}
							
						}
					}
					if(currentDestination!=null && !currentDestination.equals(currentTile))
						goOn = false;
				}		
			}
			else {
				Map<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
				TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
				Iterator<Float> it1 = values.descendingIterator();
				
				int far = 1000;
				int temp = 1000; 
				boolean canGo = true;
				while(it1.hasNext() && canGo)
				{	ai.checkInterruption();	
					
					float utility = it1.next();				
					List<AiTile> tiles = utilitiesByValue.get(utility);
				
					if(!tiles.isEmpty())
					{	
						Iterator<AiTile> it = tiles.iterator();
						
						while(it.hasNext()) {
							ai.checkInterruption();
							AiTile tempTile = it.next();
							AiPath tempPath;
							try {
								tempPath = astarPrecise.startProcess(startLocation,tempTile);
								if(tempPath != null && checkPathValidity(tempPath)) {
									temp = tempPath.getLength();
									if(far>temp) {
										far = temp;
										currentDestination = tempPath.getLastLocation().getTile();
										if(currentDestination.getItems() != null && currentDestination.getItems().size()>0)
											bombDestination = false;
										else
											bombDestination = true;
										canGo = false;
									}
								}
								
							} catch (LimitReachedException e) {	
							}
							
						}
					}
				}
			}
				if(currentDestination==null)
				{
					itemDestination = false;
				}
				
		}
		return currentDestination;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException
	{	
		ai.checkInterruption();
	
		secondaryBombing = false;
		//currentPath = null;
		boolean isMenace = calculCommun.isDanger(currentTile); // checking is safe
		if(isMenace && safeDestination == null)
		{				
			AiLocation startLocation = new AiLocation(ownHero);
			try
			{ //find a safe path with djikstra. 
				currentPath = dijkstra.processEscapePath(startLocation);
			}
			catch (LimitReachedException e)
			{
			}
			if(currentPath!=null && !currentPath.isEmpty())
			{
				AiLocation lastLocation = currentPath.getLastLocation();
				safeDestination = lastLocation.getTile();
				currentDestination = safeDestination;
			}
			safeBombing = true;
		}
		else if(isMenace && safeDestination!=null) {
			print("updateCurrentPath thlikedeyiz yol bulma isMenace && safeDestination!=null");
			if(currentTile.equals(safeDestination))
			{	//if our destination is in danger and we have set the safe tile to our tile
				// we find a new path
				print("updateCurrentPath thlikedeyiz yol bulma currentTile.equals(safeDestination)");
				safeDestination = null;
				safeBombing = true;
				try {
					currentPath = dijkstra.processEscapePath(new AiLocation(currentTile));
					safeDestination = currentPath.getLastLocation().getTile();
					print("updateCurrentPath thlikedeyiz yol bulma currentTile.equals(safeDestination)" +" safeDestination:"+safeDestination);
				} catch (LimitReachedException e) {
				}
			}
			
			else
			{	
				print("updateCurrentPath thlikedeyiz yol bulma !currentTile.equals(safeDestination)"+" safeDestination:"+safeDestination +" currentTile:"+currentTile);
				try { // if we set a safeTile, find a path with AStar
					AiLocation startLocation = new AiLocation(ownHero);
					currentPath = astarPrecise.startProcess(startLocation,safeDestination);
					if(currentPath == null) {
						currentPath = dijkstra.processEscapePath(new AiLocation(currentTile));
						safeDestination = currentPath.getLastLocation().getTile();
					}
				}
				catch (LimitReachedException e)
				{	
				}
			}	
		}
		
		else {	
			AiLocation startLocation = new AiLocation(ownHero);
			try //if we are not in danger, find a path 
			{	currentPath = astarPrecise.startProcess(startLocation,currentDestination);
			}
			catch (LimitReachedException e)
			{	
			}
			
			if(currentPath!=null && !currentPath.isEmpty())
			{	
				indirectPath = null;
			}
			else
			{	// if the path is null, find a indirect path
				if(indirectPath==null)
				{	
					try
					{	indirectPath = astarApproximation.startProcess(startLocation,currentDestination);
					}
					catch (LimitReachedException e)
					{	
					}
					
				}
				if(indirectPath != null ) {
					Iterator<AiLocation> it = indirectPath.getLocations().iterator();
					AiTile blockedTile = null;
					AiTile previousTile = null;
					while(it.hasNext() && blockedTile==null)
					{	ai.checkInterruption();
						
						AiLocation location = it.next();
						AiTile tile = location.getTile();
						List<AiBlock> blocks = tile.getBlocks();
						if(!blocks.isEmpty())
							blockedTile = previousTile;
						previousTile = tile;
					}
					if(blockedTile!=null)
					{	
						List<AiBomb> bombs = blockedTile.getBombs();
						if(bombs.isEmpty())
						{	
							safeDestination = blockedTile;
							
							if(safeDestination.equals(currentTile))
							{	
								secondaryBombing = true;
							}
							else
							{	
								try
								{	
									currentPath = astarPrecise.startProcess(startLocation,safeDestination);
								}
								catch (LimitReachedException e)
								{	
								}
							}
						}
					}
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
	{	ai.checkInterruption();
	
		// initialize
		currentDirection = Direction.NONE;
		
		if(currentPath!=null)
		{	
			long wait = currentPath.getFirstPause();
			
			if(currentPath.getLength()<2 || wait>0)
			{
				secondaryBombing = true; //setting the secondary target
			}
			else {
		
					AiLocation source = new AiLocation(currentTile);
					AiLocation target = new AiLocation(ownHero);
					if(currentPath.getLength()>1)
					target = currentPath.getLocation(1);
					currentDirection = zone.getDirection(source,target);			
					
			}
		}
		else {
			secondaryBombing = true;
		}
		return currentDirection;
	}

	/**
	 * 
	 * @param path
	 * @return check a path is safe and is crossable
	 * 		?
	 * @throws StopRequestException
	 */
	private boolean checkPathValidity(AiPath path) throws StopRequestException
	{	ai.checkInterruption();

		boolean result = true;
		List<AiLocation> paths = path.getLocations();
		Iterator<AiLocation> it = paths.iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); 			
			AiTile tile = it.next().getTile();
			if(!tile.isCrossableBy(ownHero) || !isSafe(tile))
				result = false;			
		}
		return result;
	}

	/**
	 * 
	 * @return all danger tiles
	 * 		?
	 * @throws StopRequestException
	 */
	private LinkedList<AiTile> dangerZone() throws StopRequestException {

		ai.checkInterruption();
		AiZone zone = ai.getZone();

		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
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
	 * 
	 * @param tile
	 * @return check a tile is safe
	 * 		?
	 * @throws StopRequestException
	 */
	private boolean isSafe(AiTile tile) throws StopRequestException {
		ai.checkInterruption();

		boolean result = true;
		if (dangerZone().contains(tile))
			result = false;
		return result;
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
