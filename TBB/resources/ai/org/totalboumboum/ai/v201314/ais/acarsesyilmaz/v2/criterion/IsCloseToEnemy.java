package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.Agent;

/**
 * Classe principale de notre critère IsCloseToEnemy
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class IsCloseToEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISCLOSETOENEMY";
	
	/**
	 *  Cette méthode vérfifie si l'ennemie est a une distance inférieur ou égale a 2 cases.
	 *  Si oui elle renvoie true, sinon false.
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public IsCloseToEnemy(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Is Close To Enemy" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 		si la case vérifie la condition de notre critère elle renvoie true
	 * 		sinon elle renvoie false
	 * 		
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();
		return ai.isCloseToEnemy(tile);
	}
}
