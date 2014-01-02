package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.Agent;

/**
 * cette classe examine la distance entre nous-meme et une ennemi avec la
 * distance Manhattan. on pense comme on est dans le centre d'un circle qui a un
 * rayon 5. les cases qui se trouvent dans cet circle ont la valeur 0, les
 * autres ont 1.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom de ce critere */
	public static final String NAME = "DISTANCE";

	/**
	 * Cree un nouveau critere binaire.
	 * 
	 * @param ai
	 *            l'agent concerne.
	 */
	public Distance(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Distance radius limit to check to determine the tile's state.
	 */
	private double DISTANCE_UPPER_LIMIT = 5.0;

	/**
	 * how much estimately a speed bonus augments our speed
	 */
	private final static double ONE_BONUS_GIVEN_SPEED = 35.0;

	/**
	 * Default Walking speed of a hero at the beginning of a map
	 */
	private final static double DEFAULT_WALKING_SPEED = 190.625;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		double walkingSpeed = ai.getZone().getOwnHero().getWalkingSpeed();

		if (walkingSpeed > DEFAULT_WALKING_SPEED) {
			DISTANCE_UPPER_LIMIT = walkingSpeed / ONE_BONUS_GIVEN_SPEED;
		}
		
		if (ai.tileHandler.simpleTileDistance(ai.getZone().getOwnHero()
				.getTile(), tile) <= DISTANCE_UPPER_LIMIT)
			return false;

		return true;
	}
}
