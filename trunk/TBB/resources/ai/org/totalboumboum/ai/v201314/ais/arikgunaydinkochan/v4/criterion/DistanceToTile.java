package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere on va prendre de la distance d'un case.
 * Si la case est "mytile", donc la valeur de cette critere va etre 0.
 * Si la distance de cette case est petit que 4, donc la valeur va etre 1.
 * Si la distance de cette case est plus que 3, donc la valeur va etre 2
 * 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class DistanceToTile extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DistanceToTile";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceToTile(Agent ai)
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	
		ai.checkInterruption();	
		
		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile myTile = myHero.getTile();
		
		int result =2;
		int distance=zone.getTileDistance(myTile, tile);
	
		if(distance<1)
			result=0;
		else if(distance<4)
			result=1;
		else 
			result=2;
		
		return result;
	}
}
