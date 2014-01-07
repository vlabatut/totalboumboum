package org.totalboumboum.ai.v201314.ais.cetinozel.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;

/**
 * C'est la class de notre BombHandler
 * 
 * @author Hakan Çetin
 * @author Yiğit Özel
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai)
    {	super(ai);
    	ai.checkInterruption();
		

	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;
	

	
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
