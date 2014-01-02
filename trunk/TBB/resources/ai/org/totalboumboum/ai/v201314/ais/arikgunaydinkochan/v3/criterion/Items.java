package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.Agent;

/**
 * Cette classe est un critère menace
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Items extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Items";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Items(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();

		AiZone zone = ai.getZone();	

		for (AiItem item : zone.getItems()) {
			ai.checkInterruption();
			AiItemType type=item.getType();
			if (type.equals(AiItemType.EXTRA_SPEED)||type.equals(AiItemType.GOLDEN_SPEED)){
				
				if(item.getTile().equals(tile)){
					return true;
				}
				else 
					return false;
			}
			else if(type.equals(AiItemType.EXTRA_BOMB)||type.equals(AiItemType.GOLDEN_BOMB)){
				
				if(item.getTile().equals(tile)){
					return true;
				}
				else 
					return false;
			}
			else if(type.equals(AiItemType.EXTRA_FLAME)||type.equals(AiItemType.GOLDEN_FLAME)){
				
				if(item.getTile().equals(tile)){
					return true;
				}
				else 
					return false;
			}
		}
		return false;

	}
}
