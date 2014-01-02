package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * Classe gérant le mode de l'agent. Elle implémente la méthode update, utilisée pour mettre le mode à 
 jour, et qui ne peut pas être modifiée ni surchargée. Cette méthode implémente l'algorithme de 
 sélection du mode défini en cours, qui est imposé. 
Elle fait appel aux méthodes hasEnoughItems et isCollectPossible(), qui, elles, doivent être surchargées. 
Enfin, cette classe stocke le mode courant grâce au champ mode.
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
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
	/**
	 * Détermine si l'agent possède assez d'items, ou bien s'il doit essayer d'en ramasser d'autres. Cette 
 distinction est relative à l'environnement, à l'agent lui-même et à la stratégie qu'il utilise. 
Cette méthode est utilisée lors de la mise à jour du mode par update.
	 */
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		boolean result = true;
		
		return result;
	}
	
	@Override
	/**
	 * Détermine si l'agent a la possibilité de ramasser des items dans la zone courante : présence d'items 
 cachés ou découverts, assez de temps restant, etc. 
Cette méthode est utilisée lors de la mise à jour du mode par update.
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
