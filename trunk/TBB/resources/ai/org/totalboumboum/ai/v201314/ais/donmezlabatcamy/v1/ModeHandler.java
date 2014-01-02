package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 *  Classe gérant les déplacements de l'agent. 
 *
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
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
    /**
     * Détermine si l'agent possède assez d'item.
     */
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		boolean result = true;
		
		return result;
	}
	
	@Override
    /**
     * Détermine si l'agent a la possibilité de ramasser des items dans la zone 
     * courante.
     */
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
