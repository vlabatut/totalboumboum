package org.totalboumboum.ai.v201314.ais.asilizeryuce.v0.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v0.Agent;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class CriterionSecond extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "SECOND_CRITERION";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public CriterionSecond(Agent ai)
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 2;
		
		/*
		 *  TODO à compléter par le traitement approprié.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		return result;
	}
}
