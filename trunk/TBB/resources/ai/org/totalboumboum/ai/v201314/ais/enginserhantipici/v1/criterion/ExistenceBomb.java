package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.Agent;

/**
 * cette classe relever que le tile contient une bombe
 * si on a une bombe sur le tile on retourne true
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class ExistenceBomb extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "EXISTENCE_BOMB";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public ExistenceBomb(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	
		
		ai.checkInterruption();
		boolean result;
		AiZone zone = ai.getZone();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		List<AiTile> bombTiles = new ArrayList<AiTile>();
		bombs = zone.getBombs();
		for(AiBomb b : bombs){
			ai.checkInterruption();

			bombTiles.add(b.getTile());
		}
		if(bombTiles.contains(tile)){
			result = true;
		}else{
			result = false;
		}
	
	
		
		
	
		return result;
	}
}
