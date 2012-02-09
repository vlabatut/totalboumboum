package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.CoskunOzdemir;

/**
 * The criteria that will evaluate the tile for collect suitability.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class ColPertinence extends AiUtilityCriterionBoolean
{
	public static final String	NAME	= "ColPertinence";

	public ColPertinence( CoskunOzdemir ai ) throws StopRequestException
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
		int bombNumber = this.ai.getHero().getBombNumberMax();
		int fireRange = this.ai.getHero().getBombRange();

		if ( tile.getItems().contains( AiItemType.EXTRA_BOMB ) )
		{
			if ( bombNumber < fireRange )
			{
				result = true;
			}
			else
				result = false;
		}

		if ( tile.getItems().contains( AiItemType.EXTRA_FLAME ) )
		{
			if ( bombNumber > fireRange )
			{
				result = true;
			}
			else
				result = false;
		}

		return result;
	}
}
