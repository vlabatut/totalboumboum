package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
/**
 * Classe gérant le déplacement de l'agent.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent>
{	
	/**
	 * Construit un gestionnaire de deplacement pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai)
    {	
		super(ai);
		ai.checkInterruption();
		double tileCosts[][] = ai.securityHandler.processTileCosts();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		CostCalculator costCalculator;
		HeuristicCalculator heuristicCalculator;
		SuccessorCalculator successorCalculator;

		costCalculator = new ApproximateCostCalculator(ai,ownHero);
		costCalculator.setMalusCost(1000);
		costCalculator.setTileCosts(tileCosts);
		heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		successorCalculator = new ApproximateSuccessorCalculator(ai);
		astarRoaming = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
		
		costCalculator = new TileCostCalculator(ai);;
		costCalculator.setMalusCost(1000);
		costCalculator.setTileCosts(tileCosts);
		heuristicCalculator = new TileHeuristicCalculator(ai);
		successorCalculator = new BasicSuccessorCalculator(ai);		
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
		
		costCalculator = new TimeCostCalculator(ai, ownHero);
		costCalculator.setMalusCost(1000);
		costCalculator.setTileCosts(tileCosts);
		successorCalculator = new TimePartialSuccessorCalculator(ai, SearchMode.MODE_NOBRANCH);
		dijkstra = new Dijkstra(ai, ownHero, costCalculator,successorCalculator);
        
    }

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null; 
	/** A titre d'exemple, je stocke le sprite controlé par cet agent, car on en a aussi souvent besoin */
	private AiHero ownHero = null; 
	/** Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le re-créer à chaque itération */
	private Astar astarRoaming = null;
	/** Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le re-créer à chaque itération */
	private Astar astar = null;	

	/** Création de Dijkstra */
	private Dijkstra dijkstra = null;
	
	/** Crétion d'une variable globale de type booléen */
	private boolean usingDijkstra = false;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Description : 
	 *		Calcule l'objectif courant de l'agent, c'est à dire la case dans laquelle il veut aller. 
	 * 		Ce calcul dépend devrait dépendre au moins des valeurs de préférence déjà calculées, et 
	 * 		éventuellement d'autres calculs supplémentaires.
	 * 
	 * @return
	 * 		La case correspondant à la destination courante de l'agent.
	 */
	@Override
	protected AiTile processCurrentDestination()
	{		
		ai.checkInterruption();
		AiTile result = null;
		
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);

		
		if(tiles.contains(getCurrentDestination()))
		{
			result = getCurrentDestination();
		}
		else
		{		
			result = tiles.get(0);
		}
		
		
		return result;

	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Description :
	 * 		Calcule le chemin courant de l'agent, c'est à dire la séquence de cases à parcourir 
	 * 		pour atteindre (directement ou indirectement) la case objectif.
	 * 
	 * @return
	 * 		Le chemin courant suivant par l'agent.
	 */
	@Override
	protected AiPath processCurrentPath()
	{	
		ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);			
		AiTile endTile = getCurrentDestination();
		ownHero = ai.getZone().getOwnHero();
		AiTile ownTile = ownHero.getTile();		
		boolean isReachable = ai.securityHandler.isEnemyReachable();	

		Double remainingPath,ownSpeed,timeLeft;
		
		try {
			remainingPath = Double.valueOf(dijkstra.processEscapePath(startLocation).getLength())*AiTile.getSize();
		} catch (LimitReachedException e) {
			remainingPath = (double)100;
		}catch (NullPointerException e)	{
			remainingPath = (double)100;			
		}		
		ownSpeed = ownHero.getWalkingSpeed();
		timeLeft = Double.valueOf(ai.securityHandler.getTimeLeft(ownTile));
							
		try
		{
			if((!ai.securityHandler.isEnemyReachable() && ai.securityHandler.isDangerAround()) || (ai.securityHandler.willBurn(ownTile) && (ai.getZone().getBombs().size()>5 || ai.securityHandler.getTimeLeft(ownTile)<1350 || (ai.securityHandler.isDangerAround() && !ai.securityHandler.isEnemyReachable()) || (timeLeft-(remainingPath/ownSpeed)<1750))))
			{
				usingDijkstra = true;
				AiLocation heroLocation = new AiLocation(ownHero);		
				result = dijkstra.processEscapePath(heroLocation);
			}
			else
			{
				if(!isReachable && ai.attackHandler.canBomb())
				{
					usingDijkstra = false;
					result = astarRoaming.startProcess(startLocation,endTile);	
				}
				else
				{
					usingDijkstra = false;
					result = astar.startProcess(startLocation,endTile);	
				}		
			}
		}
		catch (LimitReachedException e)
		{
			result = new AiPath();
			usingDijkstra = false;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la direction courante suivie par l'agent.
	 * 
	 * @return
	 * 		La direction courante de l'agent.
	 */
	@Override
	protected Direction processCurrentDirection()
	{	
		ai.checkInterruption();
		Direction result = Direction.NONE;
		
		AiPath path = getCurrentPath();
		if(path==null || path.getLength()<2)		
		{
			result = Direction.NONE;
		}
		else
		{	
			AiLocation nextLocation = path.getLocation(1);		
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
				
			
			if(!nextTile.getFires().isEmpty())
			{
				result = Direction.NONE; 
			}
			else
			{				
				if(!usingDijkstra)
				{
					if((ai.securityHandler.willBurn(nextTile) && !ai.securityHandler.willBurn(currentTile)) || ai.securityHandler.absoluteDeath(nextTile))
					{
						result = Direction.NONE;
					}			
					else
					{
						result = zone.getDirection(currentTile, nextTile);
					}
				}	
				else if((ai.securityHandler.getTimeLeft(nextTile) < ai.securityHandler.getTimeLeft(currentTile) && ai.securityHandler.getTimeLeft(nextTile)<1350))    
				{
					result = Direction.NONE;				
				}	
				else
				{
					result = zone.getDirection(currentTile, nextTile);
				}
			}
		}
		
		return result;
	}

	
	/**
	 * Description :
	 * 		Cette méthode permet d'accéder à notre Dijkstra
	 * 
	 * @return
	 * 		Il renvoie notre Dijkstra
	 */
	public Dijkstra getDijkstra()
	{
		ai.checkInterruption();
		return this.dijkstra;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire.
	 * Ici, on représente la case de destination courante en la coloriant, ainsi que le chemin courant, 
	 * représenté par une ligne. 
	 */
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		super.updateOutput();
		
	}
}