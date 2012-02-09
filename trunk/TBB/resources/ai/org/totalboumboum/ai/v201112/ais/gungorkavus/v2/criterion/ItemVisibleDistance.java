package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import java.util.List;


import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class ItemVisibleDistance extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemVisibleDistance(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
	public int distance (int d,int e,int f,int g) throws StopRequestException{
		ai.checkInterruption();
		
		int resultat = Math.abs(d-f)+Math.abs(e-g);
		
		return resultat;
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
	
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		
		List<AiItem> itemL = zone.getItems();
	
		
		if(itemL.size()>0){
		double shortest = zone.getTileDistance(itemL.get(0).getTile(),ownHero.getTile());
		AiItem sItem = itemL.get(0);
		AiItem dItem = null;
		for(int i = 0;i<itemL.size()-1;i++)
		{
			ai.checkInterruption();
			
			if(shortest > zone.getTileDistance(itemL.get(i+1).getTile(), ownHero.getTile()))
			{
				shortest =  zone.getTileDistance(itemL.get(i+1).getTile(), ownHero.getTile());
				sItem = itemL.get(i+1);
				
			}
			
		}	
			
		
		for(int i = 0;i<itemL.size()-1;i++)
		{
			ai.checkInterruption();
			
			if(shortest == zone.getTileDistance(itemL.get(i+1).getTile(), ownHero.getTile()))
			{
				shortest =  zone.getTileDistance(itemL.get(i+1).getTile(), ownHero.getTile());
				dItem = itemL.get(i+1);
				
				if(zone.getTileDistance(tile, dItem.getTile())==0)
				{
					result = true;
				}
			}
		}
		
		
		if(distance(tile.getCol(), tile.getRow(), sItem.getCol(), sItem.getRow())==0)
		{
			result = true;
		}
		
		
		
		
		
		}
		
	
		return result;
	}
}
