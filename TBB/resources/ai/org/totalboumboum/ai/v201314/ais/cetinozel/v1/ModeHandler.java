package org.totalboumboum.ai.v201314.ais.cetinozel.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 * C'est la class de notre ModeHandler
 * 
 * @author Hakan Çetin
 * @author Yiğit Özel
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
		

	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		boolean result = true;
		

		
		return result;
	}
	
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
		boolean result = true;
	

		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		

	}
}
