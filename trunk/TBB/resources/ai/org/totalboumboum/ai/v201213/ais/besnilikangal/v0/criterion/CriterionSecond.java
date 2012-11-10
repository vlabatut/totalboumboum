package org.totalboumboum.ai.v201213.ais.besnilikangal.v0.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v0.BesniliKangal;

/**
 * Cette classe représente est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
public class CriterionSecond extends AiUtilityCriterionInteger<BesniliKangal>
{	/** Nom de ce critère */
	public static final String NAME = "SECOND";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionSecond(BesniliKangal ai) throws StopRequestException
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		int result = 2;
		
		// TODO à compléter par le traitement approprié
		
		return result;
	}
}
