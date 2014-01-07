package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;

/**
 * Classe principale de notre critère Is Closest Item : trouve les bonus qui sont les
 * plus proche à nous.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class IsClosestItem extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISCLOSESTITEM";
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère de type integer. Il trouve les bonus qui sont les
	 * 		plus proche à nous avec notre méthode "getClosestItems" et renvoie la distance 
	 * 		la plus proche avec notre méthode "getClosestItemDistance".
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */

	public IsClosestItem(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Description:
	 * 		Calcule et renvoie la valeur de critère "Is Closest Item" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur integer de ce critère pour la case spécifiée.
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();		
		AiTile ownTile = ai.getZone().getOwnHero().getTile();		
		return ai.getZone().getTileDistance(ownTile, tile) == ai.itemHandler.getClosestItemDistance(ownTile);
	}
}
