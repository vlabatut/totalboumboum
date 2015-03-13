package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.CinarYalcin;

/**
 * Return true is elected tiöle is secure
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
@SuppressWarnings("deprecation")
public class Security extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "Securite";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Security(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result=false;
		if ( ai. getCurrentDangerousTiles().contains( tile ) ) result=false;
		//if(!ai.isSafePassingBy(tile)) result=false;
		else result=true;
		if(result==true)
		{
			for(AiItem item:tile.getItems())
			{
				ai.checkInterruption();
				if (ai.Malus.contains(item.getType())) 
						result = false;	
			}
		}
		return result;	
	}
}
