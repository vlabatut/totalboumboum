package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class PertinentCollecteForBonus extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "PertinentCollecteForBonus";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected AiTile safeDestination = null;
	/** */
	protected boolean secondaryBombing = false;
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	public PertinentCollecteForBonus(CaliskanGeckalan ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
		
		// init agent
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	boolean result = false;
	int nombreDeBomb = ownHero.getBombNumberCurrent();
	int nombreDeFlamme = ownHero.getBombRange();
	List<AiItem> items = tile.getItems();
	for(int i = 0; i<items.size(); i++) {
		ai.checkInterruption();
		AiItemType itemType = items.get(i).getType();
		if(itemType.equals(AiItemType.EXTRA_BOMB))
			if(nombreDeBomb < 2) {
				result = true;
				break;
			}
		else if(itemType.equals(AiItemType.EXTRA_FLAME))
			if(nombreDeFlamme < 3) {
				result = true;
				break;
			}
	}	
	return result;
	}
}
