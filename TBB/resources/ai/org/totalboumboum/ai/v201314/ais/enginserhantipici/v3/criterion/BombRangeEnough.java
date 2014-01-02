package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * This criterion calculates the tile in the current tunnel is reachable for our bombs fire or not.
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class BombRangeEnough extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "BOMB_RANGE_ENOUGH";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
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
