/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Agent;



/**
 * Ce critere retourne nombre de murs qui vont etre detruit si on met une bombe 
 * dans cette une case.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class NumberMur extends AiCriterionInteger<Agent>
{	
	/** Nom de ce critère */
	public static final String NAME = "NumberMur";
	/** Domaine de définition */
	public static final int DISTANCE_LIMIT = 3;
	
	/**
	 * Crée un nouveau critère.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 *            
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NumberMur(Agent ai) throws StopRequestException
	{	
		super( ai, NAME , 0, 4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		return ai.getNbMurDetruitofTile(tile);
		
	}
}
