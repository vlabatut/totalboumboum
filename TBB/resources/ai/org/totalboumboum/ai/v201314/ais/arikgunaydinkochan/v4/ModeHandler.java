package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.

 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();	
	}
	
	/**	Limit 
	 *  */
	private final double SPEED_LIMIT = 180;
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		
		AiZone zone = ai.getZone();	
		AiHero myHero = zone.getOwnHero();
		
		//
		ai.accessibleTile();
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(ai.getZone().getRemainingOpponents());
		
		//
		int heroNumber = heroes.size();					//
		int mybombs = myHero.getBombNumberMax();		//
		int mybombsRange = myHero.getBombRange();		//
		
		double mySpeed = myHero.getWalkingSpeed();		//
		long temps = zone.getLimitTime()-zone.getTotalTime(); //
			
		if(temps<=45000 && heroNumber==1){
			return true;
		}
		else if((ai.dangereousHero())
				||((mybombs>=3 && mybombsRange>=4 && mySpeed>SPEED_LIMIT) && ai.accessibleHero()==true)){ 
			return true;
		}
		else 				
			return false; 
	}
	
	@Override
	protected boolean isCollectPossible()
	{	
	ai.checkInterruption();	
	
	ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	heroes.addAll(ai.getZone().getRemainingOpponents());
	
	ArrayList<AiTile> accessibleTile=ai.accessibleTiles;
	
	if(accessibleTile.size()<=5 && ai.accessibleHero())	
			return false;		

	AiZone zone = ai.getZone();	
	AiHero myHero = zone.getOwnHero();
	
	boolean result=false ;
	//
	if (zone.getHiddenItemsCount() > 0 || zone.getItems().size() > 0){
		
		//
		int mybombs 	 = myHero.getBombNumberMax();
		int mybombsRange = myHero.getBombRange();
		double mySpeed 	 = myHero.getWalkingSpeed();		
		
		boolean needOfExtraBomb  = (mybombs < 3);
		boolean needOfExtraFlame = (mybombsRange < 4);
		boolean needOfExtraSpeed = (mySpeed < SPEED_LIMIT);
		
		boolean getBomb  = false;
		boolean getFlame = false;
		boolean getSpeed = false;
		//
		if(needOfExtraBomb){
			if(ai.itemVisible(AiItemType.EXTRA_BOMB)||ai.itemVisible(AiItemType.GOLDEN_BOMB)){
				getBomb=true;
				
			}
			else if (ai.itemHidden(AiItemType.EXTRA_BOMB)||ai.itemHidden(AiItemType.GOLDEN_BOMB)){
				getBomb=true;
			}
		}
		if(needOfExtraFlame){
			if(ai.itemVisible(AiItemType.EXTRA_FLAME)||ai.itemVisible(AiItemType.GOLDEN_FLAME)){
				getFlame=true;
			}
			else if (ai.itemHidden(AiItemType.EXTRA_FLAME)||ai.itemHidden(AiItemType.GOLDEN_FLAME)){
				getFlame=true;
			}
			
		}
		if(needOfExtraSpeed){
			if(ai.itemVisible(AiItemType.EXTRA_SPEED)||ai.itemVisible(AiItemType.GOLDEN_SPEED)){
				getSpeed=true;
			}
			else if (ai.itemHidden(AiItemType.EXTRA_SPEED)||ai.itemHidden(AiItemType.GOLDEN_SPEED)){
				getSpeed=true;
			}		
		}	
		
		if (getBomb||getFlame||getSpeed)
			result = true;
	}
	return result;
	
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		
		
	}
}
