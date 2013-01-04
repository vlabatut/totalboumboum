package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our duree class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Duree extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** time */
	public static final String NAME = "Duree";
	/** limit of distance */
	private final int	DISTANCE_LIMIT	= 5;
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree(BalyerGuven ai) throws StopRequestException
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
		
		if(this.ai.getZone().getTileDistance( this.ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_LIMIT)
		{
			result = true;
		}
		return result;
	}
}