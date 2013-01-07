package org.totalboumboum.ai.v201112.ais.ilterkaya.v0;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Mustafa Kaan İlter
 * @author Önder Kaya
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<IlterKaya>
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
	protected BombHandler(IlterKaya ai) throws StopRequestException
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
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
		// à compléter
	
		return false;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// à compléter, si vous voulez afficher quelque chose
	}
}
