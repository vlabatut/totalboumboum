
package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 *  This class handles the Tunnel Attack Strategy
 *  It has the common methods which they used in different handlers of the agent
 * 
  * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class TunnelHandler extends AiAbstractHandler<Agent>{

	/**
	 * construct a handler for agent to pass a parameter
	 
	 * @param ai	
	 * 		the agent who is managed by this class
	 */
	public TunnelHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	/**
	 * it holds the enemy who is caught in a tunnel
	 * selectedTileType = 7 category = DEAD_END_ABS  
	 */
	public AiHero theEnemyInsideTheTunnel;
	
	/**
	 * 
	 * @return 
	 * 		The theEnemyInsideTheTunnel.
	 */
	public AiHero getTheEnemyInsideTheTunnel() {
		ai.checkInterruption();
		return theEnemyInsideTheTunnel;
	}
	
	/**
	 * Setter for theEnemyInsideTheTunnel
	 * @param theEnemyInsideTheTunnel the hero that is in current tunnel
	 */
	public void setTheEnemyInsideTheTunnel(AiHero theEnemyInsideTheTunnel) {
		ai.checkInterruption();
		this.theEnemyInsideTheTunnel = theEnemyInsideTheTunnel;
	}


	/**
	 * This method returns true if the tile is a mouth(enter) of a tunnel, else returns false.
	 * @param tile  :a tile in the zone
	 * @return boolean
	 */
	public boolean isAMouth(AiTile tile){
		ai.checkInterruption();
		boolean result = false;
		
		if(tile.isCrossableBy(ai.getZone().getOwnHero()))
		if(!isTileInTunnel(tile))
		
		for(AiTile t : tile.getNeighbors()){
			ai.checkInterruption();
			if(isTileInTunnel(t)){
				result = true;
				break;
			}
			else{
				result = false;
			}
		}
		return result;
	}
	/**
	 * This method return the type of tunnel that the tile is in.
	 * There are 3 type of tunnel; 
	 * 				tunnelVertical:   the type that agents can pass this tunnel vertically.
	 * 				tunnelHorizontal: the type that agents can pass this tunnel horizontally.
	 *				tunnelEnd:        the type that agents can not pass this tunnel.
	 * @param tile :a tile in the zone
	 * @return type of tunnel that the tile is in.
	 */
	public int tunnelType(AiTile tile) {
		ai.checkInterruption();
		AiHero ownhero = ai.getZone().getOwnHero();
		boolean tunnelVertical = tile.isCrossableBy(ownhero ) 
				&& !tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
				&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
				&& (tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
				&& tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero ));

		boolean tunnelHorizontal = tile.isCrossableBy(ownhero)
				&& tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
				&& tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
				&& (!tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
				&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero ));
		
	/*	boolean tunnelBend = tile.isCrossableBy(ownhero )
				
				&& (
					(
					!tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					!tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				   );
		*/
		boolean tunnelEnd = tile.isCrossableBy(ownhero )
				
				&& (
					(
					!tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					!tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				||	
					(
					!tile.getNeighbor(Direction.LEFT).isCrossableBy(ownhero )
					&& tile.getNeighbor(Direction.RIGHT).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.UP).isCrossableBy(ownhero )
					&& !tile.getNeighbor(Direction.DOWN).isCrossableBy(ownhero )
					)
				   );
		int result;
		
		if (tile.isCrossableBy(ownhero )) {
			if (tunnelHorizontal) {
				result = 0;
			} else if (tunnelVertical) {
				result = 1;
			}/* else if(tunnelBend){
				result = 2;
			} */else if(tunnelEnd){
				result = 3;
			}
			else {
				result = -1;
			}
		} else {
			result = -1;
		}
		return result;
	}
	
	/**
	 * This method returns true if the tile is in tunnel else returns false
	 * @param tile :a tile in the zone
	 * @return boolean
	 */
	public boolean isTileInTunnel(AiTile tile) {
		ai.checkInterruption();
		boolean result;
		
		if (tunnelType(tile) != -1) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	/**
	 * This method returns a map that contains dead-ends and theirs mouths(entries)
	 * @return Map: a map of mouths of dead-ends as keys and inside of dead-ends in a list as values.
	 */
	public Map<AiTile, List<AiTile>> getDeadEnds(){
		ai.checkInterruption();
		Map<AiTile, List<AiTile>> result = new HashMap<AiTile, List<AiTile>>();
		
		AiZone zone = ai.getZone();
		List<AiTile> tiles = zone.getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		AiHero ownhero = zone.getOwnHero();
		List<AiTile> mouths = new ArrayList<AiTile>();
		List<AiTile> neighboors = new ArrayList<AiTile>();
		List<AiTile> insideOfDeadEnd = new ArrayList<AiTile>();
		for(AiTile tile : tiles){
			ai.checkInterruption();
			if(isAMouth(tile)){
				mouths.add(tile);
			}
		}
		AiTile tilePointer;
		AiTile nextTilePointer;
		Direction next;
		int tunnelType;
		for(AiTile mouth : mouths){
			ai.checkInterruption();

			neighboors.add(0, mouth.getNeighbor(Direction.LEFT));
			neighboors.add(1, mouth.getNeighbor(Direction.RIGHT));
			neighboors.add(2, mouth.getNeighbor(Direction.DOWN));
			neighboors.add(3, mouth.getNeighbor(Direction.UP));
			
			for(int i = 0; i < 4; i++){
				ai.checkInterruption();
				AiTile neighboor = neighboors.get(i);
				
				if(isTileInTunnel(neighboor)){
					if(tunnelType(neighboor) == 3){
						insideOfDeadEnd.add(neighboor);
						if(!result.containsKey(neighboor)){
							result.put(neighboor, new ArrayList<AiTile>(insideOfDeadEnd));
						}
						else{
							result.get(neighboor).addAll(insideOfDeadEnd);
						}
						insideOfDeadEnd.clear();
						break;
					}
					next = zone.getDirection(mouth, neighboor);
					tunnelType = tunnelType(neighboor);
					tilePointer = neighboor;
					nextTilePointer = tilePointer.getNeighbor(next);
					insideOfDeadEnd.add(neighboor);
					
					while(nextTilePointer.isCrossableBy(ownhero)){
						ai.checkInterruption();
						if(tunnelType(nextTilePointer) == tunnelType){
							tilePointer = nextTilePointer;
							nextTilePointer = tilePointer.getNeighbor(next);
							insideOfDeadEnd.add(tilePointer);
						}
						else{
							if(tunnelType(nextTilePointer) == 3){
								insideOfDeadEnd.add(nextTilePointer);
								if(!result.containsKey(neighboor)){
									result.put(neighboor, new ArrayList<AiTile>(insideOfDeadEnd));
								}
								else{
									result.get(neighboor).addAll(insideOfDeadEnd);
								}
								insideOfDeadEnd.clear();
								break;
							}else{
								insideOfDeadEnd.clear();
								break;
							}
						}
						if(!nextTilePointer.isCrossableBy(ownhero)){
							if(!result.containsKey(mouth)){
								result.put(neighboor, new ArrayList<AiTile>(insideOfDeadEnd));
							}
							else{
								result.get(neighboor).addAll(insideOfDeadEnd);
							}
							insideOfDeadEnd.clear();
							break;
						}
					}
				}
				else{
					continue;
				}
			}
		}
	
		return result;
	}
	
}
