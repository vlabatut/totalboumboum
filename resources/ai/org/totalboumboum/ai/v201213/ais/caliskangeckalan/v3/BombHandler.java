package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<CaliskanGeckalan>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/** initialize the map to null*/
	protected AiZone zone = null;
	/** initialize the hero to null */
	protected AiHero ownHero = null;
	/** initialize the coordinates of our hero to null */
	protected AiTile currentTile = null;
	/** initialize an Astar for direct road */
	protected Astar astarPrecise = null;
	/** initialize an Astar for indirect road */
	protected Astar astarApproximation = null;
	/**initialize an Dijkstra */
	protected Dijkstra dijkstra = null;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(CaliskanGeckalan ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = false;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
		CalculCommun calculCommun = new CalculCommun(ai);
		AiTile currentTile = ownHero.getTile();
		AiBomb bomb = ownHero.getBombPrototype();
		boolean bombAbsence = currentTile.isCrossableBy(bomb); //checking the absence of our bomb
		if (bombAbsence && ownHero.getBombNumberMax() - ownHero.getBombNumberCurrent() >0) {
			//get the target coordinates that initialize in UtilityHandler
			AiTile currentDestination = ai.moveHandler.getCurrentDestination();
			//get the first bomb target coordinates that initialize in UtilityHandler
			boolean bombPrimaryDestination = currentTile.equals(currentDestination)	&& ai.moveHandler.bombDestination;
			//get the second bomb target coordinates that initialize in UtilityHandler
			boolean bombSecondaryDestination = currentTile.equals(currentDestination) && ai.moveHandler.secondaryBombing;
			//control of map, if we put a bomb, can we escape and kill an enemy
			if (((bombPrimaryDestination && bombSecondaryDestination) || canKill() || ai.utilityHandler.getUtilitiesByTile().get(currentTile) != null) && calculCommun.canBomb()) {
				result = true;
				 //initialize of the variables for dropping bomb
				 ai.utilityHandler.rivalBombing = false;
				 ai.utilityHandler.murBombing = false;
				 ai.moveHandler.bombDestination  = false;
				 ai.moveHandler.secondaryBombing = false;
				 ai.moveHandler.safeDestination  = null;
			}
		}

		return result;
	}
	
	/**
	 * @return if we put a bomb, can we kill an enemy? controls is there any enemy
	 * in our range, and return true or false 
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	public boolean canKill() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		AiBomb bomb = ownHero.getBombPrototype();
		List<AiTile> tiles = bomb.getBlast();
		Iterator<AiTile> tilesIt = tiles.iterator();
		while(tilesIt.hasNext()) {
			ai.checkInterruption();
			AiTile tile = tilesIt.next();
			List<AiHero> heroes = tile.getHeroes();
			Iterator<AiHero> heroesIt = heroes.iterator();
			while(heroesIt.hasNext()) {
				ai.checkInterruption();
			    AiHero hero = heroesIt.next();
			    if(!hero.equals(ownHero)) {
			    	int distance = zone.getTileDistance(currentTile, hero.getTile());
			    	int ownRange = ownHero.getBombRange();
			    	if(ownRange > 2 && distance -1 >= ownRange)
			    		result = true;
			    	else if(ownRange <= 2)
			    		result = true;
			    }
			}
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();

	}
}
