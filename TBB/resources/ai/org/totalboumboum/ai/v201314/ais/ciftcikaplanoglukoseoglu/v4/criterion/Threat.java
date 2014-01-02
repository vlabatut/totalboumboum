package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cette classe va nous permettre de traiter une case par rapport au critere
 * binaire "Menace". Si la case traitée est dans la portée
 * de la bombe ce critere renvoie la valeur 1. Si il reste une seconde avant
 * l'explosion de la bombe concernée la valeur est 2. Si la case est sure 
 * elle renvoie 0.
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class Threat extends AiCriterionInteger<Agent> {
	/** on nomme le critère */
	public static final String NAME = "THREAT";

	/**
	 * Crée le critère binaire Menace. ıl renvoie une valeur int. Si la case traitée est dans la portée
	 * de la bombe ce critere renvoie la valeur 1. Si il reste une seconde avant
	 * l'explosion de la bombe concernée la valeur est 2. Si la case est sure 
	 * elle renvoie 0;
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Threat(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param tile
	 *            la case concernée.  
	 *    @return result
	 *     renvoie la valeur entiere
	 *            qui est obtenu par le traitement. Si la bombe est sur le point
	 *            d'exploser, la valeur va devenir vrai. Sinon elle est fausse.
	 */
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 0;
		Map<Long, List<AiBomb>> chainebombs = tile.getZone().getBombsByDelays();

		ArrayList<AiTile> treat = new ArrayList<AiTile>();
		for (Long b : chainebombs.keySet()) {
			ai.checkInterruption();

			// on prend le temps des bombes en prenant compte des bombes
			// enchainées.
			List<AiBomb> tumbomba = chainebombs.get(b);

			for (AiBomb k : tumbomba) {
				ai.checkInterruption();

				List<AiTile> blasts = k.getBlast();
// si il reste une seconde avant l'explosion de la bombe, cette case est tres menacée.
				if (b < 1000) {
					if (blasts.contains(tile)) {
						result = 2;
						treat.add(tile);
					}
				}

				else
//si il y a une bombe qui menace cette case la valeur est égale a 1.
				if (blasts.contains(tile)) {

					if (!treat.contains(tile))
						result = 1;
				}

			}

		}

		return result;
	}
}
