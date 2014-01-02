package org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.Agent;



/**
 * Cette critere est pour savoir que si une case est dangereuse ou pas
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class Danger extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DANGER";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Danger(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS		    /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	
		boolean result = false;
		
		for (AiItem item: tile.getItems()) {
			ai.checkInterruption();
			
			if(ai.isMalus(item))
				return true;
		}
		
		if(!(this.ai.dangerousTiles().isEmpty()) && (this.ai.dangerousTiles().contains(tile))) {
			return true;
		}
		
		return result;
	}
}
