package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.CoskunOzdemir;

/**
 * The criteria that will evaluate the tile for bonuses in attack mode while
 * advancing through a non-accessible enemy.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class AvanceItem extends AiUtilityCriterionBoolean
{
	public static final String	NAME				= "AvanceItem";

	/**
	 * Items will be checked in this radius.
	 */
	private final int			ITEM_CHECK_RADIUS	= 2;
	/**
	 * Used to determine the tile's state.
	 */
	private final int			BOMB_NUMBER_LIMIT	= 1;

	public AvanceItem( CoskunOzdemir ai ) throws StopRequestException
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
		boolean itemStatus = false;

		for ( AiTile tt : this.ai.getTo().getAccessibleTilesWithinRadius( ITEM_CHECK_RADIUS ) )
		{
			ai.checkInterruption();
			if ( !tt.getItems().isEmpty() )
			{
				itemStatus = true;
			}

		}

		if ( itemStatus && this.ai.getHero().getBombNumberMax() == BOMB_NUMBER_LIMIT )
		{

			if ( !tile.getItems().isEmpty() )
			{
				result = true;
			}
		}

		return result;
	}
}
