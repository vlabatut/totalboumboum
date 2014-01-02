package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3.Agent;

/**
 * Classe principale de notre critère Can Kill.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class CanKill extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "CANKILL";
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère booléen qui regarde si à l’entourage d’une distance 
	 * 		égale à la portée de la bombe de la case donnée en paramètre, il y a au moins une 
	 * 		case qui contient un adversaire ou non. Pour cela, elle verifie pour chaque 
	 * 		directions, tous les cases qui sont à une distance inférieure ou égale à la portée 
	 * 		de la bombe de notre agent, mais en eliminant les cases qui suivent d'une direction
	 * 		s'il recontre une case contenant un mur.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public CanKill(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Description:
	 * 		Calcule et renvoie la valeur de critère "Can Kill" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur booléen de ce critère pour la case spécifiée.
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{	
		ai.checkInterruption();
		return ai.attackHandler.checkIfCanKill(tile);
	}
}
