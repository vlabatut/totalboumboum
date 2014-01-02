package org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v2.Agent;

/**
 * Ce critère nous aide quand on choisit la case de destination. Grace à ce
 * critère, on peut choisir une case de destination qui est plus proche de nous
 * au lieu de n'importe quelle case dans la zone.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Distance";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Distance(Agent ai) throws StopRequestException {
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

		if (this.ai.getZone().getTileDistance(
				this.ai.getZone().getOwnHero().getTile(), tile) <= this.ai
				.getZone().getOwnHero().getBombRange()) {
			result = true;
		}
		return result;
	}

}