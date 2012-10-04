package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionDistanceHero extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "FIRST";
	
	/**
	 * Ce critere a pour but de determiner s'il existe un adversaire 
	 * qui se trouve dans une case plus proche que notre au tile defini.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDistanceHero(UnluYildirim ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;
	
		AiZone zone=ai.getZone(); 
		List<AiHero> zone_heros ; 
		zone_heros= zone.getRemainingOpponents(); //prend les heros dans la zone
		AiHero ownHero = zone.getOwnHero(); //notre hero 
		AiTile tile_hero=ownHero.getTile();
		

		
		int i=0;
		int distance =0;
		int distance1=0 ; 
		double own_vitesse =0, hero_vitesse=0;
		while(i<zone_heros.size() && ((distance/own_vitesse ) < (distance /hero_vitesse))) 
		{//regarde tous les heros dans la zone et fait le controle la distace de notre Ia et les autres au tile spécifié
		 //S'il y un adversaire plus proche que notre , donc while arrete et retourne false	
			distance=zone.getTileDistance(tile_hero, tile);
			distance1=zone.getTileDistance(zone_heros.get(i).getTile(),tile);
			own_vitesse=ownHero.getWalkingSpeed();
			hero_vitesse=zone_heros.get(i).getWalkingSpeed();
			i++;
		}
		if(i==zone_heros.size())
			result=true;
		
	
		return result;
	}
}
