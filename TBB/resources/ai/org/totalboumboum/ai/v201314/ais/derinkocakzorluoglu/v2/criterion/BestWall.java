package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.Agent;

/**
  * Il retourne vrai pour les cases qui sont à côte des mur destructibles et l’autre face de
 * mur est un tile libre.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class BestWall extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "BestWall";
	
	/**
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
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();

	/*	
	boolean result = false;
	
		for(AiTile Tile:ai.getMurtile().getNeighbors()){
			if(tile==Tile)
				result=true;			
		}
		*/
		return true;
	}
}
