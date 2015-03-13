package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;

/**
 * Ce critère vaut faux si la distance entre l’agent et la case est plus grand
 * qu’un rayon qui dépend du vitesse. Le rayon initial sera 5 et nous allons
 * ajouter +1 pour chaque bonus vitesse collecté. Si le bonus est dans le rayon,
 * alors il vaut vrai.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Distance";
	/** limite du rayon à vérifier pour déterminer l'état de la case */
	private double DISTANCE_UPPER_LIMIT = 5.0;
	/** approximativement un bonus de vitesse combien augmente notre vitesse */
	private final static double ONE_BONUS_GIVEN_SPEED = 35.0;
	/** au debut de la zone par defaut la vitesse de l'agent */
	private final static double DEFAULT_WALKING_SPEED = 206.25;

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Distance(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		double walkingSpeed = ai.getZone().getOwnHero().getWalkingSpeed();
		if (walkingSpeed > DEFAULT_WALKING_SPEED) {
			DISTANCE_UPPER_LIMIT = walkingSpeed / ONE_BONUS_GIVEN_SPEED;
		}

		if (ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),
				tile) <= DISTANCE_UPPER_LIMIT)
			result = true;

		return result;
	}

}