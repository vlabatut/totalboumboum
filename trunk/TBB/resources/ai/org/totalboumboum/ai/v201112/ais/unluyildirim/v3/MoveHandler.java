package org.totalboumboum.ai.v201112.ais.unluyildirim.v3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<UnluYildirim> {

	AiTile hero_destination = null;
	Boolean isBombing = false;
	int wallBombing = 0;

	boolean canRun = true;
	protected MoveHandler(UnluYildirim ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();

		isBombing = true;
		AiZone zone;
		AiHero myhero;
		AiTile hero_tile; // le tile où notre hero se trouve
		AiPath hero_path = null; // la chemin que l'agent va suivre
		Astar astar;

		zone = ai.getZone();
		myhero = zone.getOwnHero();
		hero_tile = myhero.getTile();

		AiLocation hero_location = new AiLocation(hero_tile); // la place current de l'agent
		AiLocation current_location = null; // la place que l'hero va aller
		Direction direction_current = Direction.NONE;// direction que l'agent va suivre

		CostCalculator costCalculator = new TimeCostCalculator(ai, myhero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai, myhero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astar = new Astar(ai, myhero, costCalculator, heuristicCalculator,successorCalculator);

		Dijkstra dijkstra = null;
		dijkstra = new Dijkstra(ai, myhero, costCalculator, successorCalculator);

		HashMap<Float, List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
		TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
		Iterator<Float> it1 = values.descendingIterator();

		AiTile safe_tile = null;
		{
			ai.checkInterruption();
            float utility = it1.next();
			List<AiTile> tiles = utilitiesByValue.get(utility);
			hero_destination = tiles.get(0);
		}

		if ((isDangerous(hero_tile) || isDangerous(hero_destination) || !hero_tile.getBombs().isEmpty()) && safe_tile==null  ) {

			AiLocation startLocation = new AiLocation(myhero);
			{

				if (isDangerous(hero_tile)) {

					
					startLocation = new AiLocation(hero_tile);
					isBombing = false;
				}
			}
			try {

				AiPath pathescape = dijkstra.processEscapePath(startLocation);
				
				if(pathescape==null || pathescape.isEmpty())
				{
				   safe_tile = myhero.getTile();
				}
				else
				{
					safe_tile=pathescape.getLastLocation().getTile();
					if(Math.abs(safe_tile.getCol()-hero_tile.getCol())<=myhero.getBombRange() || Math.abs(safe_tile.getRow()-hero_tile.getCol())<=myhero.getBombRange())
							isBombing =false; 
					
				}
				if(safe_tile!=null)
				{if(Math.abs(safe_tile.getCol()-hero_tile.getCol())<=myhero.getBombRange() || Math.abs(safe_tile.getRow()-hero_tile.getCol())<=myhero.getBombRange())
							isBombing =false; 
				canRun= true;
						
				}
				else
					{
					isBombing=false;
					canRun =false;
					
					}
			}
			
			 catch (LimitReachedException e) {

				// e.printStackTrace();
			}
		
			{

				
				ai.checkInterruption();
				
				try {
					hero_path = astar.processShortestPath(startLocation, safe_tile);
				} catch (LimitReachedException e) {
					
					//e.printStackTrace();
				}
				
				if (hero_path.getLength() == 1 || hero_path.isEmpty() || hero_path==null)
					direction_current = Direction.NONE;

				
				else {
					current_location = hero_path.getLocation(1);
					// print("current locations : "+current_location);
					if (isDangerous(current_location.getTile())) {
						ai.checkInterruption();
						isBombing = false;
					}

					direction_current = zone.getDirection(hero_path.getFirstLocation(),hero_path.getLastLocation());
				}

			}

		}

		else {
			
			if (hero_destination == hero_tile)

			{// Si l'agent a arrivé le destination , il retourne NONE.

				
				
				return Direction.NONE;
				
			} else {
				
				try {
					
					hero_path = astar.processShortestPath(hero_location,hero_destination);
				} catch (LimitReachedException e) { // e.printStackTrace();
				}

				if (hero_path == null || hero_path.isEmpty()) {
					
				}
				
			

		}}
		if(hero_path!=null)
		{
			
			
			long wait = hero_path.getFirstPause();
	
			
			current_location = hero_path.getLocation(1); 
			if(hero_path.getLength()<2 || wait>0 )
				{
			//direction_current =Direction.NONE;
				//direction_current = zone.getDirection(source.getTile(),target.getTile());
				}
			
			
			else
			{	
				
				AiLocation source = hero_path.getFirstLocation();
			    AiLocation target = hero_path.getLocation(1);
				direction_current = zone.getDirection(source.getTile(),target.getTile());
				
				if(isBombing)
				{
					
					try {
						
						AiPath pathescape = dijkstra.processEscapePath(target);
						if(isDangerous(pathescape.getLastLocation().getTile()))
						{
							canRun = false;
						}
					} catch (LimitReachedException e) {
						
						//e.printStackTrace();
					}
				}
				
				}
			}
		
	
		
		

		/*
		 * try { Thread.sleep(myhero.getBombDuration()/10); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */

		return direction_current;

	}
	
	private boolean isDangerous(AiTile tile) throws StopRequestException {

		ai.checkInterruption();
		AiZone zone = ai.getZone();
		// print("isDangerous");
		List<AiBomb> bombs = zone.getBombs();
		for (AiBomb bomb : bombs) {
			ai.checkInterruption();
			if (bomb.getBlast().contains(tile))
				return true;
		}
		return false;

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();

	}
}
