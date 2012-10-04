package org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion;

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
public class Time extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Time";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Time(DemirelOz ai) throws StopRequestException
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
		Time time = new Time(this.ai);

		if(this.ai.getZone().getTileDistance(this.ai.getZone().getOwnHero().getTile(), tile)<=3)	
		{
			result = true;
		}

		return result;
	}


}

