package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;


import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * Cette classe represente le critere de concurrence.
 * Il indique s'il y a une adversaire que se trouve plus proche que notre IA.
 * Si oui , la fonction renvoie false.
 * @author Merve Ünlü
 * @author Gülay Yildirim
 */
public class CriterionConcurrence extends AiUtilityCriterionBoolean
{	
	public static final String NAME = "CONCURRENCE";

	public CriterionConcurrence(UnluYildirim ai) throws StopRequestException
	{	
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
    ai.checkInterruption();	
	Boolean result =true;
	AiZone zone ;
	AiHero myhero;
	List<AiHero> zone_heros ;
	zone=ai.getZone();
	myhero=zone.getOwnHero();
	zone_heros = zone.getRemainingOpponents();
	double duration =0 ;
	double duration_heros=0;
	
	
		
	int distance = zone.getTileDistance(myhero.getTile(), tile);
	duration = distance/myhero.getWalkingSpeed(); // le temps d'arrivée de notre IA 
	
	int distance_hero = 0 ;
	
	for(AiHero hero : zone_heros)//pour chaque hero dans la zone 
	{
		ai.checkInterruption();
		distance_hero = zone.getTileDistance(hero.getTile(), tile); 
		duration_heros = distance_hero/ hero.getWalkingSpeed();//le temps d'arrivé des autres IA
		
		if(duration_heros<duration)//S'il existe une adversaire que se trouve plus proche que notre , il renvoie false.
			{
			return false;
			}
	}
		
		return result;
	}
}
