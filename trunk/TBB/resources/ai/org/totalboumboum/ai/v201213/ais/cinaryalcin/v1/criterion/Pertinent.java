package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.CinarYalcin;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class Pertinent extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";
	/** */
	public static final int BOMB_NUMBER = 2;
	/** */
	public static final int RANGE_NUMBER = 3;
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Pertinent (CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiHero fahri = this.ai.getZone().getOwnHero();
		boolean result = false;

		if (fahri.getBombNumberMax() < BOMB_NUMBER) {
			if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
				result = true;
			}
		}
		if (fahri.getBombRange() < RANGE_NUMBER) {
			if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
				result = true;
			}
		}
		if (fahri.getWalkingSpeed() < 1000) {
			if (tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
				result = true;
			}
		}
		
		return result;
	}
}
