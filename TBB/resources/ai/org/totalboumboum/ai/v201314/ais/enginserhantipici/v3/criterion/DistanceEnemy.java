package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;


/**
 * This criterion returns true if there is an enemy staying 
 * near a selected corner tile inside of the trap
 *
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE_ADVERSAIRE";
	
	/**
	 * This criterion returns true if there is an enemy staying 
	 * near a selected corner tile inside of the trap
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceEnemy(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	
	boolean result;
	
	if(ai.isB() && tile == ai.myTiles.get(0)){
		result = true;
	}else{
		result = false;
	}
		return result;
	}
}
