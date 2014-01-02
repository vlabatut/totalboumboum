package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;

import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;

import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 *
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	checkInterruption();
	}
	
	@SuppressWarnings("static-access")
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		preferenceHandler.speedAttack = false;
		preferenceHandler.ownHeroInTunnel = false;
			
		List<AiTile> myTiles = getZone().getTiles();
		myTiles = new ArrayList<AiTile>(myTiles);
		int i = 0;
		for(AiTile tile : myTiles){
			checkInterruption();
			this.tileId.put(i,tile);
			i++;
		}
//		this.preferenceHandler.setTargetHasEnoughBombs(false);
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
	
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	//////////garip//////////
	
	/**
	 * This method returns true if the tile is a mouth(enter) af a tunnel, else returns false.
	 * @param tile  :a tile in the zone
	 * @return boolean
	 */
	public boolean isAMouth(AiTile tile){
		checkInterruption();
		boolean result = false;
		
		if(tile.isCrossableBy(getZone().getOwnHero()))
		if(!isTileInTunnel(tile))
		
		for(AiTile t : tile.getNeighbors()){
			checkInterruption();
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
		checkInterruption();
		AiHero ownhero = getZone().getOwnHero();
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
		checkInterruption();
		boolean result;
		
		if (tunnelType(tile) != -1) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * This method returns true if our hero can reach before the rival's hero, else returns false.
	 * @param ownhero : Our hero
	 * @param rival   : Our rival's hero
	 * @param target  : Tile that heros want to go
	 * @return boolean
	 */
	public boolean isATileCompetible(AiHero ownhero, AiHero rival, AiTile target){
		checkInterruption();
		boolean result;
		double distancerival, mydistance;
		AiLocation ownLocation = new AiLocation(ownhero);
		AiLocation rivalLocation = new AiLocation(rival);
		
		double mytime, rivaltime;
		
		distancerival = getZone().getPixelDistance(rivalLocation, target);
		mydistance = getZone().getPixelDistance(ownLocation, target);
		
		mytime = mydistance / ownhero.getWalkingSpeed();
		rivaltime = distancerival / rival.getWalkingSpeed();
		
		if(mytime < rivaltime) 
			result = true;
		else 
			result = false;
		
		
		return result;
	}
	/*	
	public boolean isATileCompetible(AiHero ownHero, AiHero rival, AiTile target){
		boolean result;
		int distanceRival, myDistance;
		
		AiTile ownTile = ownHero.getTile();
		AiTile rivalTile = rival.getTile();
		
		double myTime, rivalTime;
		
		distanceRival = getZone().getTileDistance(rivalTile, target);
		myDistance = getZone().getTileDistance(ownTile, target);
		
		myTime = myDistance / ownHero.getWalkingSpeed();
		rivalTime = distanceRival / rival.getWalkingSpeed();
		
		if(myTime < rivalTime) 
			result = true;
		else 
			result = false;
		
		
		return result;
	}
	*/
/*	public Map<AiTile, List<AiTile>> getDeadEnds(){
		checkInterruption();
		Map<AiTile, List<AiTile>> result = new HashMap<AiTile, List<AiTile>>();
		
		AiZone zone = getZone();
		List<AiTile> tiles = zone.getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		AiHero ownhero = zone.getOwnHero();
		List<AiTile> mouths = new ArrayList<AiTile>();
		List<AiTile> neighboors = new ArrayList<AiTile>();
		List<AiTile> insideOfDeadEnd = new ArrayList<AiTile>();
		for(AiTile tile : tiles){
			checkInterruption();
			if(isAMouth(tile)){
				mouths.add(tile);
			}
		}
		AiTile tilePointer;
		AiTile nextTilePointer;
		Direction next;
		int tunnelType;
		for(AiTile mouth : mouths){
			checkInterruption();

			neighboors.add(0, mouth.getNeighbor(Direction.LEFT));
			neighboors.add(1, mouth.getNeighbor(Direction.RIGHT));
			neighboors.add(2, mouth.getNeighbor(Direction.DOWN));
			neighboors.add(3, mouth.getNeighbor(Direction.UP));
			
			for(int i = 0; i < 4; i++){
				checkInterruption();
				AiTile neighboor = neighboors.get(i);
				
				if(isTileInTunnel(neighboor)){
					if(tunnelType(neighboor) == 3){
						insideOfDeadEnd.add(neighboor);
						if(!result.containsKey(mouth)){
							result.put(mouth, new ArrayList<AiTile>(insideOfDeadEnd));
						}
						else{
							result.get(mouth).addAll(insideOfDeadEnd);
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
						if(tunnelType(nextTilePointer) == tunnelType){
							tilePointer = nextTilePointer;
							nextTilePointer = tilePointer.getNeighbor(next);
							insideOfDeadEnd.add(tilePointer);
						}
						else{
							if(tunnelType(nextTilePointer) == 3){
								insideOfDeadEnd.add(nextTilePointer);
								if(!result.containsKey(mouth)){
									result.put(mouth, new ArrayList<AiTile>(insideOfDeadEnd));
								}
								else{
									result.get(mouth).addAll(insideOfDeadEnd);
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
								result.put(mouth, new ArrayList<AiTile>(insideOfDeadEnd));
							}
							else{
								result.get(mouth).addAll(insideOfDeadEnd);
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
	*/
	
	/**
	 * This method returns a map that contains dead-ends and theirs mouths(entries)
	 * @return Map: a map of mouths of dead-ends as keys and inside of dead-ends in a list as values.
	 */
	public Map<AiTile, List<AiTile>> getDeadEnds(){
		checkInterruption();
		Map<AiTile, List<AiTile>> result = new HashMap<AiTile, List<AiTile>>();
		
		AiZone zone = getZone();
		List<AiTile> tiles = zone.getTiles();
		tiles = new ArrayList<AiTile>(tiles);
		AiHero ownhero = zone.getOwnHero();
		List<AiTile> mouths = new ArrayList<AiTile>();
		List<AiTile> neighboors = new ArrayList<AiTile>();
		List<AiTile> insideOfDeadEnd = new ArrayList<AiTile>();
		for(AiTile tile : tiles){
			checkInterruption();
			if(isAMouth(tile)){
				mouths.add(tile);
			}
		}
		AiTile tilePointer;
		AiTile nextTilePointer;
		Direction next;
		int tunnelType;
		for(AiTile mouth : mouths){
			checkInterruption();

			neighboors.add(0, mouth.getNeighbor(Direction.LEFT));
			neighboors.add(1, mouth.getNeighbor(Direction.RIGHT));
			neighboors.add(2, mouth.getNeighbor(Direction.DOWN));
			neighboors.add(3, mouth.getNeighbor(Direction.UP));
			
			for(int i = 0; i < 4; i++){
				checkInterruption();
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
						checkInterruption();
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
	
	/**
	 * a list that contains accesible tiles from a tile
	 */
	public List<AiTile> accesibles = new ArrayList<AiTile>();
	
	/**
	 * setter for accesibles
	 * @param tile :The source tile
	 */
	public void setAccesibleTilesBy(AiTile tile){
		checkInterruption();
		
		AiHero hero = getZone().getOwnHero();
		
		if(tile.isCrossableBy(hero,false, false, false, true, true, true)){ 
			accesibles.add(tile);
		}
		if(tile.getNeighbor(Direction.LEFT).isCrossableBy(hero, false, false, false, true, true, true) && 
		   !accesibles.contains(tile.getNeighbor(Direction.LEFT)) && 
		   tile.getNeighbor(Direction.LEFT).getBombs().isEmpty() && 
		   DangerDegree(tile.getNeighbor(Direction.LEFT)) != 1){
			setAccesibleTilesBy(tile.getNeighbor(Direction.LEFT));
		}
		if(tile.getNeighbor(Direction.RIGHT).isCrossableBy(hero ,false, false, false, true, true, true) && 
		   !accesibles.contains(tile.getNeighbor(Direction.RIGHT)) && 
		   tile.getNeighbor(Direction.RIGHT).getBombs().isEmpty() && 
		   DangerDegree(tile) != 1){
			setAccesibleTilesBy(tile.getNeighbor(Direction.RIGHT));
		}
		if(tile.getNeighbor(Direction.UP).isCrossableBy(hero, false, false, false, true, true, true) && 
		   !accesibles.contains(tile.getNeighbor(Direction.UP)) && 
		   tile.getNeighbor(Direction.UP).getBombs().isEmpty() && 
		   DangerDegree(tile.getNeighbor(Direction.UP)) != 1){
			setAccesibleTilesBy(tile.getNeighbor(Direction.UP));
		}
		if(tile.getNeighbor(Direction.DOWN).isCrossableBy(hero, false, false, false, true, true, true) && 
		   !accesibles.contains(tile.getNeighbor(Direction.DOWN)) && 
		   tile.getNeighbor(Direction.DOWN).getBombs().isEmpty() && 
		   DangerDegree(tile.getNeighbor(Direction.DOWN)) != 1){
			setAccesibleTilesBy(tile.getNeighbor(Direction.DOWN));
		}
		
	}
	/**
	 * Getter of accesibles
	 * @return list of accesible tiles.
	 */
	public List<AiTile> getAccesibles(){
		checkInterruption();
		setAccesibleTilesBy(getZone().getOwnHero().getTile());
		
		return accesibles;
	}
	/**
	 * This method returns: 3 if a tile is not in danger tile of a bomb
	 * 					  : 2 if a tile is in a danger tile of a bomb that will explode in 2 or more seconds
	 * 					  : 1 if a tile is in a danger tile of a bomb that will explode in 2 or less seconds
	 * @param tile that will be analyzed for danger
	 * @return Degree of danger.
	 */
	public int DangerDegree(AiTile tile){
		checkInterruption();
		int result = 3;
		Map<Long, List<AiBomb>> bombs= getZone().getBombsByDelays();
		bombs = new HashMap<Long, List<AiBomb>>(bombs);
		int bombCounter = 0;
		for(Long delay : bombs.keySet()){
			checkInterruption();
			for(AiBomb bomb : bombs.get(delay)){
				checkInterruption();
				if(bomb.getBlast().contains(tile)){
					bombCounter++;
					if(delay < 2000 || bombCounter > 1 || !tile.getFires().isEmpty()){
						result = 1;
						break;
					}
					else{
						result = 2;
						break;
					}
				}
			}
			if(result == 1){
				break;
			}
		}
		return result;
		
	}
	
	/**
	 * This method returns true if our agent can escape from own bomb if it puts a bomb on current tile else returns false.
	 * @return boolean
	 */
	public boolean canEscapeFromOwnBomb(){
		checkInterruption();
		boolean result = false;
		List<AiTile> securityTiles = getAcc();
		securityTiles = new ArrayList<AiTile>(securityTiles);
		List<AiTile> tempSecurityTiles = new ArrayList<AiTile>();
		tempSecurityTiles.addAll(securityTiles);
		AiTile ownTile = getZone().getOwnHero().getTile();
		
		for(AiTile tile : securityTiles){
			checkInterruption();
			if(tile.getPosX() == ownTile.getPosX()){
				if(getZone().getTileDistance(ownTile, tile) <= getZone().getOwnHero().getBombRange()){
					tempSecurityTiles.remove(tile);
				}
			}else{
				if(tile.getPosY() == ownTile.getPosY()){
					if(getZone().getTileDistance(ownTile, tile) <= getZone().getOwnHero().getBombRange()){
						tempSecurityTiles.remove(tile);
					}
				}
			}
			if(DangerDegree(tile) == 1){
				tempSecurityTiles.remove(tile);
			}
		}
		if(!tempSecurityTiles.isEmpty()){
			result = true;
		}
		
		return result;
	}
	
	
	
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
	///////////garip son //////
	
	/**
	 * This method returns list of tiles that can be accessed form our agent.
	 * @return List of tiles that our agent can access
	 */
	public List<AiTile> getAcc(){
		checkInterruption();
		List<AiTile> accTiles = getZone().getTiles();
		accTiles = new ArrayList<AiTile>(accTiles);
		accTiles.clear();
		
		
		CostCalculator costCalculator = new TileCostCalculator(this);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(this);
		
//		CostCalculator costCalculator = new PixelCostCalculator(this);	
		
		Dijkstra dijk = new Dijkstra(this,getZone().getOwnHero(), costCalculator, successorCalculator);
		Map<AiTile, AiSearchNode> myMap = new HashMap<AiTile, AiSearchNode>();
		
		try {
			myMap = dijk.startProcess(new AiLocation(getZone().getOwnHero().getTile()));
		} catch (LimitReachedException e) {
			
			//e.printStackTrace();
		}
		
		accTiles.addAll(myMap.keySet());
		return accTiles;
	}
	
	
	/**
	 * it holds an integer value for each tile in the current game zone
	 */
	private Map<Integer,AiTile> tileId = new HashMap<Integer,AiTile>();
	
	/**
	 * it returns an identifier for a tile
	 * @param tile AiTile 
	 * @return integer : id of the tile
	 */
	public int tileIdentification(AiTile tile){
		checkInterruption();
		int result = -1;
		
			for(int i=0; i < tileId.keySet().size();i++){
				checkInterruption();
			if(tileId.get(i) == tile){
				
				result = i;	
				break;
			}	
			}	
		return result;
	}
	
	
	/**
	 * This method detects if the tile is in danger or not
	 * @param tile AiTile
	 * @return result boolean
	 */
	public boolean isInDanger(AiTile tile){
		checkInterruption();
		AiZone zone = getZone();
		boolean result;
		List<AiTile> dangertiles = new ArrayList<AiTile>();
		Map<Long, List<AiBomb>> allbombs = zone.getBombsByDelays();
		for(long l : allbombs.keySet()){
			checkInterruption();
			for(AiBomb bomb : allbombs.get(l)){
				checkInterruption();
				if(l < 1500){
					dangertiles.addAll(bomb.getBlast());
				}
			}
		}
		
		if(dangertiles.contains(tile)){
			result = true;
		}else{
			result = false;
		}
		return result;
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
		checkInterruption();
		return theEnemyInsideTheTunnel;
	}
	
	/**
	 * Setter for theEnemyInsideTheTunnel
	 * @param theEnemyInsideTheTunnel the hero that is in current tunnel
	 */
	public void setTheEnemyInsideTheTunnel(AiHero theEnemyInsideTheTunnel) {
		checkInterruption();
		this.theEnemyInsideTheTunnel = theEnemyInsideTheTunnel;
	}

	/**
	 * List of tiles that is in the current tunnel
	 */
	public List<AiTile> tilesInTunnel;
	
	/**
	 * Getter for tilesInTunnel
	 * @return the list of tiles in tunnel
	 */
	public List<AiTile> getTilesInTunnel() {
		checkInterruption();
		return tilesInTunnel;
	}

	/**
	 *  Setter for tilesInTunnel
	 * @param tilesInTunnel List of tiles that is in the current tunnel
	 */
	public void setTilesInTunnel(List<AiTile> tilesInTunnel) {
		checkInterruption();
		this.tilesInTunnel =  new ArrayList<AiTile>(tilesInTunnel);
	}

	/**
	 * if the enemy is near the selected tile, for criterion "DistanceAdversaire"
	 */
	public boolean b = false;
	
	/**
	 * if there is a pattern for applying triangle attack strategy
	 */
	public boolean PatternExist = false;
	
	
	/**
	 * list for holding the selected tile for triangle attack strategy
	 * myTiles(0) n myTiles(1) hold the corners and
	 * myTiles(2) holds the middle tile
	 * if there is specific pattern, myTiles(0) also shows the tile where there is enemy is staying near of it(inside of the trap)  
	 */
	public List<AiTile> myTiles = null;
	
	/**
	 * getter for myTiles
	 * @return myTiles
	 */
	public List<AiTile> getMyTiles() {
		checkInterruption();
		return myTiles;
	}

	/**
	 * setter for myTiles
	 * @param myTiles list of AiTiles
	 */
	public void setMyTiles(List<AiTile> myTiles) {
		checkInterruption();
		this.myTiles = myTiles;
	}

	/**
	 * 
	 * @return true if the pattern exist
	 */
	public boolean isPatternExist() {
		checkInterruption();
		return PatternExist;
	}

	/**
	 * setter for PatternExist
	 * @param patternExist true if pattern exist
	 */
	public void setPatternExist(boolean patternExist) {
		checkInterruption();
		PatternExist = patternExist;
	}

	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		b = false;
		// b = preferenceHandler.isThereATileNearByEnemy;
		
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	
	/**
	 * getter for boolean b
	 * @return b
	 */
	public boolean isB() {
		checkInterruption();
		return b;
	}
	/**
	 * setter for b
	 * @param b true if Patterns.isThereATileNearByEnemy is true
	 */
	public void setB(boolean b) {
		checkInterruption();
		this.b = b;
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);


		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	/**
	 * @return
	 */
	
}
