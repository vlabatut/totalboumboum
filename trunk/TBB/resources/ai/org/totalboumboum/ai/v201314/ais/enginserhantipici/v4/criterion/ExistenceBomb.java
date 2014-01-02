package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 *this class is a binary criterion
 *It returns true if there is a bomb at current tile, if not returns false
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class ExistenceBomb extends AiCriterionBoolean<Agent>
{	/** Name of the criterion */
	public static final String NAME = "EXISTENCE_BOMB";
	
	/**
	 * Creation of a new boolean criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public ExistenceBomb(Agent ai)
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
		if(!tile.getBombs().isEmpty()){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
}
