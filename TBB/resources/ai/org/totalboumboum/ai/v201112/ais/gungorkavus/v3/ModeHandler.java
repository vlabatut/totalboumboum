package org.totalboumboum.ai.v201112.ais.gungorkavus.v3;


import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
public class ModeHandler extends AiModeHandler<GungorKavus>
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
	protected ModeHandler(GungorKavus ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		
		verbose = false;
		
		
    }

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
	
	
	
	boolean resultat = false ;
	
		int myBombNbr = ai.getZone().getOwnHero().getBombNumberMax();
		int myBombRng = ai.getZone().getOwnHero().getBombRange(); 
  //  	int ourAverage = 50*myBombNbr + 70*myBombRng;
		
    	List<AiHero> heroL = ai.getZone().getRemainingHeroes();
    	
    	int heroBombNbr = 0;
    	int heroBombRng = 0;
   // 	int average = 0;
    	
    	for(int i=0;i<heroL.size();i++){
    		ai.checkInterruption();
    		heroBombNbr += heroL.get(i).getBombNumberMax();
    		heroBombRng += heroL.get(i).getBombRange();
    	}
    //		average = (50*heroBombNbr + 70*heroBombRng)/heroL.size();
    	
		if((myBombNbr>=2 && myBombRng>=3)/*||(ourAverage>=average)*/){
			resultat = true;
		}
		
		return resultat;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
		boolean resultat = false ;
	
   		int itemLS = ai.getZone().getItems().size();
		int hItemLS = ai.getZone().getHiddenItemsCount();
		if(ai.getZone().getTotalTime()>=ai.getZone().getLimitTime()/5*4)
			resultat=false;
		else if(itemLS>0 || hItemLS>0){
			resultat = true;
		}
		return resultat;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
