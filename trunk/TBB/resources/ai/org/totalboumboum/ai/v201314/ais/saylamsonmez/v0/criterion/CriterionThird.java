package org.totalboumboum.ai.v201314.ais.saylamsonmez.v0.criterion;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionString;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v0.Agent;

/**
 * Cette classe est un simple exemple de 
 * critère chaîne de caractères. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que le domaine
 * de définition est spécifiés dans l'appel au constructeur 
 * ({@code super(nom,inf,sup)}).
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class CriterionThird extends AiCriterionString<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "THIRD_CRITERION";
	/** Domaine de définition */
	public static final Set<String> DOMAIN = new TreeSet<String>(Arrays.asList("VALUE1","VALUE2","VALUE3","VALUE4","VALUE5"));
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public CriterionThird(Agent ai)
	{	super(ai,NAME,DOMAIN);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String processValue(AiTile tile)
	{	ai.checkInterruption();
		String result = DOMAIN.iterator().next(); // on renvoie une valeur arbitraire, pour l'exemple
		
		/*
		 *  TODO à compléter par le traitement approprié.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		return result;
	}
}
