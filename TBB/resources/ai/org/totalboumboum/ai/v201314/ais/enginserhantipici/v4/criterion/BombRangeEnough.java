package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;


/**
 * This criterion calculates the tile in the current tunnel is reachable for our bombs fire or not.
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class BombRangeEnough extends AiCriterionBoolean<Agent>
{	/** name of the criterion */
	public static final String NAME = "BOMB_RANGE_ENOUGH";
	
	/**
	 * Creation of a new boolean criterion
	 * 
	 * @param ai
	 * 		related agent 
	 */
	public BombRangeEnough(Agent ai)
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
	
		if(ai.getTilesInTunnel().indexOf(tile) 
				<= ai.getTilesInTunnel().indexOf(ai.getZone().getOwnHero().getTile()) + ai.getZone().getOwnHero().getBombRange())
			result = true;
	
		return result;
	}
}
