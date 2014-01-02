package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 * This class is responsible of the decision of the mode.
 *
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class ModeHandler extends AiModeHandler<Agent> {
//	/** */
//	private final int BOMB_NUMBER_COUNT = 2;
//	/** */
//	private final int BOMB_RANGE = 3;
//	/** */
//	private final int MULTIPLICATION_LIMIT = 8;
//	/** */
//	private final int ACCESSIBLE_TILE_LIMIT = 5;
//	/** */
//	private final int NONE = 0;
//	/** */
//	private final double RATIO = 0.25;


	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 *
	 * @param ai l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();

//		int bombNumber = ai.getZone().getOwnHero().getBombNumberMax();
//		int bombRange = ai.getZone().getOwnHero().getBombRange();
//		int multiplication = bombNumber * bombRange;

//		return (bombNumber >= BOMB_NUMBER_COUNT && bombRange >= BOMB_RANGE && multiplication >= MULTIPLICATION_LIMIT);
		return true;
	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();

//		Set<AiTile> accessibleTiles = ai.getTileUtil().getAccessibleTiles();
//		if (accessibleTiles.size() <= ACCESSIBLE_TILE_LIMIT) return false;
//
//		int hiddenItemsCount = ai.getZone().getHiddenItemsCount();
//		int visibleItemCount = ai.getZone().getItems().size() - hiddenItemsCount;
//		if (visibleItemCount == NONE) {
//			int destructibleCount = ai.getZone().getDestructibleBlocks().size();
//			return (double) hiddenItemsCount / (double) destructibleCount > RATIO;
//		} else {
//			return true;
//		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();
	}
}
