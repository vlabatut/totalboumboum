package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v3.TileCalculation;

/**
 * This criterion is used for case Unreachable_Enemy.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */

public class ClosestWallToEnemy extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** criterion's name */
	public static final String NAME = "ClosestWallToEnemy";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 * 
	 */
	public ClosestWallToEnemy(GerginOzkanoglu ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calcul = new TileCalculation(this.ai);
		return calcul.closestWallToEnemy(tile, calcul.closestEnemyToUs(this.ai.getZone().getOwnHero().getTile()));
			}
}
