package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.CinarYalcin;

/**
 * Controle si bomber cette case bloque des ennemies
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
@SuppressWarnings("deprecation")
public class BlockEnemy extends AiUtilityCriterionBoolean<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "PeutTuer";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public BlockEnemy(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		boolean result = true;
		result=ai.isBlockingEnemy(tile);
		return result;
	}
}
