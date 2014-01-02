package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3.Agent;

/**
 * Cette classe calcule le temps d'explosion d'une bombe en regroupant des courtes durées et
 * créer un compte à rebours.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class BombExplosionTime extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "BMBEXTIME";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public BombExplosionTime(Agent ai) {
		super(ai, NAME, 0, 5);
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
	 * @return result la valeur du critère
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 0;
		long bombExplosionTime = ai.getZone().getOwnHero().getBombDuration();
		if (ai.getCG().chainReactionTime(tile) >= 0
				&& ai.getCG().chainReactionTime(tile) < bombExplosionTime / 32)
			result = 0;
		if (ai.getCG().chainReactionTime(tile) >= bombExplosionTime / 32
				&& ai.getCG().chainReactionTime(tile) < bombExplosionTime / 16)
			result = 1;
		if (ai.getCG().chainReactionTime(tile) >= bombExplosionTime / 16
				&& ai.getCG().chainReactionTime(tile) < bombExplosionTime / 8)
			result = 2;
		if (ai.getCG().chainReactionTime(tile) >= bombExplosionTime / 8
				&& ai.getCG().chainReactionTime(tile) < bombExplosionTime / 4)
			result = 3;
		if (ai.getCG().chainReactionTime(tile) >= bombExplosionTime / 4
				&& ai.getCG().chainReactionTime(tile) < bombExplosionTime / 2)
			result = 4;
		if (ai.getCG().chainReactionTime(tile) >= bombExplosionTime / 2)
			result = 5;
		return result;
	}
}
