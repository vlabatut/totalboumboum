package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v0.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v0.CiplakErakyol;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionFirst extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "FIRST";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionFirst(CiplakErakyol ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'agent associé au traitement */ 
	protected CiplakErakyol ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = true;
	
		// TODO à compléter par le traitement approprié
	
		return result;
	}
}
