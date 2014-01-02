package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.Agent;

/**
 * The criteria for distance . Il va retourner false si la case est  proche a nous sinon il va etre true.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Distance extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/** Domaine de définition  {true,false} 
	 * Si la distance entre notre agent et cette case est plus petit ou égale a 4
     * il prend 0 comme valeur. Sinon il va être 1.
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Distance(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	boolean result = false;
		
		
		int a;
		
		a=ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),tile);
		if(a<=4)
			result=true;
		
		return result;
	}
}
