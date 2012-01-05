package org.totalboumboum.ai.v201112.ais.sakaryasar.v2.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v2.SakarYasar;

public class CriterionEnemyDirection extends AiUtilityCriterionBoolean{
	public static final String NAME = "ENEMYDIRECTION";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionEnemyDirection(SakarYasar ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
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
	{	ai.checkInterruption();
		boolean result = false;
		List<AiHero> heroes = ai.getZone().getHeroes();
		Iterator<AiHero> itHeroes = heroes.iterator();
		AiHero temp;
		AiHero ownHero = ai.getZone().getOwnHero();
		
		while(itHeroes.hasNext() && !result){
			ai.checkInterruption();
			temp = itHeroes.next();
			if(temp != ownHero && (temp.getTile().getCol() == tile.getCol() || temp.getTile().getRow() == tile.getRow()))
				result = true;
		}
		return result;
	}

}
