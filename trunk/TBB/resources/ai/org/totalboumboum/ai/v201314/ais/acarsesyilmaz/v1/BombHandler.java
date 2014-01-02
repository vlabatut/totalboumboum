package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
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
    {	
		super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false; 	
    }

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas. 
	 * Cette décision dépend, entre autres, des valeurs de préférence courantes, 
	 * et éventuellement d'autres informations.
	 * La méthode renvoie true si l'agent doit poser une bombe, et false sinon.
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 */
	@Override
	protected boolean considerBombing()
	{	
		ai.checkInterruption();
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
	{	
		ai.checkInterruption();
	}
}
