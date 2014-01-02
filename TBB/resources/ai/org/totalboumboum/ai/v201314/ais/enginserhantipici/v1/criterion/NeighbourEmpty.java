package org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v1.Agent;

 /**
		 * ici on compte les murs autour d'une tile
		 * si on a 0,1 ou 2 tile on retourne false
		 * ou si on a 4 ou plus beaucoup de mur, on retourne true 
		 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class NeighbourEmpty extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "VOISIN_VIDE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public NeighbourEmpty(Agent ai)
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
		List<AiTile> neighbours = new ArrayList<AiTile>();
		neighbours = tile.getNeighbors();
		int v = 0;
		for(AiTile neighbour : neighbours){
			ai.checkInterruption();

			if(neighbour.getBlocks().isEmpty()){
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
