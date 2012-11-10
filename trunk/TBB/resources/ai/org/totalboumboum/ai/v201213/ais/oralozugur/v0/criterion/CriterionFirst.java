package org.totalboumboum.ai.v201213.ais.oralozugur.v0.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v0.OralOzugur;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class CriterionFirst extends AiUtilityCriterionBoolean<OralOzugur>
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
	public CriterionFirst(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = true;
		ai.checkInterruption();
	
		// TODO à compléter par le traitement approprié
	
		return result;
	}
}
