package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our danger of malus class
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class MalusMenace extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** danger of malus */
	public static final String NAME = "MalusMenace";

	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MalusMenace(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;

		for (AiItem item : tile.getItems()) {
			ai.checkInterruption();

			if (item.getType().equals(AiItemType.NO_BOMB)
					|| item.getType().equals(AiItemType.NO_FLAME)
					|| item.getType().equals(AiItemType.NO_SPEED)
					|| item.getType().equals(AiItemType.ANTI_BOMB)
					|| item.getType().equals(AiItemType.ANTI_FLAME)
					|| item.getType().equals(AiItemType.ANTI_SPEED)
					|| item.getType().equals(AiItemType.RANDOM_NONE)) {
				result=true;
			}
		}
		return result;
	}
}
