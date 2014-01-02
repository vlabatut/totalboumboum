package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.Agent;


/**
 * cette classe trouve le distance entre les adversaires et les tiles.
	 * pour cette critere, on calcule le distance entre tile et tile de notre agent
	 * si on est sur meme tile, result est true
	 * 
	 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE_ADVERSAIRE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceEnemy(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	
	boolean result;
	
	AiZone zone = ai.getZone();
	AiTile currentTile = zone.getOwnHero().getTile();
	int d;
	d = zone.getTileDistance(currentTile, tile);
	if(d == 0){
		result = true;
	}else{
		result = false;
	}
	
	
		return result;
	}
}
