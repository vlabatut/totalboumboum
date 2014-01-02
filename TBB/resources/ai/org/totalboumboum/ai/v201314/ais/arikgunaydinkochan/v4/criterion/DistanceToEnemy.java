package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere on va prendre de la distance de l'adversaire d'un case.
 * Si la distance est plus petit que 2, donc la valeur de cette critere va etre 0.
 * Si la distance est plus petit que 5, donc la valeur va etre 1.
 * Sinon la valeur retourne 2.
 * 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class DistanceToEnemy extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DistanceToEnemy";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceToEnemy(Agent ai)
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
		
		List<AiHero> heroes=zone.getRemainingOpponents();
		
		int result =2;
		int distance = 100;
		
		for(AiHero hero:heroes){
			ai.checkInterruption();
			AiTile heroTile = hero.getTile();
			if(distance>zone.getTileDistance(heroTile, tile))
				distance=zone.getTileDistance(heroTile, tile);
		}
		if(distance<2)
			result=0;
		else if(distance<5)
			result=1;
		else 
			result=2;
		
		return result;
	}
}
