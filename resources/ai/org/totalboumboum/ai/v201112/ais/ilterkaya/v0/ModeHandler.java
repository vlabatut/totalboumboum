package org.totalboumboum.ai.v201112.ais.ilterkaya.v0;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Mustafa Kaan İlter
 * @author Önder Kaya
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<IlterKaya>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(IlterKaya ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	// à compléter
		
		return true;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		// à compléter
		
		return true;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException 
	 * 		description manquante !
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// à compléter, si vous voulez afficher quelque chose
	}
}
