package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * This criterion returns 1 or 2 if there is an enemy in current dead-end else returns 3
 * returns 1 if the mouth of tunnel is more reachable for our hero else returns 2
 * 
* @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class DeadEndContainsEnemy extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DEADENDCONTAINSENEMY";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DeadEndContainsEnemy(Agent ai)
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		int result = 3;
		List<AiHero> opponents = new ArrayList<AiHero>(ai.getZone().getRemainingOpponents());
		Map<AiTile, List<AiTile>> deadEnds = new HashMap<AiTile, List<AiTile>>(ai.getDeadEnds());
		List<AiTile> primaryTiles = new ArrayList<AiTile>();
		List<AiTile> secondaryTiles = new ArrayList<AiTile>();
		for(AiHero opponent : opponents){
			ai.checkInterruption();
			if(ai.isTileInTunnel(opponent.getTile()))
			for(AiTile mouth : deadEnds.keySet()){
				ai.checkInterruption();
				if(deadEnds.get(mouth).contains(opponent.getTile())){
					if(ai.isATileCompetible(ai.getZone().getOwnHero(), opponent, mouth)){
						if(!primaryTiles.contains(mouth) && !secondaryTiles.contains(mouth))
						primaryTiles.add(mouth);
					}
					else{
						if(!secondaryTiles.contains(mouth) && !primaryTiles.contains(mouth))
						secondaryTiles.add(mouth);
					}
				}	
			}
		}
		if(primaryTiles.contains(tile)){
			result = 1;
		}else if(secondaryTiles.contains(tile)){
			result = 2;
		}
		
		
		return result;
	}
}
