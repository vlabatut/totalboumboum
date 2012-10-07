package org.totalboumboum.ai.v201112.ais.kayukataskin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<KayukaTaskin>
{	
	/** */
	private AiZone zone;
	/** */
	private AiHero ownHero;
	
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

    }	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
		int range= ownHero.getBombRange();
		int bomb= ownHero.getBombNumberMax();
    	
		if(range>2 && bomb>2){
			return true;
		}
		else
			return false;
		
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		boolean attaque=ai.modeHandler.hasEnoughItems();
   		
   		if(attaque){
   			return false;
   		}
   		else{
   			if(!zone.getItems().isEmpty() || zone.getHiddenItemsCount()!=0){
   				if(ownHero.getBombNumberMax()<3 && zone.getItems().contains(AiItemType.EXTRA_BOMB)){
   				return true;	
   				}
   			
   	   			if(ownHero.getBombRange()<3 && zone.getItems().contains(AiItemType.EXTRA_FLAME)){
   	   			return true;	
   	   			}
   			}
   			
		return false;
   		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
