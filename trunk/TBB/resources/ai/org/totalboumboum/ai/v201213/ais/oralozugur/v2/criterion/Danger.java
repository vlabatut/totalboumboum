package org.totalboumboum.ai.v201213.ais.oralozugur.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.ais.oralozugur.v2.OralOzugur;

/**
 * Cette classe est un simple exemple de critère binaire. Copiez-la,
 * renommez-la, modifiez-la pour l'adapter à vos besoin.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class Danger extends AiUtilityCriterionBoolean<OralOzugur> {
	/** Nom de ce critère */
	public static final String NAME = "Danger";

	/**
	 * On ne voudrais que l'agent rest dans la meme case beaucoup de temps
	 * 
	 * 
	 */
	// private static final int STUCK_LIMIT=500;

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Danger(OralOzugur ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (this.ai.dangerList.contains(tile))
			return true;
		if (!this.ai.suddenDeathTiles.isEmpty()) {
			if (this.ai.suddenDeathTiles.contains(tile)) {
				return true;
			}
		}

		return false;
	}
}
