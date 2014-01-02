package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere on examine de distance de cette case par rapport a l'adversaire et par rapport a notre agent.
 * Si l'adversaire est plus proche que notre agent a cette case, la valeur retourne false.
 * Si notre agent est plus proche que l'adversaire a cette case, la valeur retourne true.
 * Alors comme ça, on peut decider d'aller a cette case.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Concurrence extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Concurrence";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Concurrence(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();
		
		AiZone zone=ai.getZone();
		AiTile myTile=zone.getOwnHero().getTile();
			
		int myDistance = ai.getZone().getTileDistance( myTile, tile );
		
		for(AiHero hero : zone.getRemainingOpponents()){
				ai.checkInterruption();
				AiTile heroTile=hero.getTile();
				if ( ai.accessibleTiles.contains(heroTile)
						&&zone.getTileDistance( heroTile, tile ) < myDistance ) 
						return false;
		}
		return true;			
	}
}
