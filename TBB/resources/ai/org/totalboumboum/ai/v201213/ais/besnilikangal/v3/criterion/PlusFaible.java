package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette critere a été utilisé pour evaluer les agents qui sont plus puissant
 * que. On ne considere que le nombre de bombe que les agents possedent. On va
 * considerer les autres items aussi. Quand l'API contient ces informations.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class PlusFaible extends AiUtilityCriterionBoolean<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "PlusFaible";

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PlusFaible( BesniliKangal ai ) throws StopRequestException
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
		for ( AiHero hero : tile.getHeroes() )
		{
			ai.checkInterruption();
			if ( ai.ownHero.getBombNumberMax() >= hero.getBombNumberMax() )
				return true;
		}
		return false;
	}
}
