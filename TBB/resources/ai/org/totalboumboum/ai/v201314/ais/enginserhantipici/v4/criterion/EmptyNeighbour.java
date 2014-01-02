package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 * It returns true if there is more then 3 empty tiles around the current tile
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class EmptyNeighbour extends AiCriterionBoolean<Agent>
{	/** Name of the criterion */
	public static final String NAME = "EMPTY_NEIGHBOUR";
	
	/**
	 * Creation of a new integer criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public EmptyNeighbour(Agent ai)
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

		int v = 0;
		for(AiTile neighbor : tile.getNeighbors()){
			ai.checkInterruption();
			if(neighbor.getBlocks().isEmpty()){
				v++;
			}
		}
		if(v >= 3){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
}
