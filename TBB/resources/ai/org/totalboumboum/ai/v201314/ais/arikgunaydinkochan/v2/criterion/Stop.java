package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.Agent;

/**
 * Cette classe est critère attack.
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Stop extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Stop";
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Stop(Agent ai)
	{	super(ai,NAME,0,1);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	
	ai.checkInterruption();
	
	
	
	return 0;
	}
}
