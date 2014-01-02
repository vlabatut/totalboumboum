package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.  
 *
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
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
    /** 
     * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas.
     */
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
