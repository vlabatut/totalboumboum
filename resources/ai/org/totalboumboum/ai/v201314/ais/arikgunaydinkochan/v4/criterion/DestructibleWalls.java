package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;


import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere on va prendre des numbres des murs destructible d'un case
 * S'il y a just 1 destructible mur autour de cette case, donc la valeur va etre retourné 1.
 * S'il y a 2 destructibles murs autour de cette case, donc la valeur va etre retourné 2.
 * S'il y a 3 destructibles murs autour de cette case, donc la valeur va etre retourné 3.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class DestructibleWalls extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DesturictibleMurs";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DestructibleWalls(Agent ai)
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
