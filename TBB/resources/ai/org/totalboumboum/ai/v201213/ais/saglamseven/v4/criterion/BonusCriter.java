package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 *
 */
@SuppressWarnings("deprecation")
public class BonusCriter extends AiUtilityCriterionBoolean<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "BONUS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public BonusCriter(SaglamSeven ai) throws StopRequestException
	{	// init nom
		super(ai, NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		if(tile.getItems().size() >0)
			return true;
		return false;
	}
}