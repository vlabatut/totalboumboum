package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.TileHandler;

/**
 *This criterion calculates danger degree of the tile.
 *
 * if 1 --> too much danger
 * if 2 --> less danger
 * if 3 --> no danger
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
@SuppressWarnings("deprecation")
public class DangerDegree extends AiCriterionInteger<Agent>
{	/** Name of the criterion */
	public static final String NAME = "DANGER_DEGREE";
	
	/**
	 * Creation of a new integer criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public DangerDegree(Agent ai)
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}
	/**
	 * TileHandler
	 */
	TileHandler tlh;
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 3;
		tlh = new TileHandler(ai);
		result = tlh.DangerDegree(tile);
		
		return result;
	}
}
