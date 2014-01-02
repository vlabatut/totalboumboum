package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;

/**
 * Classe principale de notre critère Can Lock : regarde s'il existe au moin 
 * un agent adversaire à l’entourage des  cases qui sont à distance égale à la portée 
 * de la bombe de la case donnée en paramètre.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class CanLock extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "CANLOCK";
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère booléen. Cette méthode regarde s'il existe au moin 
	 * 		un agent adversaire à l’entourage des  cases qui sont à distance égale à la portée 
	 * 		de la bombe de la case donnée en paramètre. Pour chaque direction, elle regarde les 
	 * 		voisins et vérifie s'il y a un agent adversaire, mais en eliminant les cases qui 
	 * 		suivent d'une direction s'il recontre une case contenant un mur.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public CanLock(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Description:
	 * 		Calcule et renvoie la valeur de critère "Can Lock" pour la case passée en paramètre.
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
		return ai.attackHandler.checkIfCanLock(tile);
	}
}
