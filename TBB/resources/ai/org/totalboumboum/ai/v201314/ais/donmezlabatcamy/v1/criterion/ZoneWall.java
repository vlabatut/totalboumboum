package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1.Agent;

/**
 * Cette classe permet de calculer le nombre de murs existant au voisinage de la
 * case de l'adversaire.
 *
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class ZoneWall extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ZONEWALL";

	/**
	 * Crée un nouveau critère entier.
     *
	 * @param ai
	 *            l'agent concerné.
	 */
	public ZoneWall(Agent ai) {
		super(ai, NAME, 0, 3);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
    /**
	 * Calcul la valeur du critère.
	 *
	 * @param tile
	 *            la case séléctionnée.
     * @return result
     *             le nombre de murs sur les cases voisine de l'adversaire.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 3;
		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			for (AiTile neighbor : ennemy.getTile().getNeighbors()) {
				ai.checkInterruption();
				if (neighbor.getBlocks().isEmpty() && result > 0) {
					result--;
				}
			}
		}
		return result;
	}
}
