package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;




import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */


public class CriterSecure extends AiUtilityCriterionBoolean<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "SECURITE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterSecure(SaglamSeven ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai1 = ai;
	}
	
	
	/** */
	protected SaglamSeven ai1;
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai1.checkInterruption();
	
		if ( this.ai1.getCurrentDangerousTiles().contains(tile)){
			return false;
		}
	
		return true;
	}
	

	
}