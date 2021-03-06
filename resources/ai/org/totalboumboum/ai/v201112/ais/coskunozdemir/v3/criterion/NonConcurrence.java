package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v3.CoskunOzdemir;

/**
 * The criteria that will evaluate the tile for the remaining heroes' distances
 * to it.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class NonConcurrence extends AiUtilityCriterionBoolean
{	/** */
	public static final String	NAME	= "NonConcurrence";

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public NonConcurrence( CoskunOzdemir ai ) throws StopRequestException
	{
		super( NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** */
	protected CoskunOzdemir	ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile myTile = this.ai.getHero().getTile();
		int myDistance = this.ai.getZone().getTileDistance( myTile, tile );

		for ( AiHero currentEnemy : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( this.ai.getZone().getTileDistance( currentEnemy.getTile(), tile ) < myDistance ) return false;
		}
		return true;
	}
}
