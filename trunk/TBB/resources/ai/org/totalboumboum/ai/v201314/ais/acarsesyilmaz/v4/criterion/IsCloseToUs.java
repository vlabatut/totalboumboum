package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;

/**
 * Classe principale de notre critère Is Close To Us : trouve l'ennemi la plus proche à nous
 * dans les cases accessibles
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class IsCloseToUs extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISCLOSETOUS";
	
	/**
	 * Description :
	 * 		Création d'un nouveau critère booléen. Il trouve l'ennemi la plus proche à nous
	 * 		dans les cases accessibles (s'il existe) avec notre méthode "getAccessibleTiles".
	 *		Puis il vérifie si cette agent est accessible, c'est-à-dire si celui-ci se trouve
	 *		sur une case accessible ou non avec les méthodes "processAccessibleTiles", 
	 *		"getAccessibleTiles" et "isEnemyReachable".
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public IsCloseToUs(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Calcule et renvoie la valeur de critère "Is Close To Us" pour la case passée en paramètre.
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
		return ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile)<1;
	}
}
