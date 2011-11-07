package org.totalboumboum.ai.v201112.ais.kayukataskin.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
public class ModeHandler extends AiModeHandler<KayukaTaskin>
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
	protected ModeHandler(KayukaTaskin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// TODO à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	// TODO à compléter
		
		return true;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		// TODO à compléter
		
		return true;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter, si vous voulez afficher quelque chose
	}
}
