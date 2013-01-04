package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1;

import java.util.List;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * Classe gÃ©rant les dÃ©placements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de dÃ©tails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Bekir CÄ±nar
 * @author Deniz YalÃ§Ä±n
 */
public class ModeHandler extends AiModeHandler<CinarYalcin>
{	
	/**
	 * Construit un gestionnaire pour l'agent passÃ© en paramÃ¨tre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gÃ©rer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas oÃ¹ le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(CinarYalcin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on rÃ¨gle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// TODO Ã  complÃ©ter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
				
		boolean result = false; 
		
		int myBombNumber = ai.getZone().getOwnHero().getBombNumberMax();
		int myBombRang = ai.getZone().getOwnHero().getBombRange();
		double mySpeed = ai.getZone().getOwnHero().getWalkingSpeed();
		
		List<AiHero> HeroList=ai.getZone().getRemainingOpponents();
		int i=0;
		int ortalamaBomba=0;
		int ortalamaRange=0;
		double ortalamaSpeed=0.0;
		int t = HeroList.size();
		if(t<2)t=0; // 2 tane dÃ¼ÅŸman kaldÄ±ÄŸÄ±nda ortalamaya bakmasÄ±n dalsÄ±n.
		while( i < t)
		{
			ortalamaBomba += HeroList.get(i).getBombNumberMax();
			ortalamaRange += HeroList.get(i).getBombRange();
			ortalamaSpeed += HeroList.get(i).getWalkingSpeed();
			i++;
		}
		t=HeroList.size(); 
	// 0/0 iÅŸlemini engellemek iÃ§in.
	//dÃ¼ÅŸmanlarÄ±n ortalamasÄ± bizden fazlaysa bizde yeterli item yok demektir.	
		if((myBombNumber>=2 && myBombRang>=3)&&(((ortalamaBomba/t)<myBombNumber)&&((ortalamaRange/t)<myBombRang)&&((ortalamaSpeed/(double)t)<mySpeed)))
			result=true;
			
		return result;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		boolean result=false;
		
   		int hiddenItem=ai.getZone().getHiddenItemsCount();
   		int itemVisible=ai.getZone().getItems().size();
   		
   		if((ai.getZone().getTotalTime())>=(4*(ai.getZone().getLimitTime())/5))
   			result=false;
   		else if(hiddenItem>0||itemVisible>0)
   			result=true;
   		
		return result;
	}
	
	//Burada eÄŸer 3*3 te dÃ¼ÅŸman var ise modumuzun atak moduna dÃ¶ndÃ¼rmemiz gerekiyor.
	//Onun iÃ§in buraya bir method yazmamÄ±z lazÄ±m

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met Ã  jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas oÃ¹ le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO Ã  complÃ©ter, si vous voulez afficher quelque chose
	}
}

