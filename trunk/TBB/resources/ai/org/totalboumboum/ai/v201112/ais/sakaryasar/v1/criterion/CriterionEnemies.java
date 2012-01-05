package org.totalboumboum.ai.v201112.ais.sakaryasar.v1.criterion;

import java.util.Iterator;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v1.SakarYasar;

public class CriterionEnemies extends AiUtilityCriterionBoolean{
	public static final String NAME = "ENEMIES";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionEnemies(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected SakarYasar ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		boolean result = false;

		int distOfOurHero = ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile);
		Iterator<AiHero> heroes = ai.getZone().getHeroes().iterator();
		
		while(heroes.hasNext() && !result){
			if(ai.getZone().getTileDistance(heroes.next().getTile(),tile) < distOfOurHero)
				result = true;
		}
		
		return result;
	}

}
