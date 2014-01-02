package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.criterion;

import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4.Agent;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe principale de notre critère Possible Triangle : vérifie si on peut appliquer la 
 * stratégie de Triangle ou non.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class PossibleTriangle extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "POSSIBLETRIANGLE";
	
	/**
	 * Description : 
	 * 		Création d'un nouveau critère boolée qui verifie les mêmes conditions que les 
	 * 		critère "can kill" et "can lock" pour la case donnée en paramètre, mais regarde 
	 * 		aussi s'il y a aussi une bombe dans les cases voisins qui sont à distance égale 
	 * 		à la portée de la bombe de la case concernée.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public PossibleTriangle(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Possible Triangle" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * @return
	 * 		La valeur booléen de ce critère pour la case spécifiée.
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();
		return ai.attackHandler.isTriangle(tile);
	}
}
