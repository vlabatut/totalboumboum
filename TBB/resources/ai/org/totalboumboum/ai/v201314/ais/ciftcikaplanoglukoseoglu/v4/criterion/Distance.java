package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.Agent;

/**
 * critère binaire distance : vraie
 * si la distance Manhattan est au plus 2 case(en pixel) 
 * entre notre agent et la case traitée .
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu 
 */
@SuppressWarnings("deprecation")
public class Distance extends AiCriterionBoolean<Agent> {
	/** Nom du critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée le critère binaire distance qui retourne la valeur vraie
	 * si la distance Manhattan est au plus 2 case(en pixel) entre notre agent et la case traitée .
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
