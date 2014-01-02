package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.BlockingHandler;

/**
 * Il vaut true si on a besoin de ce bonus. Donc, pour bonus vitesse, il va
 * retourner true si la vitesse n’est pas maximum. Pour extra feu, si notre
 * portée est plus petit que 3 qu'on en a besoin, il va retourner true. Et pour
 * extra bombe, s’il existe plus que 55 tile dans la zone alors, on besoin de 4
 * bombe. Sinon 3 bombe est suffisante.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Necessity extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Necessity";
	/** La vitesse maximale de l'agent */
	private double MAXIMUM_HERO_SPEED = 200.0;
	/** S'il existe plus de 55 cases dans la zone, on a besoin au moins 4 bombes */
	private int ZONE_TILE_LIMIT = 55;
	/** Le minimum limite de la portée qu'on doit avoir */
	private final int BOMBE_RANGE_LIMIT = 3;
	/** pour acceder aux methodes de BlockingHandler */
	BlockingHandler blockingHandler;

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Necessity(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
		blockingHandler = ai.blockingHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		AiZone zone = ai.getZone();
		AiHero hero = zone.getOwnHero();
		blockingHandler = ai.blockingHandler;

		for (AiItem item : tile.getItems()) {
			ai.checkInterruption();
			if (item.getType() == AiItemType.EXTRA_FLAME
					|| tile.getItems().contains(AiItemType.GOLDEN_FLAME)) {
				int fireRange = hero.getBombRange();
				if (fireRange < BOMBE_RANGE_LIMIT) {
					result = true;
					if (blockingHandler.getBlockSize(hero) < 4) {
						result = false;
					}
				} else
					result = false;
			}

			if (item.getType() == AiItemType.EXTRA_BOMB
					|| tile.getItems().contains(AiItemType.GOLDEN_BOMB)) {
				int bombNumber = hero.getBombNumberMax();
				int wantedBombs = ((zone.getHeight() * zone.getWidth()) < ZONE_TILE_LIMIT) ? 3
						: 4;

				if (bombNumber < wantedBombs) {
					result = true;
				} else
					result = false;
			}

			if (item.getType() == AiItemType.EXTRA_SPEED
					|| tile.getItems().contains(AiItemType.GOLDEN_SPEED)) {
				double heroSpeed = hero.getWalkingSpeed();
				if (MAXIMUM_HERO_SPEED > heroSpeed) {
					result = true;
				} else
					result = false;
			}
		}

		return result;
	}
}
