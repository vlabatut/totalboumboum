package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * It returns true if there is more then 3 empty tiles around the current tile
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class EmptyNeighbour extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "VOISIN_VIDE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public EmptyNeighbour(Agent ai)
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
		
		List<AiTile> neighbours = new ArrayList<AiTile>();
		neighbours = tile.getNeighbors();
		int v = 0;
		for(AiTile voisin : neighbours){
			ai.checkInterruption();
			if(voisin.getBlocks().isEmpty()){
				v++;
			}
		}
		if(v >= 3){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
}
