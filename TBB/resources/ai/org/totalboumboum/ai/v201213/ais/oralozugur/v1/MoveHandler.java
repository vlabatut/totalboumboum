package org.totalboumboum.ai.v201213.ais.oralozugur.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class MoveHandler extends AiMoveHandler<OralOzugur> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/**
	 * Si la difference de valeur d'utilite entre objective et la case qui a
	 * l'utilite maximal est plus grand que ce valeur alors on change
	 * l'objective.
	 */
	/**
	 * valeur represantante la prochaine case
	 * 
	 */
	private static int NEXT_TILE = 1;
	/**
	 * Difference neccessaire pour changer l'objective dans mode collecte
	 */
	private static final int COLLECT_OBJECTIVE_DIFFERENCE = 4;
	/**
	 * Difference neccessaire pour changer l'objective dans mode attaque.
	 */
	private static final int ATTACK_OBJECTIVE_DIFFERENCE = 1;

	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	protected MoveHandler(OralOzugur ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		ai.checkInterruption();

		if (this.ai.objective == null) {
			this.ai.objective = this.ai.getBiggestTile();
		} 
		else {
			float biggestTileUtility = this.ai.getUtilityHandler()
					.getUtilitiesByTile().get(this.ai.getBiggestTile());
			float currentObjectiveUtility = 0;
			if(this.ai.getUtilityHandler()
					.getUtilitiesByTile().containsKey(this.ai.objective))
			{
				currentObjectiveUtility = this.ai.getUtilityHandler()
					.getUtilitiesByTile().get(this.ai.objective);
			}
			if ((biggestTileUtility - currentObjectiveUtility > COLLECT_OBJECTIVE_DIFFERENCE)
					&& (ai.getModeHandler().getMode().equals(AiMode.COLLECTING))) {
				this.ai.objective = this.ai.getBiggestTile();
			} else if ((biggestTileUtility - currentObjectiveUtility > ATTACK_OBJECTIVE_DIFFERENCE)
					&& (ai.getModeHandler().getMode().equals(AiMode.ATTACKING))) {
				this.ai.objective = this.ai.getBiggestTile();
			}
		}
		return this.ai.objective;
	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		ai.checkInterruption();
		AiHero hero = this.ai.getZone().getOwnHero();
		AiPath path = null;
		AiLocation location = new AiLocation(hero);
		CostCalculator ccalculator = new TimeCostCalculator(this.ai, hero);
		HeuristicCalculator hcalculator = new TimeHeuristicCalculator(this.ai,
				hero);

		SuccessorCalculator scalculator = new TimePartialSuccessorCalculator(
				this.ai, TimePartialSuccessorCalculator.MODE_NOTREE);

		Astar astar = new Astar(this.ai, hero, ccalculator, hcalculator,
				scalculator);

		if (this.ai.objective != null) {
			try {
				path = astar.startProcess(location, this.ai.objective);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
		}

		return path;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException {
		ai.checkInterruption();
		AiPath path = updateCurrentPath();
		if (path != null) {

			long wait = path.getFirstPause();
			if (wait > 0) {// DOUBLE CHECK LATER
				return Direction.NONE;
			}

			if (path.getLength() > 1) {
				if (!path.getLocation(NEXT_TILE).getTile().getFires().isEmpty())
					return Direction.NONE;

				return ai.getZone().getDirection(
						ai.getZone().getOwnHero().getTile(),
						path.getLocation(NEXT_TILE).getTile());
			}
		}

		return Direction.NONE;

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
