package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**

 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class CriterionTempsCollecte extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "TEMPSCOLLECTE";
	
	/**
	 * Ce critère a but de déterminer le plus proche bonus dans la zone.
	 * Il renvoie la valeur 1 , si le tile contient le plus proche bonus ;
	 * sinon il renvoie 0.
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionTempsCollecte(UnluYildirim ai) throws StopRequestException
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
	{	
		int result = 0;
		
	    AiZone zone =ai.getZone(); //prend la zone. 
		List<AiItem> bonus_ouvert; // pour stocker les bonus ouvert
		List<AiItem> tile_item = null ; // stocker les items dans le tile défini
		List<AiBlock> tile_block=null ; 
		List<AiBlock> zone_block =null;
		
		AiTile tile_hero; // presente le tile de notre hero
		int distance = 0,index=0; 
		double temps =0 ,temps_proche=0;
		
		AiHero ownHero = zone.getOwnHero(); // ownHero prend la valeur de notre Hero.
		tile_item= tile.getItems(); 
		tile_block=tile.getBlocks();
		zone_block=zone.getDestructibleBlocks();
		bonus_ouvert=zone.getItems();// tous les bonus ouverts dans la zone.
		
		
		
		tile_hero = ownHero.getTile(); // determine le tile de notre Hero.
		
		int i;
		if(!(bonus_ouvert.isEmpty())){ // S'il y a pas de bonus ouvert dans la zone
		for(i=0;i<bonus_ouvert.size();i++) // examine tous les bonus ouvert dnas la zone.
		{
			distance= zone.getTileDistance(tile_hero, tile); // la distance entre nutre hero et chaque bonus.
			temps=(float) (distance/(ownHero.getWalkingSpeed())); // le temps d'arrivée pour tous les bonus 
			if (temps < temps_proche) // trouver le bonus plus proche 
			{
				temps_proche=temps;
				index=i; // stocke le place de bonus plus proche 
			}
		}
		
		
		if(tile_item.contains(bonus_ouvert.get(index))) // regarde si le tile contient le bonus plus proche dans la zone
		{
			result=1; 
		}
		
		}
		else { // S'il n'y a pas de bonus ouvert
			int j = 0;
			while(tile.getBlocks().get(j).isDestructible()) {
			 if(zone.getHiddenItemsCount()!=0) //si la zone contient des bonus cachés
			{
				for(i=0;i<zone_block.size();i++)//pour chaque murs destructible dans la zone
				{
					
					distance=zone.getTileDistance(tile_hero,zone_block.get(i).getTile()); //regarde la distace de tous les 
					temps=(float)(distance/(ownHero.getWalkingSpeed())) ; 
					if(temps < temps_proche )
					{
						temps_proche=temps;
						index=i;
						
					}
				}// il trouve le plus proche block
				if(tile_block.contains(zone_block.get(index))) // si le tile contient le plus proche mur destructible
				{
					result=1;
				}
			}
		
		}
			j++;}
		return result;
	}
}
