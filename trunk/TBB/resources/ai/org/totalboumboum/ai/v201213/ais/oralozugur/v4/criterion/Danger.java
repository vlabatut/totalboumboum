package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
 * Si un case est un danger ou pas.
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
		if (!tile.getHeroes().isEmpty()) {
			for (AiHero hero : tile.getHeroes()) {
				this.ai.checkInterruption();
				if (hero.isContagious())
					return true;
			}
		}
		if (!tile.getItems().isEmpty()) {
			for(AiItem item: tile.getItems())
			{
				this.ai.checkInterruption();
				if(!this.ai.isBonus(item))
				return true;
			}
		}
		this.ai.safe_tile_count++;
		return false;
	}
}
