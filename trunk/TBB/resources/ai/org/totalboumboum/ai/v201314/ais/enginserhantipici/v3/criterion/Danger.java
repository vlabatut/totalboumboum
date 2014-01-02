package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * This criterion returns true if the tile is in blast of a bomb that will explode in 3 second.
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class Danger extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";
	
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
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
		boolean result = false;
		AiZone zone = ai.getZone();
		List<AiTile> dangerTiles = new ArrayList<AiTile>();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		bombs = zone.getBombs();
		long time;
		
		if(!bombs.isEmpty()){
			
		for(AiBomb bomb : bombs){
			ai.checkInterruption();
			time = bomb.getNormalDuration() - bomb.getElapsedTime();
			dangerTiles.addAll(bomb.getBlast());
				
			if(dangerTiles.contains(tile) && time < 3000){
				result = true;
				break;
			}
			else{
				result = false;
			}
		}
		}else{
			
			result = false;
		}
	
		return result;
	}
}
