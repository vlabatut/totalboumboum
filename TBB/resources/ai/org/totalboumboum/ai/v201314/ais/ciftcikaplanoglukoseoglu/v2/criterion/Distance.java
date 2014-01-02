/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.Agent;

/**
 * 
 * Cette classe va nous permettre de traiter une case par rapport au critere
 * binaire "distance".
 * 
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom du critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée le critère binaire distance a l'ennemi.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Distance(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param tile
	 *            la case concernée.
	 * @return result renvoie la valeur booléene qui est obtenu par le
	 *         traitement. Si l'adversaire est au plus a trois case de distance
	 *         de la case concernée, la valeur est vrai. Sinon elle est fausse.
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		boolean result = false;
		AiHero ownhero = zone.getOwnHero();

		double longeur = zone.getPixelDistance(ownhero.getPosX(),
				ownhero.getPosY(), tile);
		if (longeur < 2 * AiTile.getSize())
			result = true;

		return result;
	}
}
