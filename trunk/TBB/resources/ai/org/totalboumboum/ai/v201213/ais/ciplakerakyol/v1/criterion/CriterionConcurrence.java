package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionConcurrence extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "concurrence";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
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
		Boolean result = false;
		int i;
		
		AiZone zone=ai.getZone();
		AiHero ajan= zone.getOwnHero();
		List<AiHero>  opponents=  zone.getRemainingOpponents();
		
		for(i = 0;i<opponents.size();i++)
		{
			ai.checkInterruption();
			
			if(zone.getTileDistance(tile, ajan.getTile())/(ajan.getWalkingSpeed())
				>zone.getTileDistance(tile,opponents.get(i).getTile())/(opponents.get(i).getWalkingSpeed()))
			{
				result = true;
			}	
		}

		return result;
	}
}
