package org.totalboumboum.ai.v201112.ais.ilterkaya.v0.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.ilterkaya.v0.IlterKaya;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Mustafa Kaan İlter
 * @author Önder Kaya
 */
@SuppressWarnings("deprecation")
public class CriterionSecond extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "SECOND";
	
	/**
	 * Crée un nouveau critère entier.
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionSecond(IlterKaya ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,1,3);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected IlterKaya ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	int result = 2;
		
		// à compléter par le traitement approprié
		
		return result;
	}
}
