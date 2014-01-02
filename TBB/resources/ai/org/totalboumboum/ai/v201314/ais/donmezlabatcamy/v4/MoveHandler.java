package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class MoveHandler extends AiMoveHandler<Agent> {

	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;

	/** */
	private AiHero ownHero = null;

	/** */
	public Astar astar = null;

	/** */
	public boolean pathControl = false;
	/** */
	public boolean dangerControl;
	/** */
	public AiTile nextTile;

	// /** */
	// public AiPath result2 = null;;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		AiLocation location = new AiLocation(ownHero);
		{
			CostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai, ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai, SearchMode.MODE_NOTREE);
			costCalculator.setMalusCost(1000);
			// costCalculator.setOpponentCost(1000);
			heuristicCalculator.processHeuristic(location);
			astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}

	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Objectif courant de l'agent.
	 * 
	 * @return endTile la case de destination de l'agent
	 */
	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();

		AiTile result = null;

		AiTile myTile = ownHero.getTile();

		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();

		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();

		int minPref = Collections.min(preferences.keySet());

		List<AiTile> tiles = preferences.get(minPref);

		result = tiles.get(0);

		boolean control = true;

		int distance = zone.getTileDistance(myTile, tiles.get(0));

		for (int i = 0; i < tiles.size(); i++) {
			ai.checkInterruption();

			int tmpDist = zone.getTileDistance(myTile, tiles.get(i));

			if ( tmpDist <= distance ) {
				distance = tmpDist;
				result = tiles.get(i);
			}
		}
		if ( ai.endTile == null ) {
			ai.endTile = result;
			ai.endTilePref = minPref;
		} else {
			if ( !tiles.contains(ai.endTile) ) {
				ai.endTile = result;
				ai.endTilePref = minPref;
			}
		}
		if ( ai.getDangerousTiles.contains(myTile) )
			dangerControl = true;
		else
			dangerControl = false;

		if ( pathControl && ai.getDangerousTiles.contains(myTile) ) {
			if ( ai.getTH().getNearestSafeTiles2() != null ) {
				ai.endTile = ai.getTH().getNearestSafeTiles2();
				if ( !ai.getDangerousTiles.contains(myTile) )
					pathControl = false;
			} else {
				ai.endTile = ai.getTH().getNearestSafeTiles();
				if ( !ai.getDangerousTiles.contains(myTile) )
					pathControl = false;
			}

		}
		if ( dangerControl && !ai.ennemyAccesibility && ai.getCG().amIINmyFirstBombBlast() && control ) {
			if ( ai.getDangerousTiles.contains(myTile) && ai.getTH().getNearestSafeTiles2() != null ) {
				ai.endTile = ai.getTH().getNearestSafeTiles2();
				if ( !ai.getDangerousTiles.contains(myTile) ) {
					control = false;
					dangerControl = false;
				}

			}

			else {
				if ( ai.getDangerousTiles.contains(myTile) && ai.getTH().getNearestSafeTiles() != null ) {
					ai.endTile = ai.getTH().getNearestSafeTiles();
					if ( !ai.getDangerousTiles.contains(myTile) ) {
						control = false;
						dangerControl = false;
					}
				}

			}
		}
		return ai.endTile;

	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Calcule le chemin courant de l'agent, c'est à dire la séquence de case à parcourir pour atteindre la case objectif.
	 * 
	 * @return result Le chemin calculé
	 */
	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();
		
		AiPath result = null;

		AiLocation startLocation = new AiLocation(ownHero);

		try {
			if ( ai.endTile != null )
				result = astar.startProcess(startLocation, ai.endTile);
		} catch (LimitReachedException e) {
			pathControl = true;
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Calcule la direction courantes suivie par l'agent.
	 * 
	 * @return la direction de l'agent
	 */
	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();

		AiPath path = getCurrentPath();
		
		AiTile currentTile = ownHero.getTile();

		if ( path != null ) {
			
			long wait = path.getFirstPause();
		
			if ( wait > 0 )
				return Direction.NONE;
			if ( (path.getLength() > 1) && wait <= 0 ) {
				nextTile = path.getLocation(1).getTile();
				if ( !ai.ennemyAccesibility && ai.getTH().allNeighborsInDangerExceptHeroTile(ownHero) )
					return Direction.NONE;
				else if ( !ai.ennemyAccesibility && ai.getDangerousTiles.contains(nextTile)
						&& !ai.getDangerousTiles.contains(ownHero.getTile()) )
					return Direction.NONE;
				else
					return zone.getDirection(currentTile, path.getLocation(1).getTile());
			}
		}
		return Direction.NONE;

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
