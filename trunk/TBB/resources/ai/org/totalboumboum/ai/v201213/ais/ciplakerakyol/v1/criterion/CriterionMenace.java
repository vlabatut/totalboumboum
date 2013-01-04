package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionMenace extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "Securite";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionMenace(CiplakErakyol ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override

	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = true;
		int i;
		AiZone zone=ai.getZone();
		List<AiBomb>  bomba_alani= zone.getBombs(); // tous les bomb dans la zone 

		for(i=0;i<bomba_alani.size();i++)
		{
			ai.checkInterruption();
					
			if(bomba_alani.get(i).getBlast().contains(tile))
			//si le case est inclus dans la portee d'une bombe 
				result = true;			
		}


		return result;
	}
}
