package org.totalboumboum.ai.v201112.ais.unluyildirim.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class BombHandler extends AiBombHandler<UnluYildirim>
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
	protected BombHandler(UnluYildirim ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
    	
    	// 
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	     
	   AiZone zone;
	   zone=ai.getZone();
	   
	  
		
			return false;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
