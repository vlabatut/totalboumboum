package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;

/**
 * Classe gérant les déplacements de l'agent. 
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
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
	 * Détermine si l'agent possède assez d'items, ou bien s'il doit essayer d'en ramasser 
	 * d'autres. Cette distinction est relative à l'environnement, à l'agent lui-même et à 
	 * la stratégie qu'il utilise.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent possède assez d'items.
	 */
	@Override
	protected boolean hasEnoughItems()
	{	
		ai.checkInterruption();
		boolean result = true;

		return result;
	}

	/**
	 * Détermine si l'agent a la possibilité de ramasser des items dans la zone courante : 
	 * présence d'items cachés ou découverts, assez de temps restant, etc.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent a la possibilité de ramasser des items.
	 */
	@Override
	protected boolean isCollectPossible()
	{	
		ai.checkInterruption();
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
	{	
		ai.checkInterruption();
	}
}
