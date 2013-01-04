package org.totalboumboum.ai.v201213.ais.balyerguven.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v3.BalyerGuven;


/**
 * our MurVoiAdv class.
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class MurVoiAdv extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** mur voisin de l'adversaire */
	public static final String NAME = "MurVoiAdv";
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MurVoiAdv(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
	
		
		return result;
	}
}
