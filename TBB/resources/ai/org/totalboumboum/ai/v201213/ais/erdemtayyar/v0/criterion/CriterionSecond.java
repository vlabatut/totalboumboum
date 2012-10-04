package org.totalboumboum.ai.v201213.ais.erdemtayyar.v0.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v0.ErdemTayyar;

/**
 * Cette classe représente est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class CriterionSecond extends AiUtilityCriterionInteger
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
	public CriterionSecond(ErdemTayyar ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,1,3);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'agent associé au traitement */ 
	protected ErdemTayyar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	int result = 2;
		
		// TODO à compléter par le traitement approprié
		
		return result;
	}
}
