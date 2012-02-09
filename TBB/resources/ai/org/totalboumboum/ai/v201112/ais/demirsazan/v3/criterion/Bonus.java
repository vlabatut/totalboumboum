package org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.DemirSazan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Serdil Demir
 * @author Gokhan Sazan
 */
@SuppressWarnings("deprecation")
public class Bonus extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "BONUS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Bonus(DemirSazan ai) throws StopRequestException
	{	
	
		// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
     	this.ai = ai;
     	
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirSazan ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		if(tile.getItems().size() >0)
			return true;
		return false;
	}
}
