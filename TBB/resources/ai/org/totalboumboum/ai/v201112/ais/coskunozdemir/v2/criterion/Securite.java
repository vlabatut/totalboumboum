package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.TileOperation;

/**
 * The criteria that will evaluate the tile for its security status.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class Securite extends AiUtilityCriterionBoolean
{
	public static final String	NAME	= "Securite";

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
		TileOperation to = new TileOperation( this.ai );
		if ( to.getCurrentDangerousTiles().contains( tile ) ) return false;

		return true;
	}
}
