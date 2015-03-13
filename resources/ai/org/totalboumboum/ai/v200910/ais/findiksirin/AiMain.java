package org.totalboumboum.ai.v200910.ais.findiksirin;

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.ais.findiksirin.v5c.FindikSirin;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("deprecation")
public class AiMain extends AiManager
{
	/////////////////////////////////////////////////////////////////
	// AGENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ArtificialIntelligence instantiateAgent()
	{	return new FindikSirin();
	}
}
