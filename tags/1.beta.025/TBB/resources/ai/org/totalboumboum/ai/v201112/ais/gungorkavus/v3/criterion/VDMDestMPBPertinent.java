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
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class VDMDestMPBPertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		?	
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestMPBPertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	

		ai.checkInterruption();
		boolean result = false;

		AiZone zone = ai.getZone();
		List<AiItem> item = zone.getItems();
		AiHero ownHero = zone.getOwnHero();

		if(item.isEmpty()){
			result = true;
		}else
		{
			int shortest = zone.getTileDistance(item.get(0).getTile(), ownHero.getTile()) ;

			for(int i = 0;i<item.size()-1;i++){
				ai.checkInterruption();
				if(shortest > zone.getTileDistance(item.get(i+1).getTile(), ownHero.getTile()))
				{
					shortest = zone.getTileDistance(item.get(i+1).getTile(), ownHero.getTile());
				}
			}

			if(shortest>3)
			{
				result=true;

			}
		}




		return result;
	}
}