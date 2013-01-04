package org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.CinarYalcin;

/**
 *Return true if there is an opponent closer than us to selected tile
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */

public class Concurrence extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "Concurrence";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Concurrence(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result=false;
		AiTile mytile=this.ai.getZone().getOwnHero().getTile();
		int mydistance=this.ai.getZone().getTileDistance(mytile, tile);
	
		for(AiHero currentEnemy:this.ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			if(((double)this.ai.getZone().getTileDistance(currentEnemy.getTile(),tile)/currentEnemy.getWalkingSpeed()
					)<((double)mydistance/this.ai.getZone().getOwnHero().getWalkingSpeed()))
			result=true;
		}
	
		return result;
	}
}
