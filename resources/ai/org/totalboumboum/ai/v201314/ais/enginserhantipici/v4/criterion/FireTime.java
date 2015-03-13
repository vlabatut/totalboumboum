package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 * This criterion is needed for speed attack strategy
 * it returns always false but actually it doesn't really meter if its true or false
 * we are choosing just one tile in speed attack strategy so we don't need any preference calculation
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class FireTime extends AiCriterionBoolean<Agent>
{	/** Name of the criterion */
	public static final String NAME = "FIRE_TIME";
	
	/**
	 * Creation of a new boolean criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public FireTime(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
		boolean result = false;
		
		return result;
	}
}
