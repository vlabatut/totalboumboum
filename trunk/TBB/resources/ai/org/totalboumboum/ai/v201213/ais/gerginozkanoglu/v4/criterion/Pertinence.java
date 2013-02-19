package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.GerginOzkanoglu;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.TileCalculation;

/**
 * This criterion will measure the item's validity for our agent.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class Pertinence extends AiUtilityCriterionInteger<GerginOzkanoglu> {
	/** Criterion's name */
	public static final String NAME = "Pertinence";

	/**
	 * this will determine the maximum range of our bomb. if our range is less
	 * than this static variable's value, we will get this item.
	 */
	public static int RANGE_MAX = 7;

	/**
	 * @param ai
	 * 		description manquante !	
	 * @throws StopRequestException
	 * 		description manquante !	
	 */
	public Pertinence(GerginOzkanoglu ai) throws StopRequestException {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calculate = new TileCalculation(ai);
		int result = calculate.evaluateVisibleItem(tile);


		return result;
	}
}
