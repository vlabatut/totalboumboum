package org.totalboumboum.ai.v201112.ais.unluyildirim.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v2.UnluYildirim;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe donne les resultat des numéros des murs voisins autour du tile.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionVoisin extends AiUtilityCriterionInteger {
	public static final String NAME = "VOISIN";

	public CriterionVoisin(UnluYildirim ai) throws StopRequestException {
		super(NAME, 0, 3);
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

	// ///////////////////////////////////////////////////////////////

	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();

		Integer result = 0;

		int number = 0; // represente le numero
		AiZone zone = ai.getZone();
		AiHero myhero = zone.getOwnHero();

		int range = myhero.getBombRange();
		range = myhero.getBombRange();
		AiFire fire = myhero.getBombPrototype().getFirePrototype();

		for (Direction d : Direction.getPrimaryValues()) {
			ai.checkInterruption();

			AiTile voisin = tile;
			int i = 1;
			boolean result1 = false;
			while (i <= range && !result1) {
				ai.checkInterruption();

				voisin = voisin.getNeighbor(d);
				if (!voisin.isCrossableBy(fire)
						&& !voisin.getBlocks().isEmpty()
						&& voisin.getBlocks().get(0).isDestructible()) {
					number++;
				} else
					result1 = true;
				i++;
			}
			// Il donne le numero des murs destructible autour du tile sélon la
			// portée de la bombe.
		}
		if (number > 3)
			number = 3;
		else if (number == 0)
			number = 1;
		result = number;
		return result;
	}
}
