package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;


/**
 * our adversaire class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Adversaire extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** enemy */
	public static final String NAME = "Adversaire";
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 *		
	 */
	public Adversaire(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiTile myTile = ai.getHero().getTile();
		int myDistance = ai.getZone().getTileDistance(myTile, tile);
		boolean result = true;
		
		for(AiHero enemy : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			if(this.ai.getZone().getTileDistance( enemy.getTile(), tile ) < myDistance)
			{
				result=false;
			}
		}
		return result;
	}
}
