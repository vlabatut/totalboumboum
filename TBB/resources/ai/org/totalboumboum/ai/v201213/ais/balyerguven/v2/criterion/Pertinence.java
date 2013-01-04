package org.totalboumboum.ai.v201213.ais.balyerguven.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.balyerguven.v2.BalyerGuven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 */
public class Pertinence extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "Pertinence";
	/** */
	public static final int NBR_BOMBE = 2;
	/** */
	public static final int NBR_PORTE = 2;

	
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Pertinence(BalyerGuven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiHero myHero = this.ai.getZone().getOwnHero();
		boolean result = false;

		if (myHero.getBombNumberMax() < NBR_BOMBE) {
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			result = true;
		}
		}
		
		if (myHero.getBombRange() < NBR_PORTE) {
		if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			result = true;
		}
		}
		return result;
	}
}
