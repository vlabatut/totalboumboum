package org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;

import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.BalCetin;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.TileProcess;

/**
 *Criterion binary to decide if a tile is relevant.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
public class CollectRelevance extends AiUtilityCriterionBoolean {
	/** Nom de ce critère */
	public static final String NAME = "CollectRelevance";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
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

	AiZone zone = this.ai.getZone();
	AiHero ownHero = this.zone.getOwnHero();
	//Tile process object to use the methods of Tileprocess class
	TileProcess tp = new TileProcess(this.ai);

	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;

			//if our hero has more than 5 bombs or 5 extra flames, relevance of the tiles which have visible items is false. 
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			if (ownHero.getBombNumberMax() > 5)
				result = false;

		} else if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			if (ownHero.getBombRange() > 5)
				result = false;

		}
		/*
		 * if(ownHero != null) { if(ownHero.getBombNumberCurrent() ==
		 * ownHero.getBombNumberMax()) { for(i=0;i<items.size();i++) {
		 * ai.checkInterruption(); if(items.get(i).getType() ==
		 * AiItemType.EXTRA_BOMB) result = false;
		 * 
		 * }
		 * 
		 * } else if(ownHero.getBombRange() >= 6) { for(i=0;i<items.size();i++)
		 * { ai.checkInterruption(); if(items.get(i).getType() ==
		 * AiItemType.EXTRA_FLAME) result = false;
		 * 
		 * }
		 * 
		 * } }
		 */
		return result;
	}
}
