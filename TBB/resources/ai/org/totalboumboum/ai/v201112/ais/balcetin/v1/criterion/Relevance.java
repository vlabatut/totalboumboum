package org.totalboumboum.ai.v201112.ais.balcetin.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.balcetin.v1.BalCetin;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class Relevance extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Relevance";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Relevance(BalCetin ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected BalCetin ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	AiZone zone = this.ai.getZone();
	AiHero ownHero = this.zone.getOwnHero();
	List<AiItem> items = zone.getItems();
	
	int i;
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		boolean result = true;

		
		if(ownHero != null)
		{	
			if(ownHero.getBombNumberCurrent() == ownHero.getBombNumberMax())
			{
				for(i=0;i<items.size();i++)
			{
			ai.checkInterruption();
			if(items.get(i).getType() == AiItemType.EXTRA_BOMB)
				result = false;
			
			}
				
			}
			else if(ownHero.getBombRange() >= 6)
			{
				for(i=0;i<items.size();i++)
				{
				ai.checkInterruption();
				if(items.get(i).getType() == AiItemType.EXTRA_FLAME)
					result = false;
				
				}
				
			}
		}
		return result;
	}
}
