package org.totalboumboum.ai.v201112.ais.sakaryasar.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v2.SakarYasar;

/**
 *
 */
@SuppressWarnings("deprecation")
public class CriterionPertinance extends AiUtilityCriterionBoolean{
	/** */
	public static final String NAME = "PERTINANCE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionPertinance(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		
		// init agent
		this.ai = ai;
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isItemPertinant(AiItemType t) throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		if(t==AiItemType.EXTRA_BOMB){
			int currentBombs = ai.getZone().getOwnHero().getBombNumberMax();
			if(currentBombs <3)
				result = true;
		}
		else{
			int currentRange = ai.getZone().getOwnHero().getBombRange();
			if(currentRange < 4)
				result = true;
		}
		
		return result;
	}
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected org.totalboumboum.ai.v201112.ais.sakaryasar.v2.SakarYasar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
	
		List<AiItem> items = tile.getItems();
		if(!items.isEmpty()){
			result = isItemPertinant(items.get(0).getType());
		}
		
		return result;
	}

}
