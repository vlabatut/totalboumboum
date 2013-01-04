package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4;

import java.util.ArrayList;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * Classe gÃ©rant les dÃ©placements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de dÃ©tails.
 * 
 * 
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
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
	}
	

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
				
		boolean result = false; 
		
		int myBombNumber = this.ai.getZone().getOwnHero().getBombNumberMax();
		int myBombRang = this.ai.getZone().getOwnHero().getBombRange();
		double mySpeed = this.ai.getZone().getOwnHero().getWalkingSpeed();
		
		int i=0;
		int ortalamaBomba=0;
		int ortalamaRange=0;
		double ortalamaSpeed=0.0;
		int t = 0;
		if(ai.getZone().getTotalTime()*5<2*ai.getZone().getLimitTime())
		{
			if(!this.ai.getZone().getRemainingOpponents().isEmpty())
			{
				for(AiHero hero:this.ai.getZone().getRemainingOpponents())
				{
					ai.checkInterruption();
					ortalamaBomba += hero.getBombNumberCurrent();
					ortalamaRange += hero.getBombRange();
					ortalamaSpeed += hero.getWalkingSpeed();
					t++;
				}
			}
			
			// 0/0 iÅŸlemini engellemek iÃ§in.
			//dÃ¼ÅŸmanlarÄ±n ortalamasÄ± bizden fazlaysa bizde yeterli item yok demektir.	
				if(t!=0){
				if(((ortalamaBomba/t)<myBombNumber)&&((ortalamaRange/t)<myBombRang)&&((ortalamaSpeed/(double)t)<mySpeed))
					result=true;
				}
				
				AiZone zone=this.ai.getZone();
				AiTile mytile=zone.getOwnHero().getTile();
				ArrayList<AiTile> tile_list = new ArrayList<AiTile>();
				for(i=0;i<4;i++)
				{
					ai.checkInterruption();
					for(int j=0;j<4;j++)
					{	
						ai.checkInterruption();
						if((mytile.getCol()+j<zone.getWidth())&&(mytile.getRow()+i<zone.getHeight()))
						tile_list.add(zone.getTile(mytile.getRow()+i,mytile.getCol()+j));
						
						if((mytile.getCol()-j>0)&&(mytile.getRow()-i>0))
						tile_list.add(zone.getTile(mytile.getRow()-i,mytile.getCol()-j));
					}
				}
				if(!zone.getRemainingOpponents().isEmpty())
				{
					for(AiHero hero:zone.getRemainingOpponents())
					{
						ai.checkInterruption();
						if(tile_list.contains(hero.getTile()))
							result=true;
					}
				}
		}
		else result=true;
		
	
			
		return result;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		boolean result=false;
		int itemVisible=0;
   		int hiddenItem=ai.getZone().getHiddenItemsCount();
   		for (AiItem aiItem : ai.getZone().getItems()) {
   			ai.checkInterruption();
   			if(ai.Bonus.contains(aiItem)||ai.SBonus.contains(aiItem))
   			{
   				itemVisible++;
   			}
   			
			
		}
   		
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
		
	}
}

