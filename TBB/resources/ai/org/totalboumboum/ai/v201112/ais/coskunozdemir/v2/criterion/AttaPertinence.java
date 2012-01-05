package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.TileOperation;

/**
 * The criteria that will evaluate the tile for attack suitability.
 * 
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class AttaPertinence extends AiUtilityCriterionBoolean
{
	public static final String	NAME			= "AttaPertinence";

	/**
	 * Range - CLOSING_LIMIT determines the tile's state.
	 */
	public static final int		CLOSING_LIMIT	= 3;

	public AttaPertinence( CoskunOzdemir ai ) throws StopRequestException
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
		boolean result = false;

		TileOperation to = new TileOperation( ai );
		for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();

			if ( to.getDangerousTilesOnBombPut( tile, this.ai.getZone().getOwnHero().getBombRange() - CLOSING_LIMIT ).contains( currentEnemy.getTile() ) ) result = true;
		}
		return result;
	}
}
