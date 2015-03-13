package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion;

//import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.Agent;

/**
 * BestWall
 * On regarde si la tile trouve dans le liste de bestWallTile.
 * bestWallTile = Les tiles a cotes des murs destructible,
 * qui sont les plus proches a l'ennemie 
 * et les qui sont accessible directement.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
@SuppressWarnings("deprecation")
public class BestWall extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "BestWall";
	
	/**
	 * Il retourne vrai si il est dans le liste bestWallTile
     * non si il n'est pas dans la liste.
	 * Domaine de definition = { true , false }
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public BestWall(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	if(ai.bestWallTile!=null){
		if (ai.bestWallTile.contains(tile)) {
			return true;
		}
	}
	return false;	
	}
	
}
