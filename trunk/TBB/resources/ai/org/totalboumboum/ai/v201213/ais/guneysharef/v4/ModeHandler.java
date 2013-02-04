package org.totalboumboum.ai.v201213.ais.guneysharef.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
@SuppressWarnings("deprecation")
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
		AiZone z=ai.getZone();
		AiHero h=z.getOwnHero();
		boolean resultat=false;
		if(h.getBombNumberMax()>=2 && h.getBombRange()>=3)
		{
			resultat=true;
		}
    	
		
		return resultat;
	}
	
	@Override
	/**
	 * Détermine si l'agent a la possibilité de ramasser des items
	 */
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		AiZone z=ai.getZone();
		AiHero h=z.getOwnHero();
   		boolean r=false;
   		boolean r2=false;
   		boolean bomb=false;
   		boolean range=false;
		
   		if(h.getBombNumberMax()<2){
   			if(z.getItems().contains(AiItemType.EXTRA_BOMB)){
   				r=true;
   			}
   			bomb=true;
   		}
   		
   		if(h.getBombRange()<3){
   			if(z.getItems().contains(AiItemType.EXTRA_FLAME)){
   				r=true;
   			}
   			range=true;
   		}
   		
   		if(z.getItems().isEmpty()!=true && r==true){
   			r2=true;
   		}
   		
   		if(bomb){
   			if(z.getHiddenItemsCount(AiItemType.EXTRA_BOMB)>0){
   				r2=true;
   			}
   		}
   		
   		if(range){
   			if(z.getHiddenItemsCount(AiItemType.EXTRA_FLAME)>0){
   				r2=true;
   			}
   		}
		return r2;
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
