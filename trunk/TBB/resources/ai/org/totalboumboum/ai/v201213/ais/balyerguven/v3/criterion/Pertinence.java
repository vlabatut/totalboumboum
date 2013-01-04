package org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.BalyerGuven;

/**
 * our Pertinence class
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Pertinence extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** pertinence */
	public static final String NAME = "Pertinence";
	/**represent number of bombes */
	public static final int NBR_BOMBE = 2;
	/** represent number of range*/
	public static final int NBR_PORTE = 2;

	
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Pertinence(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
	

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiHero myHero = this.ai.getZone().getOwnHero();
		boolean result = false;

		if (myHero.getBombNumberMax() < NBR_BOMBE) {
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			result = true;
		}
		}
		
		if (myHero.getBombRange() < NBR_PORTE) {
		if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			result = true;
		}
		}
		return result;
	}
}
