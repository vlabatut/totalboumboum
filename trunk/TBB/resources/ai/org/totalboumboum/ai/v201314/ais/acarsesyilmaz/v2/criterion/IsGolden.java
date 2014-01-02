package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.criterion;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.Agent;

/**
 * Classe principale de notre critère IsGolden
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class IsGolden extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISGOLDEN";
	
	/**
	 *  Cette méthode vérfifie s'il existe un golden item sur la case passée en parametre.
	 *  Si oui elle renvoie true, sinon false.
	 * 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public IsGolden(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Is Golden" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur booléen de ce critère pour la case spécifiée.
	 * 		true s'il y a un golden item sur la case
	 * 		false sinon
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();
		return ai.isGoldenExists(tile);
	}
}
