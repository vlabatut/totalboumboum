 package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionTypeBonus extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "TYPEBONUS";
	
	/**
	 * Ce critère détermine notre besoin et regarde si le tile contient
	 * l'item que l'on a besoin.Si on a besoin , il donne la valeur 1 ; 
	 * sinon il donne la valeur 0.
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionTypeBonus(UnluYildirim ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,1,3);
		
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
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	int result = 0;
		
	      AiZone zone = ai.getZone(); // prend la zone 
	      AiItemType type ;
	      AiHero ownHero ;
	      
	      ownHero=zone.getOwnHero(); //notre hero 
	      List<AiItem>  tile_item;
	      tile_item=tile.getItems(); // items dans la tile defini 
	      
	      
	      int i;
	      for(i=0;i<tile_item.size();i++)
	      {
	    	  type=tile_item.get(i).getType() ;
	    	  if(type.equals(AiItemType.EXTRA_BOMB) &&  (ownHero.getBombNumberMax()<2)) 
	    	  {//regarde si le tile contient le bonus EXTRA_BOMB quand on a besoin
	    		  result=1;
	    	  }
	    	  if(type.equals(AiItemType.EXTRA_BOMB) &&  (ownHero.getBombNumberMax()>2))
	    	  {//regarde si le tile contient le bonus EXTRA_BOMB  , mais on n'a pas besoin 
	    		  result=0;
	    	  }
	    	  if(type.equals(AiItemType.EXTRA_FLAME) &&  (ownHero.getBombRange()<2))
	    	  {// regarde si le tile contient le bonus EXTRA_FLAME et on a besoin
	    		  result=1;
	    	  }
	    	  if(type.equals(AiItemType.EXTRA_FLAME) &&  (ownHero.getBombRange()>2))
	    	  {//regarde si le tile contient le bonus EXTRA_FLAME  , mais on n'a pas besoin 
	    		  result=0;
	    	  }
	    	  
	      }
		
		return result;
	}
}
