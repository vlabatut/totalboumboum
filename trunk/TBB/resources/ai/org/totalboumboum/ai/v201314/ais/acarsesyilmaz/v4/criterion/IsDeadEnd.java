package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de notre critère Is Dead End : permet de savoir si un ennemi est 
 * peut etre facilement tué.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class IsDeadEnd extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISDEADEND";
	
	/**
	 * Description : 
	 * 		Création d'un nouveau critère booléen qui permet de savoir si un ennemi est 
	 * 		peut etre facilement tué. C'est-à-dire, il regarde le niveau de disponibilité
	 * 		de la case de l'ennemi, si c'est tres élevé il regarde s'il peut etre tuer si
	 * 		on pose une bombe.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public IsDeadEnd(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * Calcule et renvoie la valeur de critère "Is Dead End" pour la case passée en paramètre.
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
		return ai.securityHandler.isDeadEnd(tile,Direction.NONE);
	}
}
