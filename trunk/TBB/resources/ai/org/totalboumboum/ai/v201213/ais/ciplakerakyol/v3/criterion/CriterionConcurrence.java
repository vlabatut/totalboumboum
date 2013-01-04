package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v3.CiplakErakyol;

/**
 * Cette classe represente le critere de concurrence.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionConcurrence extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "concurrence";
	
	/**
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionConcurrence(CiplakErakyol ai) throws StopRequestException
	{	
		super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
		AiZone zone = ai.getZone();
		AiHero ajan= zone.getOwnHero();
		int ownDistance = zone.getTileDistance(tile, ajan.getTile());
		
		for( AiHero ennemy : zone.getRemainingOpponents() )
		{
			ai.checkInterruption();
			if( ownDistance > zone.getTileDistance(tile, ennemy.getTile()) )
				result = false;
		}
		return result;
	}
}
