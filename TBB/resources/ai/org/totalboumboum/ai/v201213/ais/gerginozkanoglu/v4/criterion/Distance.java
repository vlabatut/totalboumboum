package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.TileCalculation;

/**
 * This criterion is used by all cases of all modes. it will evaluate the tile
 * for its security status.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class Distance extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** criterion's name */
	public static final String NAME = "Distance";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 * 
	 */
	public Distance(GerginOzkanoglu ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile) throws StopRequestException {

		ai.checkInterruption();
		boolean result = false;
		TileCalculation t = new TileCalculation(this.ai);
		try {
			if (t.getClosestAccDesWalltoEnemy().getNeighbors().contains(tile)) {
				result = true;
			}
		} catch (NullPointerException e) {
			result = false;
		}
		return result;

	}
}
