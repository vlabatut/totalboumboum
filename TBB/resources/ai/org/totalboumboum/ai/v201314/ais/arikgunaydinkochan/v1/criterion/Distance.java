package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v1.Agent;

/**
 * Cette classe est distance critère 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Distance extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Distance(Agent ai)
	{	super(ai,NAME,0,1);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	
		ai.checkInterruption();	
		
	
		AiTile mytile=ai.getZone().getOwnHero().getTile();
		
		int distancetotile=ai.getZone().getTileDistance(mytile, tile);
		
		if(distancetotile<=3){
			
			return 0;
		}
		else
				
			return 1;	
	}
}
