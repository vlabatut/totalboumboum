package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v2.Agent;

/**
 * cette critere nous envoyer la distance(integer(0,1 ou 2)) entre le case et
 * l'adversaire.
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 * 
 */
public class Distance extends AiCriterionInteger<Agent> {

	/**
	 * description de nom de critere.
	 */
	public static final String NAME = "Distance";

	/**
	 * @param ai
	 *            l'agent concerné.
	 */
	public Distance(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		// initialisation de result
		int result = 0;
		// création de liste des tiles dans cette zone
		List<AiTile> tiles = ai.getZone().getTiles();
		// loop pour toutes les tiles
		for (AiTile tiless : tiles) {
			ai.checkInterruption();
			// on voit que est-ce que notre agent accéde a cette tile.
			if (tiless.isCrossableBy(ai.getZone().getOwnHero())) {
				// loop pour la liste des personnages contenus dans cette zone
				for (AiHero adversaire : ai.getZone().getRemainingOpponents()) {
					ai.checkInterruption();
					// on prend la distance entre tile de cette adversaire et
					// tile de premiere loop
					int distance = ai.getZone().getTileDistance(
							adversaire.getTile(), tile);
					if (distance >= 0 && distance < 3)
						result = 0;
					else if (distance >= 3 && distance < 6)
						result = 1;
					else
						result = 2;
				}
			}
		}

		return result;
	}
}