package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
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
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		
		AiZone zone = ai.getZone();	
		AiHero myHero = zone.getOwnHero();
		AiTile myTile = myHero.getTile();
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(ai.getZone().getRemainingOpponents());
		
		ai.accessibleTiles();
		ArrayList<AiTile> accessibleTiles=ai.accessibleTiles;
		
		int zoneBomb=zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB);	
		int zoneFlame=zone.getHiddenItemsCount(AiItemType.EXTRA_FLAME);
		
		ArrayList<AiItem> itemVisible = new ArrayList<AiItem>();
		itemVisible.addAll(zone.getItems());

		for(AiItem item: itemVisible){
			ai.checkInterruption();
			if(item.getType()==AiItemType.EXTRA_BOMB){
				zoneBomb++;
			}
			else if(item.getType()==AiItemType.EXTRA_FLAME){
				zoneFlame++;
			}
		}
		
		int bombNumber=myHero.getBombNumberMax()-myHero.getBombNumberCurrent();
		int bombRange=myHero.getBombRange();
		boolean accessibleHero=false ;
		
		if(!heroes.isEmpty()){
			for(AiHero hero : heroes){
				ai.checkInterruption();
				if(accessibleHero==false){
					AiTile heroTile =hero.getTile();
					if(accessibleTiles.contains(heroTile) && ai.getZone().getTileDistance(myTile,heroTile)<=3){
							accessibleHero=true;						
					}
				}	
			}
		}
		
		if((bombNumber>=2 && bombRange>=3 && accessibleHero==true)||(zoneBomb==0&&zoneFlame==0)){
			return true;
		}
		else 
			return false;
		
	}
	
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
	
	AiZone zone = ai.getZone();	
	AiHero myHero = zone.getOwnHero();
	AiTile myTile = myHero.getTile();
	
	ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	heroes.addAll(ai.getZone().getRemainingOpponents());
	
	ai.accessibleTiles();
	ArrayList<AiTile> accessibleTiles=ai.accessibleTiles;
	
	//int bombNumber=myHero.getBombNumberMax()-myHero.getBombNumberCurrent();
	//int bombRange=myHero.getBombRange();
	boolean accessibleHero=false ;
	
	int bombInvisible=zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB);	
	int flameInvisible=zone.getHiddenItemsCount(AiItemType.EXTRA_FLAME);
	
	int flameVisible=0;
	int bombVisible=0;
	
	if(!heroes.isEmpty()){
		for(AiHero hero : heroes){
			ai.checkInterruption();
			if(accessibleHero==false){
				AiTile heroTile =hero.getTile();
				if(accessibleTiles.contains(heroTile) && ai.getZone().getTileDistance(myTile,heroTile)<=3){
							accessibleHero=true;						
				}
			}	
		}
	}
		ArrayList<AiItem> items = new ArrayList<AiItem>();
		items.addAll(zone.getItems());
		
		for(AiItem item: items){
			ai.checkInterruption();
			
			if(item.getType()==AiItemType.EXTRA_BOMB){
				bombVisible++;
			}
			else if(item.getType()==AiItemType.EXTRA_FLAME){
				flameVisible++;
			}
			
		}
		
		if(((bombInvisible+bombVisible>0)||(flameInvisible+flameVisible>0))&&accessibleHero==false)
			return true;
		else
			return false;

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
