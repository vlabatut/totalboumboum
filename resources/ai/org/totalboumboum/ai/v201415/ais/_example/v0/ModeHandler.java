package org.totalboumboum.ai.v201415.ais._example.v0;

import org.totalboumboum.ai.v201415.adapter.agent.AiModeHandler;

/**
 * Classe gérant le mode courant de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Xxxxxx
 * @author Yyyyyy
 * @author Zzzzzz
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
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		boolean result = true;
		
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
		return result;
	}
	
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
		boolean result = true;
	
		/*
		 *  TODO à compléter.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
		
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
		
		/*
		 *  TODO à compléter, si vous voulez afficher quelque chose.
		 *  
		 *  Remarque : ce commentaire est à effacer, comme tous les autres marqueurs TODO
		 */
	}
}
