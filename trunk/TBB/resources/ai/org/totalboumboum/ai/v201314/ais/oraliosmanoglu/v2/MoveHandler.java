package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2;

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
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * Cette class decide que ou notre agent va aller. quand on va arreter ou
 * continuer?
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 */
public class MoveHandler extends AiMoveHandler<Agent> {
	/**
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */

	protected MoveHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		TimeCostCalculator costCalculator = new TimeCostCalculator(ai, ownHero);
		TimeHeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(
				ai, ownHero);
		TimePartialSuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(
				ai, SearchMode.MODE_NOTREE);
		astar = new Astar(ai, ownHero, costCalculator, heuristicCalculator,
				successorCalculator);
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	/**
	 * Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le
	 * re-créer à chaque itération
	 */
	private Astar astar = null;

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected AiTile processCurrentDestination() {
		ai.checkInterruption();
		AiTile result = null;
		AiTile ourtile = ai.getZone().getOwnHero().getTile();
		int i, samePrefnbr = 0;

		AiPreferenceHandler<Agent> preferenceHandler = ai
				.getPreferenceHandler();

		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();

		int minPref = Collections.min(preferences.keySet());
		if (!preferences.isEmpty()) {
			List<AiTile> tiles = preferences.get(minPref);
			int tilesDistance = ai.getZone().getTileDistance(ourtile,
					tiles.get(0));

			for (i = 1; i < tiles.size(); i++) {
				ai.checkInterruption();
				if (tiles.get(0) == tiles.get(i))
					samePrefnbr++;

			}
			if (samePrefnbr == 0) {
				result = tiles.get(0);
			} else {
				for (i = 0; i < samePrefnbr; i++) {
					ai.checkInterruption();
					if (tilesDistance < ai.getZone().getTileDistance(ourtile,
							tiles.get(i + 1))) {
						tilesDistance = ai.getZone().getTileDistance(
								tiles.get(0), tiles.get(i + 1));
						result = tiles.get(i + 1);
					}
				}
			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected AiPath processCurrentPath() {
		ai.checkInterruption();

		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);
		AiTile endTile = getCurrentDestination();

		try { // on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch

			// result=dijk.processEscapePath(startLocation);
			result = astar.startProcess(startLocation, endTile);
		} catch (LimitReachedException e) {
			result = new AiPath();
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	protected Direction processCurrentDirection() {
		ai.checkInterruption();
		Direction result = Direction.NONE;

		AiPath path = getCurrentPath();
		if (path == null || path.getLength() < 2)
			// cas où le chemin est vide, ou bien ne contient que la case
			// courante
			result = Direction.NONE;
		else {
			AiLocation nextLocation = path.getLocation(1);

			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);
			// List<AiFire> dangerTiles=ai.getZone().getFires();

			if (!nextLocation.getTile().getFires().isEmpty())
				result = Direction.NONE;
			if (nextTile == null || nextTile.equals(currentTile))
				result = Direction.NONE;

			// ici, j'utilise une méthode de l'API pour calculer la direction
			// pour aller de la 1ère vers la 2nde case
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	public void updateOutput() {
		ai.checkInterruption();

		super.updateOutput();

	}
}
