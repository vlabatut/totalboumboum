package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our menace class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Menace extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** danger */
	public static final String NAME = "Menace";
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Menace(BalyerGuven ai) throws StopRequestException
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
		boolean result = true;
		
		if(ai.getCurrentDangerousTiles().contains(tile))
		{
			result=false;
		}
		return result;
	}
}
