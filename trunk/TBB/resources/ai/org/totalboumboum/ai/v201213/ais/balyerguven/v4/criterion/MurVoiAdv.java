package org.totalboumboum.ai.v201213.ais.balyerguven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven;

/**
 * our MurVoiAdv class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
@SuppressWarnings("deprecation")
public class MurVoiAdv extends AiUtilityCriterionBoolean<org.totalboumboum.ai.v201213.ais.balyerguven.v4.BalyerGuven>
{	/** mur voisin de l'adversaire */
	public static final String NAME = "MurVoiAdv";
	
	/**
	 * Crée un nouveau critère boolean.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public MurVoiAdv(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiHero myHero = ai.getHero();
		int count = 0;
		boolean result = false;
		
		
		for ( AiTile neighbor : tile.getNeighbors() )
		{
			ai.checkInterruption();
			if ( !neighbor.isCrossableBy(myHero) )
			{
				count++;
			}
				
		}
		if ( count > 1 )
		{
			result=true;
		}
		return result;
	}
}
