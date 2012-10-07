package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v0;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class BombHandler extends AiBombHandler<CiplakErakyol>
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
	protected BombHandler(CiplakErakyol ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
   	
    	// TODO à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
		// TODO à compléter
	
		return false;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter, si vous voulez afficher quelque chose
	}
}
