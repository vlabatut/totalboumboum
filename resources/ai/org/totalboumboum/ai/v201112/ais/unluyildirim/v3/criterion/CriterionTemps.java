package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * 
 * @author Merve Ünlü
 * @author Gülay Yildirim
 */
@SuppressWarnings("deprecation")
public class CriterionTemps extends AiUtilityCriterionBoolean
{	/** */
	public static final String NAME = "TEMPS";
	
	/**
	 * Ce critere renvoie une true si le tile possede le plus proche item/adversaire
	 * 
	 * @param ai 
	 * 		description manquante !
	 * @throws StopRequestException	
	 * 		description manquante !
	 */
	public CriterionTemps(UnluYildirim ai) throws StopRequestException
	{	
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	ai.checkInterruption();
	boolean result = true;
	
	AiZone zone ;
	AiTile tile_hero ;
	AiHero myHero; 
	int distance ,distance_tile=0 ;
	List<AiItem> tile_item ; 
	List<AiItem> zone_item ;
	
	
	zone=ai.getZone(); 
	myHero =zone.getOwnHero(); 
	tile_hero=myHero.getTile(); //prend la valeur du tile de hero 
 	distance =zone.getTileDistance(tile, tile_hero) ; //la distance entre le tile de l'hero et le tile ÃƒÂ  calculer 
    tile_item = tile.getItems(); // tous les items dans le tile 

	
	int i=0;
	
	if(!tile_item.isEmpty()) // si on s'interresse aux bonus 
	{    ai.checkInterruption();
		zone_item =zone.getItems();
		for(AiItem item : zone_item)
		{   ai.checkInterruption();
			
			distance_tile =zone.getTileDistance(item.getTile(),zone.getOwnHero().getTile());
			
			i++;
			if(distance_tile<distance)
				return false ;
		}
		if(i==zone_item.size())
			return true ;
		
	}
	else{
		 //Si on s'interrese aux adversaires 
			
		ai.checkInterruption();
			List<AiHero> zone_hero ;
			
			zone_hero =zone.getRemainingOpponents();
			i=0;
			if(!zone_hero.isEmpty())
			{	distance=zone.getTileDistance(zone_hero.get(0).getTile(),zone.getOwnHero().getTile());
				AiHero hero_proche = zone_hero.get(0);
				for(AiHero hero : zone_hero )
				{
					ai.checkInterruption();
					distance_tile = zone.getTileDistance(hero.getTile(),zone.getOwnHero().getTile());
					
					i++;
					if(distance_tile<distance)
						{
						ai.checkInterruption();
						
							distance = distance_tile ;
							hero_proche = hero ;
							
							
						}
	
				}
	
				if(zone.getTileDistance(tile, hero_proche.getTile()) <= myHero.getBombRange())
				{
				//Si le tile possede le plus proche item/adversaire , il renvoie true.
					return true;
				}
				else
					return false;
			}
			return true;
	}

		return result;
	}
}
