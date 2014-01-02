package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.criterion;


import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.Agent;

/**
 * Cette classe est un critère 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class DesturictibleMurs extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DesturictibleMurs";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DesturictibleMurs(Agent ai)
	{	super(ai,NAME,0,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile){	
		ai.checkInterruption();
	
		int result = 0;
		ArrayList<AiBlock> block = new ArrayList<AiBlock>();
		for(AiTile t:tile.getNeighbors()){
			ai.checkInterruption();
			block.addAll(t.getBlocks());
		}
		if(!block.isEmpty()){
			for(AiBlock b : block){
				ai.checkInterruption();
				if(b.isDestructible()){
					result++;
				}
			}	
		}
		if(result>=3)
			result=3;
		return result;
	}
}
