package org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.DemirelOz;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class Competition extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Competition";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Competition(DemirelOz ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirelOz ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	@Override
	
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	
	boolean result = false;
	Competition competition = new Competition(this.ai);
	
	if(this.ai.getZone().getRemainingOpponents().isEmpty()!=false)
	{	
	for (AiHero currentopponent : this.ai.getZone().getRemainingOpponents())
	{
		if(this.ai.getZone().getTileDistance(currentopponent.getTile(),tile) <=5 )
			{
		result = true;
	        }
		else 
			result =false;
	}
	}
	
	return result;
	}
}