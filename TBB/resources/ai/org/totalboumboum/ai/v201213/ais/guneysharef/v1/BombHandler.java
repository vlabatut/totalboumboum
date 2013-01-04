package org.totalboumboum.ai.v201213.ais.guneysharef.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;


/**

 * @author Melis Güney
 * @author Seli Sharef
 */
public class BombHandler extends AiBombHandler<GuneySharef>
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
	protected BombHandler(GuneySharef ai) throws StopRequestException
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
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas
	 */
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
		
	
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
		AiHero h=this.ai.getZone().getOwnHero();
		AiMode mode = this.ai.modeHandler.getMode();
		
		if(!this.ai.getZone().getRemainingOpponents().isEmpty()){
			for(AiHero h2 :this.ai.getZone().getRemainingOpponents()){
				ai.checkInterruption();
				if(this.ai.utilityHandler.selectTiles().contains(h2.getTile())){
					
					break;
				}
			}
			
		if(mode.equals(AiMode.COLLECTING)){
			if(h.getBombNumberCurrent() < h.getBombNumberMax()){

			}
		}
		
		if(mode.equals(AiMode.ATTACKING)){
			if(h.getBombNumberCurrent() < h.getBombNumberMax()){
				
			}
		}
		
		
		
	}
		
	}
}
