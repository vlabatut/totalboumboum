package org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.OralOzugur;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class Danger extends AiUtilityCriterionBoolean<OralOzugur>
{	/** Nom de ce critère */
	public static final String NAME = "Danger";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Danger(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		AiZone zone=ai.getZone();
		for(AiBomb bombs : zone.getBombs())
		{
			ai.checkInterruption();
			if(bombs.getBlast().contains(tile))
			{
				result=true;
			}
		}
		
	
		return result;
	}
}
