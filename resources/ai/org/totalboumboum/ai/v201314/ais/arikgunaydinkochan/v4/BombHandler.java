package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai)
    {	super(ai);
    	ai.checkInterruption();
   	
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;
		
		AiZone zone=  ai.getZone();
		AiHero myHero=zone.getOwnHero();
		AiTile myTile=myHero.getTile();
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		// 
		Map<AiTile,Integer> zonePreference=ai.getPreferenceHandler().getPreferencesByTile();
		int value=zonePreference.get(myTile);
		// 
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);	
		// 
		int bombsValue = myHero.getBombNumberMax()-myHero.getBombNumberCurrent();
		int bombsValuePoser=myHero.getBombNumberCurrent();
		
		ArrayList<AiBomb> mybombs = new ArrayList<AiBomb>();
		mybombs.addAll(zone.getBombsByColor(PredefinedColor.GREY));
		
		if(ai.simulationSecuriteZone() 
				&& bombsValue>0 
				&& myTile.getBombs().isEmpty()){
				//
				if(mode==AiMode.ATTACKING){
						
						if(!ai.accessibleHero()){
							if(ai.currentPath!=null && ai.currentPath.getLength()>=2)
								for(AiBlock block : ai.currentPath.getLocation(1).getTile().getBlocks()){
									ai.checkInterruption();
									if(block.isDestructible()){
										return true;
									}
								}
						}
						else {
							for(AiHero hero :zone.getRemainingOpponents()){
								ai.checkInterruption();
								for(AiTile tile :hero.getTile().getNeighbors()){
									ai.checkInterruption();
									if(myTile==tile && value<2){
										return true;
									}
								}
							}
						}
						if(!mybombs.isEmpty() && bombsValuePoser<3&&value<2){
								result=true;
						}
						else if(value==0&&bombsValuePoser<3){
								result=true;
						}
						else 	result=false;
								
				}
				//
				else 
						if(bombsValue>0 && bombsValuePoser<3){
								if(tiles.contains(myTile))
										if(value>=48 && value<=59)
												result=true;
								}
		}
		else result=false;
		
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
