package org.totalboumboum.ai.v201314.ais.enginserhantipici.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;


/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
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
		boolean result = false;
		
		if(ai.isPatternExist() && 
				ai.getZone().getOwnHero().getBombNumberMax() >= 3 &&
				ai.moveHandler.getCurrentPath() != null &&
				ai.preferenceHandler.selectedTileType == 2){
				//ai.preferenceHandler.selectedTileType holds the number of strategy
				//in this version(v2), as we didn't send any other attack strategies, it's kind of unnecessary 
				//when there will be other strategies, we are going to use this variable to distinguish their needs from the other ones 
			result = true;
		}
		
		if(ai.preferenceHandler.selectedTileType == 1){
			//security category, no items or anything needed
			result = true;
		}
		
		
		return result;
	}
	
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
	
	boolean result = false;
	
	 boolean tempResult = false;
	
	AiZone zone = ai.getZone();
	
	for(AiTile tile : zone.getTiles()){
		ai.checkInterruption();
			if(zone.getTileDistance(zone.getOwnHero().getTile(), tile) < 3){
				if(!tile.getItems().isEmpty()){
					tempResult = true;
					break;
				}
			}
		}
	
	if(!tempResult){
		if(zone.getDestructibleBlocks().size() > 3){
		float d;
		d = zone.getHiddenItemsCount() / zone.getDestructibleBlocks().size(); 
		
			if(d < 0.3){
				tempResult = false;
			}else{
				tempResult = true;
			}
		}
	}
	result = tempResult;
		
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
