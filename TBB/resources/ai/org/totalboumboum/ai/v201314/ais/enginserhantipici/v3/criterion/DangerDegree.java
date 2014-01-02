package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 *This criterion calculates danger degree of the tile.
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class DangerDegree extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DANGERDEGREE";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DangerDegree(Agent ai)
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 3;
		result = ai.DangerDegree(tile);
		
		return result;
	}
}
