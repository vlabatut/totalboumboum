package org.totalboumboum.ai.v201213.ais.balyerguven.v3;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
/**
 * our bomb handler class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 *
 */
public class BombHandler extends AiBombHandler<BalyerGuven>
{	
 
	/**
	 * represents tile of our hero
	 */
	public AiTile startTile=ai.getZone().getOwnHero().getTile();
	/** represents zone*/
	protected AiZone zone;
	/** represents our hero*/
	protected AiHero myHero;
	/** represents  our tile*/
	protected AiTile ownTile;

	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 *             
	 *  
	 */

	protected BombHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
    	
	}

	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/** method of escaping
	 * @param tile 
	 * @return boolean
	 * @throws StopRequestException
	 * 			If the engine demands the termination of the agent.
	 */
	public boolean canRun(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		zone = ai.getZone();
		myHero=zone.getOwnHero();
		AiFire fire = myHero.getBombPrototype().getFirePrototype();
		int range = myHero.getBombRange();
		List<AiTile> posibleTile = clearDuplicate(ai.utilityHandler.possibleTiles);

		for(Direction d: Direction.getPrimaryValues())
		{	ai.checkInterruption();
			AiTile neighbor = tile;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		for(int i = 0 ; i<posibleTile.size();i++){
			ai.checkInterruption();
			if(result.contains(posibleTile.get(i)) || tile.equals(posibleTile.get(i))){
				posibleTile.remove(i);
				i--;
			}
		}
		
		
		if(posibleTile.size()>0){
			return true;
		}
		
		return false;
	}
	
	/**method for clear duplicate
	 * @param tiles
	 * @return List tiles
	 * @throws StopRequestException
	 * 			If the engine demands the termination of the agent.
	 */
	private  List<AiTile> clearDuplicate(List<AiTile> tiles)throws StopRequestException {
		ai.checkInterruption();
		for(int i = 0 ; i<tiles.size();i++){
			ai.checkInterruption();
			AiTile temp = tiles.get(i);
			for(int j = i ; j< tiles.size() ; j++){
				ai.checkInterruption();
				if(temp.equals(tiles.get(j))){
					tiles.remove(j);
					j--;
				}
			}
		}
		return tiles;
	}
	

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
	
	ai.checkInterruption();
	AiTile target=ai.getMoveHandler().getCurrentDestination();
	boolean result=false;
	zone = ai.getZone();
	myHero=zone.getOwnHero();
	
	if(canRun(myHero.getTile())){
		
		if(myHero.getBombNumberMax()>0){
			
			if(zone.getTotalTime()>500 && myHero.getBombNumberCurrent()<1 && target.equals(myHero.getTile())){
				
			result=true;
			}

		}
	}
	
	return result;
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
