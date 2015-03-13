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
public class Security extends AiUtilityCriterionBoolean<GerginOzkanoglu> {
	/** criterion's name */
	public static final String NAME = "Security";

	/**
	 * 
	 * @param ai
	 * 		description manquante !	
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public Security(GerginOzkanoglu ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		TileCalculation calcul = new TileCalculation(this.ai);
		if (calcul.isDangerous(tile) || calcul.isThereSuddenDeathEventInTile(tile))
			result = false;

		return result;
	}
}
