package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.CinarYalcin;

/**
 * Returns three value
 * 2 for closest tile
 * 1 for a tile not far away
 * 0 for a tile far away
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class Distance extends AiUtilityCriterionInteger<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Distance(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiTile mytile=ai.getZone().getOwnHero().getTile();
		int mydistance=ai.getDist(mytile, tile);
		int result = 0;
		
		if(mydistance<=3) result=2;
		else{
				if(mydistance<=6)result=1;
				else result=0;
		}
		
		return result;
	}
}
