package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.Agent;

/**
 * Cette classe relever que la tile est menace d'une bombe ou pas.
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
		boolean result = true;
		AiZone zone = ai.getZone();
		List<AiTile> dangerTiles = new ArrayList<AiTile>();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		bombs = zone.getBombs();
		long time;
		for(AiBomb bomb : bombs){
			ai.checkInterruption();

			time = bomb.getNormalDuration() - bomb.getElapsedTime();
			
			dangerTiles.addAll(bomb.getBlast());
			if(dangerTiles.contains(tile) && time < 2000){
				result = true;
				break;
			}
			else{
				dangerTiles.clear();
				result = false;
			}
		}
		
		
		
		
		
	
		return result;
	}
}
