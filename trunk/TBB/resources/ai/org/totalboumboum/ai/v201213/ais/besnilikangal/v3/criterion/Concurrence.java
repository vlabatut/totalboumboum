package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette critere est utilisé pour determiner la concurrence qui peut exister
 * entre un des agents et notre agent. On considere la distance pour calculer la
 * valeur de cette critere.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class Concurrence extends AiUtilityCriterionBoolean<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "Concurrence";

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Concurrence( BesniliKangal ai ) throws StopRequestException
	{
		super( ai, NAME );
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		int distance = ai.getZone().getTileDistance( ai.ownHero.getTile(), tile );
		for ( AiHero ennemy : ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( ai.getZone().getTileDistance( ennemy.getTile(), tile ) < distance )
				return true;
		}
		return false;
	}
}
