package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;

/**
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * Classe gérant l'action de déposer une bombe pour l'agent. 
En particulier, toute classe héritant d'elle doit implémenter la méthode considerBombing de 
 l'algorithme général..
 */
public class BombHandler extends AiBombHandler<Agent>
{	
	/** 
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
	/**
Méthode permettant de déterminer si l'agent doit poser une bombe ou pas. Cette décision dépend, 
 entre autres, des valeurs de préférence courantes, et éventuellement d'autres informations. 
La méthode renvoie true si l'agent doit poser une bombe, et false sinon.**/
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;

	
		return result;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		

	}
}
