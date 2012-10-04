package org.totalboumboum.ai.v201112.ais.gungorkavus.v3.criterion;

import java.util.List;


import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v3.GungorKavus;

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
	 * @param ai
	 * 		?	
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


		if(!itemL.isEmpty()){
			int shortest = zone.getTileDistance(itemL.get(0).getTile(),ownHero.getTile());
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


			if(zone.getTileDistance(tile, sItem.getTile())==0)
			{
				result = true;
			}

		}


		return result;
	}
}
