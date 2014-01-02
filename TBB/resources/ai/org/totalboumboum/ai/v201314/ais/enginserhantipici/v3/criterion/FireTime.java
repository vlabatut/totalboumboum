package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * This criterion calculates the remaining time of fire in the tile is more than 2" or not
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class FireTime extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "FIRE_TIME";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
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
		
		
/*		if(2000 < tile.getFires().get(0).getElapsedTime()){//2000 ? how many ms we need for the fire disappeared
			result = true;
		}
	*/
		return result;
	}
}
