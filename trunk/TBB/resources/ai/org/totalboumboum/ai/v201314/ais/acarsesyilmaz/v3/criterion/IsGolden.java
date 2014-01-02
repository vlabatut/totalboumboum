package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3.criterion;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3.Agent;

/**
 * Classe principale de notre critère Is Golden.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class IsGolden extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISGOLDEN";
	
	/**
	 * Description : 
	 * 		Création d'un nouveau critère booléen qui vérifie s'il y a un item de type 
	 * 		Golden ou non.
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
	 */
	@Override
	public Boolean processValue(AiTile tile)
	{
		ai.checkInterruption();
		return ai.itemHandler.isGoldenExists(tile);
	}
}
