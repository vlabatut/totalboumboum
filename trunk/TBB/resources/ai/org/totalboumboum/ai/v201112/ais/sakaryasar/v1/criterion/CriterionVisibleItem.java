package org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.SakarYasar;

public class CriterionVisibleItem extends AiUtilityCriterionBoolean {
	public static final String NAME = "VISIBLEITEM";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionVisibleItem(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SakarYasar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = true;
	
		//  à compléter par le traitement approprié
		if(tile.getItems()==null)
			result = false;
		
		return result;
	}

}
