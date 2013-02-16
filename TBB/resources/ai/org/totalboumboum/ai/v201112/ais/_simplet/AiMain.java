package org.totalboumboum.ai.v201112.ais._simplet;

import org.totalboumboum.ai.v201112.adapter.agent.AiManager;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * (à ne pas modifier)
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("deprecation")
public class AiMain extends AiManager
{
	/////////////////////////////////////////////////////////////////
	// AGENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ArtificialIntelligence instantiateAgent()
	{	return new Simplet();
	}
}
