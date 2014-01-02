package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.Agent;

/**
 * Criteria pour la distance entre nous et l'ennemie
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DistanceEnemy";
	
	/** Domaine de définition  {true,false} 
	 * Il parle de la distance entre nous et notre ennemie.
	 * Si la distance entre notre agent
	 * et l’adversaire est plus petit ou égale a 2 , 
	 * il prend 0 comme valeur.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DistanceEnemy(Agent ai)
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
		int D=100;
		AiZone Temp=ai.getZone();
		AiHero MyHero=Temp.getOwnHero();
		List<AiHero> HList=new ArrayList<AiHero>();
		List<AiHero> Htemp=new ArrayList<AiHero>();
		Htemp=Temp.getHeroes();
		for(AiHero Hero:Htemp){
			ai.checkInterruption();
			if(!Hero.equals(MyHero)){
				
				HList.add(Hero);
			}
		}
		for(AiHero Hero:HList){
			ai.checkInterruption();
			if(Temp.getTileDistance(tile,Hero.getTile())<D){
				D=Temp.getTileDistance(tile,Hero.getTile());
			}
		}
		if(D<2){
			result=true;
		}
		return result;
	}
}
