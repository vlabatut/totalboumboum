package org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;

import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.BalCetin;

/**
 *Criterion binary to decide if a tile is relevant.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
public class CollectRelevance extends AiUtilityCriterionBoolean {

	public static final String NAME = "CollectRelevance";

	
	public CollectRelevance(BalCetin ai) throws StopRequestException { // init
																		// nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected BalCetin ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		AiZone zone = this.ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		//Tile process object to use the methods of Tileprocess class
		
		
		boolean result = true;

			//if our hero has more than 5 bombs or 5 extra flames, relevance of the tiles which have visible items is false. 
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			if (ownHero.getBombNumberMax() > 5)
				result = false;

		} else if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			if (ownHero.getBombRange() > 5)
				result = false;

		}
		
		return result;
	}
}
