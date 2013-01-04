package org.totalboumboum.ai.v201213.ais.balyerguven.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v2.BalyerGuven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */

public class Duree extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "Duree";
	/** */
	private final int	DISTANCE_LIMIT	= 5;
	/** */
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Duree(BalyerGuven ai) throws StopRequestException
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
		
		if ( this.ai.getZone().getTileDistance( this.ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_LIMIT )
		{
			result = true;
		}
		return result;
		
	}
}