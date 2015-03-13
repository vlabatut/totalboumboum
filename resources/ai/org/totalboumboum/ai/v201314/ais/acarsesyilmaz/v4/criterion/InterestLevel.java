package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe principale de notre critère Interest Level :vérifie s'il y a item sur la case
 * dans laquelle notre agent se trouve.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class InterestLevel extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "INTERESTLEVEL";
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère de type integer. Il vérifie s'il y a item sur la case
	 * 		dans laquelle notre agent se trouve. Si oui il donne une priorité allant de 0 à 2 
	 * 		(0 pour l'item qu'on a le plus besoin et 2 la moin). Si non il donne 3.
	 * 	  
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public InterestLevel(Agent ai)
	{	
		super(ai,NAME,0,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Description:
	 * 		Calcule et renvoie la valeur de critère "Interest Level" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 		Une case qui contient un item qu'on a beaucoup besoin renvoie 0.
	 * 		Une case qui contient un item qu'on n'a pas beaucoup besoin renvoie 1.
	 * 		Une case qui contient un item qu'on n'a pas besoin renvoie 2.
	 * 		Une case qui ne contient pas d'item renvoie 4.
	 */
	@Override
	public Integer processValue(AiTile tile)
	{		
		ai.checkInterruption();		
		return ai.itemHandler.itemType(tile);
	}
}
