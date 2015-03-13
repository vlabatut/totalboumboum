package org.totalboumboum.ai.v201213.ais.besnilikangal.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.besnilikangal.v3.BesniliKangal;

/**
 * Cette critere a été utilisé pour qu'on puisse détecter les agents qui se
 * trouvent dans une case qui est dans l'un des bombes portées.
 * 
 * @author Doruk Kangal
 * @author Mustafa Besnili
 */
@SuppressWarnings("deprecation")
public class Easier extends AiUtilityCriterionBoolean<BesniliKangal>
{
	/** Nom de ce critère */
	public static final String NAME = "PlusFacile";

	/**
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Easier( BesniliKangal ai ) throws StopRequestException
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
		List<AiHero> ennemies = (List<AiHero>) tile.getHeroes();
		for ( AiHero ennemy : ennemies )
		{
			ai.checkInterruption();
			return ai.heroOperation.isEnemyInDanger( ennemy );
		}
		return false;
	}
}
