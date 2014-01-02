package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;

/**
 * Classe principale de notre critère Is Close To Enemy : regarde si l'item est proche 
 * a notre adversaire de deux cases ou non
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class IsCloseToEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISCLOSETOENEMY";
	
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère de type booléen qui regarde si l'item est proche 
	 * 		a notre adversaire de deux cases ou non
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
	 * Description : 
	 * 		Calcule et renvoie la valeur de critère "Is Close To Enemy" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		Renvoie true 
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();		
		boolean result;
		AiHero closestEnemy = ai.securityHandler.getClosestEnemy(ai.getZone().getOwnHero().getTile());
		
		if(closestEnemy != null)
		{
			result = ai.getZone().getTileDistance(closestEnemy.getTile(), tile)<2;
		}
		else
		{
			result = false;
		}
		return result;
	}
}
