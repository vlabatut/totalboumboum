package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiItemType;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.CinarYalcin;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */

public class TypeItem extends AiUtilityCriterionBoolean<CinarYalcin>
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
	public TypeItem(CinarYalcin ai) throws StopRequestException
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
		
		if (tile.getItems().contains(AiItemType.ANTI_BOMB) || tile.getItems().contains(AiItemType.NO_BOMB)
				|| tile.getItems().contains(AiItemType.NO_FLAME)|| tile.getItems().contains(AiItemType.ANTI_FLAME)
				|| tile.getItems().contains(AiItemType.NO_SPEED)|| tile.getItems().contains(AiItemType.ANTI_SPEED)
				|| tile.getItems().contains(AiItemType.RANDOM_NONE)) {
				result = false;
		}
		
		// TODO à compléter par le traitement approprié
	
		return result;
	}
}
