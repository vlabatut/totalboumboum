package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.Agent;



/**
 * Criteria for danger .
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Danger extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Danger";
	
	
	/** Domaine de définition  {0,1,2}
	 * 0 quand il n'y a pas du danger dans cette tile
	 * 1 quand il y a un bombe dans cette tile 
	 * 2 quand il y a un bombe qui va exploiter dans un temps tres proche ou il y a un feu dans cette tile 
	 *  
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 */
	public Danger(Agent ai)
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();			
		return ai.getDangerValue(tile);	
	}

}
