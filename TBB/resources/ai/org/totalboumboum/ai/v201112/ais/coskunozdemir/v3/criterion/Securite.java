package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.CoskunOzdemir;

/**
 * The criteria that will evaluate the tile for its security status.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class Securite extends AiUtilityCriterionBoolean
{	/** */
	public static final String	NAME	= "Securite";
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public Securite( CoskunOzdemir ai ) throws StopRequestException
	{
		super( NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected CoskunOzdemir	ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( this.ai.getTo().getCurrentDangerousTiles().contains( tile ) ) return false;

		return true;
	}
}
