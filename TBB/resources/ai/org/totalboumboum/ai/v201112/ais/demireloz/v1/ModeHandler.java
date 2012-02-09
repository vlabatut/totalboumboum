package org.totalboumboum.ai.v201112.ais.demireloz.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<DemirelOz>
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
	protected ModeHandler(DemirelOz ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		verbose = false;
		

	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems()  throws StopRequestException
	{	ai.checkInterruption();
		
    	if(this.ai.getZone().getOwnHero().getBombNumberMax() >=2 && this.ai.getZone().getOwnHero().getBombRange() >=3 )
    	{
    		return true;
    	}
    	else 	return false;
    	
		
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
     if(this.ai.getZone().getHiddenItemsCount() >0 || this.ai.getZone().getItems().isEmpty()==false)
     {
    	 return true;
     }

 	else return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
