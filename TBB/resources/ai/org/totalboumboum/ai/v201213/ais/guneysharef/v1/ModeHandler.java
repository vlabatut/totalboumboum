package org.totalboumboum.ai.v201213.ais.guneysharef.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class ModeHandler extends AiModeHandler<GuneySharef>
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
	protected ModeHandler(GuneySharef ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Détermine si l'agent possède assez d'items
	 */
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	
		
		return true;
	}
	
	@Override
	/**
	 * Détermine si l'agent a la possibilité de ramasser des items
	 */
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		
		
		return true;
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
		
	
	}
}
