package org.totalboumboum.ai.v201112.ais.unluyildirim.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v2.UnluYildirim;

/**
 * Cette classe represent le critere besoin
 * Si l'agent a besoin l'item dans le tile , il retourne true.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionBesoin extends AiUtilityCriterionBoolean
{	
	public static final String NAME = "BESOIN";
	

	public CriterionBesoin(UnluYildirim ai) throws StopRequestException
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
	{	ai.checkInterruption();
		boolean result = true;
	
		AiZone zone = ai.getZone();
		AiHero myhero ;
		int hero_range , hero_bomb; 
	
		
		
		myhero = zone.getOwnHero();
		hero_range=myhero.getBombRange();
		hero_bomb=myhero.getBombNumberMax();
		if(hero_bomb<3 && hero_range<3)	//si l'agent a besoin les deux type des bonus 
		{
			ai.checkInterruption();
			if(!tile.getItems().isEmpty()){
			for(AiItem item : tile.getItems())
			{
				ai.checkInterruption();
				if(item.getType()==AiItemType.EXTRA_BOMB || item.getType()==AiItemType.EXTRA_FLAME)
				//Si le tile possed l'un de ces deux type , il retourne true
				   return true;
			    
				else{
					//Si le tile ne possed pas , il retourne false
					return false ; }
				}
		}}
		else{
			if(hero_bomb<3 && hero_range>3)
			{//Si l'agent a besoin EXTRA_BOMB 

				if(!tile.getItems().isEmpty()){
				for(AiItem item : tile.getItems())
				{
					ai.checkInterruption();
					if(item.getType()==AiItemType.EXTRA_BOMB)
						//Si le tile possede le bonus EXTRA_BOMB , il retourne true
					   return true;
				    
					else{
						return false ; }
					}
			}
				else{
					if(hero_bomb>3 && hero_range<3)
					{//Si l'agent a besoin EXTRA_FLAMME
						if(!tile.getItems().isEmpty()){
							for(AiItem item : tile.getItems())
							{
								ai.checkInterruption();
								if(item.getType()==AiItemType.EXTRA_FLAME)
									//Si le tile possede le bonus de type EXTRA_FLAMME , il retourne true
								   return true;
							    
								else{
									return false ; }
								}
					}
				}
		}
	
		}
		}
	
		return result;
	}
}
