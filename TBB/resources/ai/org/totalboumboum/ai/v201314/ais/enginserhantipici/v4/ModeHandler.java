package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;


/**
 * Class which manage the displacement of the agent 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
	 * construct a handler for agent to pass a parameter
	 * 
	 * @param ai	
	 * 		the agent who is managed by this class
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
				/*ai.moveHandler.getCurrentPath() != null && */
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
		if(ai.preferenceHandler.selectedTileType == 4 
				&& ai.getZone().getOwnHero().getBombNumberMax() >= 1
				&& ai.getZone().getOwnHero().getBombRange() >= 2){
			//
			result = true;
		}
		if(ai.preferenceHandler.selectedTileType == 6){
			//category "SEARCH"
			result = true;
		}
		
		if(result) result = true;
		
		return result = true;
	}
	
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
	
	boolean result;
	
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
	if(!result){
		result = false;
	}
		
	return result = false;
	}	


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * graphic update
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		
	}
}
