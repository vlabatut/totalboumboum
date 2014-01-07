package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe qui contient les méthodes concernant les items.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class ItemHandler extends AiAbstractHandler<Agent>{

	/**
	 * Appel à la classe ItemHandler
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public ItemHandler(Agent ai) 
	{
		super(ai); 
		ai.checkInterruption(); 
	}
		
	/**
	 * Description :
	 * 		Cette méthode regarde s'il y a un item sur la case ou non et s'il existe un item
	 * 		elle donne une priorité allant de 0 à 2 (0 pour l'item qu'on a le plus besoin et 
	 * 		2 la moin). Si non il donne 3.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 		Une case qui contient un item qu'on a beaucoup besoin renvoie 0.
	 * 		Une case qui contient un item qu'on n'a pas beaucoup besoin renvoie 1.
	 * 		Une case qui contient un item qu'on n'a pas besoin renvoie 2.
	 * 		Une case qui ne contient pas d'item renvoie 4.
	 */
	public int itemType(AiTile tile)
	{
		ai.checkInterruption();
		int result = 3;
		AiHero ownHero = ai.getZone().getOwnHero();
		
		if(tile.getItems().isEmpty())
		{
			result = 3;
		}
		else
		{
			for(AiItem item : tile.getItems())
			{
				ai.checkInterruption();
				if(!item.getType().isBonus())
				{
					result = 3;
				}
				else
				{
					if(item.getType() == AiItemType.EXTRA_BOMB)
					{
						if(ownHero.getBombNumberMax() == 1)
						{
							result = 0;
						}
						else if(ownHero.getBombNumberMax() == ownHero.getBombNumberLimit())
						{
							result = 3;
						}
						else
						{
							result = 1;
						}
					}
					else if(item.getType() == AiItemType.EXTRA_SPEED)
					{
						if(ownHero.getBombNumberMax() == 1)
						{
							result = 1;
						}
						else
						{
							result = 0;
						}
					}
					else if(item.getType() == AiItemType.EXTRA_FLAME)
					{
						if(ownHero.getBombRangeLimit() == ownHero.getBombRange())
						{
							result = 3;
						}
						else
						{
							result = 2;
						}
					}					
				}				
			}
		}	
		
		return result;
	}


	/**
	 * Description :
	 * 		Cette méthode regarde s'il y a un item de type Golden dans la case à traiter.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		renvoie true s'il existe et false sinon
	 */
	public boolean isGoldenExists(AiTile tile)
	{		
		ai.checkInterruption();
		boolean result;			
		
		if(tile.getItems().isEmpty())
		{
			result = false;
		}
		else
		{
			if(tile.getItems().get(0).getType() == AiItemType.GOLDEN_BOMB || tile.getItems().get(0).getType() == AiItemType.ANTI_FLAME || tile.getItems().get(0).getType() == AiItemType.ANTI_SPEED)
			{
				result = true;
			}	
			else
			{
				result = false;
			}
		}
		return result;			
	}
	

	/**
	 * Description : 
	 * 		Cette méthode regarde si un bonus accessible ou non. En effet,
	 * 		il regarde si le bonus se trouve dans une case accessible.
	 * 
	 * @return
	 * 		renvoie true s'il est accessible et false sinon
	 */
	public boolean isBonusReachable()
	{
		ai.checkInterruption();
		boolean result = false;		
		ai.securityHandler.updateAccessibleTiles();
		for(AiTile tile : ai.securityHandler.getAccessibleTiles(ai.getZone().getOwnHero().getTile()))
		{
			ai.checkInterruption();
			if(!result)
			{
				for(AiItem item : tile.getItems())
				{
					ai.checkInterruption();
					if(item.getType().isBonus())
					{
					result = true;
					}
				}
			}
		}			
		return result;	
	}
		

	/**
	 * Description : 
	 * 		Cette méthode met les bonus qui sont les plus proche à nous et les mets
	 * 		dans une liste. En effet, elle prend seulement le bonus qui est le plus 
	 * 		proche à nous met s'il y a encore d'autres items qui sont à la meme
	 * 		distance elle les prends aussi dans la liste.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		Renvoie la liste
	 */
	public List<AiItem> getClosestItems(AiTile tile)
	{
		ai.checkInterruption();
		int minDistance = 10000;
		List<AiItem> result = new ArrayList<AiItem>();
		
		for(AiItem item : ai.getZone().getItems())
		{
			ai.checkInterruption();
			int distance = ai.getZone().getTileDistance(item.getTile(), tile);
			if(distance<minDistance)
			{
				minDistance = distance;
				result.clear();
				result.add(item);
				
			}
			else if(distance==minDistance)
			{
				result.add(item);
			}
		}		
		return result;	
	}
	
	
	/**
	 * Description :
	 * 		Cette méthode prend la liste des bonus qui sont les plus proches à notre agent
	 * 		et donne la distance de celui-ci.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		Renvoie la distance en case du bonus qui est le plus proche de notre agent.
	 */
	public int getClosestItemDistance(AiTile tile)
	{
		ai.checkInterruption();
		List<AiItem> items = getClosestItems(tile);
		int result;
		
		if(items == null || items.isEmpty())
		{
			result = 1000;
		}
		else
		{
			result =  ai.getZone().getTileDistance(items.get(0).getTile(),tile);
		}
		return result;		
	}
	
}
