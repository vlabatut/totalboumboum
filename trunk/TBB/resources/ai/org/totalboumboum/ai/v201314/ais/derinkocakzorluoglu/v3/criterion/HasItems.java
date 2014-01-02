package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.Agent;

/**
 * HasItems
 * On regarde si il y a un item dans cette case. On regarde aussi le type de l'item.
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class HasItems extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "HasItems";
	
	/**
	 * Domaine de definition={0,1,2}
	 * 2 si il n'y a pas d'item dans cette tile
	 * 1 si il y a un malus dans cette case
	 * 0 si il y a un bon item 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public HasItems(Agent ai)
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 2;
		if(!tile.getItems().isEmpty()){
			if(tile.getItems().get(0).getType().equals(false)){
				result=1;
			}
			else result=0;
		}
		return result;
	}
}
