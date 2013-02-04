package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.CinarYalcin;

/**
 * Return if selected tile's item is bonus and unless a bonus it returns false
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
@SuppressWarnings("deprecation")
public class ItemType extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "TypeItem";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemType(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
		for(AiItem item:tile.getItems())
		{
			ai.checkInterruption();
			if (ai.Malus.contains(item.getType())) 
					result = false;	
		}
		return result;
	}
}
