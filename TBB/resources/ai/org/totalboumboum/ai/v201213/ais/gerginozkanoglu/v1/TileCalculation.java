package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu 
 * This class will be used for all the tile operations.
 *         It will be used in criterions' calculs.
 * 
 */
public class TileCalculation {

	/**
	 * agent.
	 */
	private final GerginOzkanoglu ai;
	/**
	 * all accessible tiles in the zone.
	 */
	protected ArrayList<AiTile> accessibleTiles ;
	/**
	 * Maximum number of item extra bomb that we have.
	 */
	private static int EXTRABOMB_MAX = 3;

	/**
	 * hero.
	 */
	private AiHero ourHero;

	/**
	 * this will determine the maximum range of our bomb. if our range is less
	 * than this static variable's value, we will get this item.
	 */
	private static int RANGE_MAX = 7;

	/**
	 * Utilities of tiles will be kept in this hashmap.
	 */
	private Map<AiTile, Float> utilities;

	/**
	 * limit to determine the nearest tile to our hero.
	 */
	private static int MAX_ACCESSIBLE_TILES = 6;
	
	/**
	 * bomb's minimum range limit.
	 */
	private static int BASE_LIMIT_RANGE = 0;

	
	/**
	 * constructor
	 * @param ai
	 * @throws StopRequestException
	 */
	public TileCalculation(GerginOzkanoglu ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		utilities = this.ai.getUtilityHandler().getUtilitiesByTile();

	}

	/**
	 * @return dangerousBoxes
	 * @throws StopRequestException
	 *             This method will add all dangerous boxes to a list and return
	 *             it.
	 */
	
	public ArrayList<AiTile> getDangerousBoxes() throws StopRequestException {
		ai.checkInterruption();

		ArrayList<AiTile> dangerousBoxes = new ArrayList<AiTile>();
		// all tiles contains bombs will be considered dangerous and added to
		// list.
		for (AiBomb bomb : ai.getZone().getBombs()) {
			ai.checkInterruption();
			dangerousBoxes.add(bomb.getTile());
			for (AiTile rangeOfBombs : bomb.getBlast()) {
				ai.checkInterruption();
				dangerousBoxes.add(rangeOfBombs);
			}
		}
		// all tiles contains fires will be considered dangerous and added to
		// list.
		for (AiFire fire : ai.getZone().getFires()) {
			ai.checkInterruption();
			dangerousBoxes.add(fire.getTile());
		}

		return dangerousBoxes;

	}

	/**
	 * @param consideredTile
	 * @return control
	 * @throws StopRequestException
	 *             This method will be directly used by security criterion and
	 *             time.
	 */
	
	public boolean isDangerous(AiTile consideredTile)
			throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTiles = this.getDangerousBoxes();
		Iterator<AiTile> it = dangerousTiles.iterator();
		boolean control = false;
		while (it.hasNext() && !control) {
			ai.checkInterruption();
			if (consideredTile.equals(it))
				control = true;
		}
		return control;
	}

	/**
	 * This method will calculate the number of destructible walls around a
	 * tile.
	 * 
	 * @param tile
	 * @return counter
	 * @throws StopRequestException
	 */
	public int numberOfDestructibleWalls(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		int counter = 0;
		for (AiBlock block : tile.getNeighbor(Direction.UP).getBlocks()) {
			ai.checkInterruption();
			if (block.isDestructible())
				counter++;
		}
		for (AiBlock block : tile.getNeighbor(Direction.DOWN).getBlocks()) {
			ai.checkInterruption();
			if (block.isDestructible())
				counter++;
		}
		for (AiBlock block : tile.getNeighbor(Direction.LEFT).getBlocks()) {
			ai.checkInterruption();
			if (block.isDestructible())
				counter++;
		}
		for (AiBlock block : tile.getNeighbor(Direction.RIGHT).getBlocks()) {
			ai.checkInterruption();
			if (block.isDestructible())
				counter++;
		}
		return counter;
	}

	/**
	 * Evaluate item's status.
	 * 
	 * @param tile
	 * @return result
	 * @throws StopRequestException
	 */
	public int evaluateVisibleItem(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int result = 2;
		this.ourHero = this.ai.getZone().getOwnHero();

		if (tile.getItems().contains(AiItemType.ANTI_BOMB)
				|| tile.getItems().contains(AiItemType.ANTI_FLAME)
				|| tile.getItems().contains(AiItemType.ANTI_SPEED)
				|| tile.getItems().contains(AiItemType.NO_BOMB)
				|| tile.getItems().contains(AiItemType.NO_FLAME)
				|| tile.getItems().contains(AiItemType.NO_SPEED)
				|| tile.getItems().contains(AiItemType.RANDOM_NONE))
			result = 0;
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			if (this.ourHero.getBombNumberCurrent() < EXTRABOMB_MAX)
				result = 2;
			else
				result = 1;
		}
		if (tile.getItems().contains(AiItemType.GOLDEN_BOMB)) {
			if (this.ourHero.getBombNumberMax() == this.ourHero
					.getBombNumberCurrent()) {
				// then this will show that we have the item "golden bomb"
				result = 1;
			} else {
				result = 2;
			}
		}
		if (tile.getItems().contains(AiItemType.GOLDEN_FLAME)
				|| tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			if (this.ourHero.getBombRange() < RANGE_MAX)
				result = 2;
			else
				result = 1;
		}
		if (tile.getItems().contains(AiItemType.RANDOM_EXTRA))
			result = 1;
		if (tile.getItems().contains(AiItemType.EXTRA_SPEED)
				|| tile.getItems().contains(AiItemType.GOLDEN_SPEED)) {
			result = 2;
		}

		return result;
	}

	/**
	 * This method will calculate the time for an agent to arrive a tile.
	 * 
	 * @param destination
	 * @param agent
	 * @return timeToArriveDestination
	 * @throws StopRequestException
	 */
	
	public long getTimeForDistance(AiTile destination, AiHero agent)
			throws StopRequestException {
		ai.checkInterruption();
		return ((long) this.ai.getZone().getTileDistance(agent.getTile(),
				destination))
				/ ((long) agent.getWalkingSpeed());

	}

	/**
	 * this method will check the tile if there is an enemy or not.
	 * 
	 * @param tile
	 * @return boolean
	 * @throws StopRequestException
	 */

	public boolean existanceEnemy(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (!tile.getHeroes().isEmpty()
				&& tile.compareTo(this.ai.getZone().getOwnHero().getTile()) != 0)
			return true;
		else
			return false;
	}

	/**
	 * check if a tile has a destructible wall
	 * 
	 * @param tile
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean isDestructible(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (tile.getBlocks().isEmpty())
			return false;
		else {
			for (AiBlock block : tile.getBlocks()) {
				ai.checkInterruption();
				if (block.isDestructible())
					return true;
			}
		}
		return false;

	}

	/**
	 * this method will check if there is a wall or an item around a tile. (This
	 * will be used in attack mode)
	 * 
	 * @param tile
	 * @return boolean
	 * @throws StopRequestException
	 */
	
	public boolean wallOrItemAroundTile(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		Iterator<AiTile> iter = tile.getNeighbors().iterator();
		while (iter.hasNext()) {
			ai.checkInterruption();
			AiTile neighborTiles = iter.next();
			if (!neighborTiles.getItems().isEmpty()
					|| this.isDestructible(neighborTiles))
				return true;
		}
		return false;

	}

	/**
	 * @return result. return the tile with highest utility.
	 * @throws StopRequestException
	 */
	protected AiTile mostValuableTile() throws StopRequestException {
		ai.checkInterruption();
		AiTile result = this.ai.getZone().getOwnHero().getTile();
		for (AiTile currentTile : this.utilities.keySet()) {
			ai.checkInterruption();
			if (utilities.get(currentTile) > utilities.get(result)) {
				result = currentTile;
			}
		}
		return result;
	}

	/**
	 * @param tile
	 * @return Compares if the tile considered has the highest utility value.
	 * @throws StopRequestException
	 */
	protected boolean isTileMostValuable(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		if (this.utilities.get(tile) == this.utilities.get(this
				.mostValuableTile()))
			return true;
		return false;
	}

	/**
	 * @return actually return the tiles which come from the selection of tiles.
	 * @throws StopRequestException
	 */
	protected Set<AiTile> allAccessibleTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		for (AiTile tile : this.ai.getZone().getTiles()) {
			ai.checkInterruption();
			if (tile.isCrossableBy(this.ai.getZone().getOwnHero(), true, true,
					true, false, false, false))
				result.add(tile);
		}

		return result;
	}
	

	/**
	 * @param currentTile
	 * @return The arraylist of the nearest tiles depend on a limit that we
	 *         determined.
	 * @throws StopRequestException
	 */
	protected ArrayList<AiTile> closestTiles(AiTile currentTile)
			throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> closestTiles = new ArrayList<AiTile>();
		for (AiTile tile : this.allAccessibleTiles()) {
			ai.checkInterruption();
			if (Math.abs(this.ai.getZone().getTileDistance(currentTile, tile)) <= MAX_ACCESSIBLE_TILES)
				closestTiles.add(tile);
		}
		return closestTiles;
	}
	
	/**
	 * this methode will find closest accesible tiles in a range of MAX_ACCESIBLE_TILES.
	 * @param ourTile
	 * @return closestTiles
	 * @throws StopRequestException
	 */
	protected ArrayList<AiTile> closestAccesibleTiles(AiTile ourTile) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> closestTiles = new ArrayList<AiTile>();
		for(AiTile accesibleTile: this.allAccesibleTiles(ourTile))
		{
			ai.checkInterruption();
			if(Math.abs(this.ai.getZone().getTileDistance(ourTile, accesibleTile)) <=  MAX_ACCESSIBLE_TILES)
				closestTiles.add(accesibleTile);
		}
		return closestTiles;
	}

	/**
	 * @param ourTile
	 * @return return the closest and the safest tile.
	 * @throws StopRequestException
	 */
	protected AiTile closestAndSafestTile(AiTile ourTile)
			throws StopRequestException {
		ai.checkInterruption();
		AiTile result = ourTile;
		int distance = MAX_ACCESSIBLE_TILES;
		for (AiTile tile : this.closestTiles(ourTile)) {
			ai.checkInterruption();
			if (!this.getDangerousBoxes().contains(tile)
					&& distance >= Math.abs(this.ai.getZone().getTileDistance(
							tile, ourTile))) {
				distance = Math.abs(this.ai.getZone().getTileDistance(tile,
						ourTile));
				result = tile;
			}

		}
		return result;

	}
	/**
	 * closest and safest tile after we put a bomb.
	 * @param ourTile
	 * @param bombRange 
	 * @return safest Tile
	 * @throws StopRequestException
	 */
	protected AiTile closestAndSafestTileAfterBombing(AiTile ourTile, int bombRange) throws StopRequestException
	{
		ai.checkInterruption();
		
		int distance = MAX_ACCESSIBLE_TILES;
		
		AiTile result = ourTile;
		for(AiTile closestTile : this.closestAccesibleTiles(ourTile))
		{ 
			ai.checkInterruption();
			boolean control = true;
			for(AiTile dangerousTile : this.rangeOfOurBomb(ourTile, bombRange) )
			{
				ai.checkInterruption();
				if(closestTile.equals(dangerousTile))
					control = false;	
			}
			if(control)
			{
				if (!this.getDangerousBoxes().contains(closestTile)
						&& distance >= Math.abs(this.ai.getZone().getTileDistance(
								closestTile, ourTile))) {
					distance = Math.abs(this.ai.getZone().getTileDistance(closestTile,
							ourTile));
					result = closestTile;
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * @param tile
	 * @return boolean. This method check if there is a bomb in the considered
	 *         tile. Actually, we will use it to check if we put a bomb in any
	 *         tile.
	 * @throws StopRequestException 
	 */
	protected boolean isThereBomb(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (tile.getBombs().isEmpty())
			return false;
		return true;
	}
	/**
	 * @param tileConsidered
	 * @param rangeOfBomb
	 * @return rangeOfOurBomb
	 *      we consider as we put a bomb in the "tileConsidered", then we calculate the range of bomb.
	 * @throws StopRequestException
	 */
	protected ArrayList<AiTile> rangeOfOurBomb(AiTile tileConsidered, int rangeOfBomb) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> rangeOfOurBomb = new ArrayList<AiTile>();
		if(tileConsidered.isCrossableBy(this.ai.getZone().getOwnHero()) && rangeOfBomb > BASE_LIMIT_RANGE)
		{
			rangeOfOurBomb.add(tileConsidered);
			AiTile neighbor = tileConsidered.getNeighbor(Direction.LEFT);
			int distance = rangeOfBomb;
			while(neighbor.isCrossableBy(this.ai.getZone().getOwnHero()) && distance > BASE_LIMIT_RANGE)
			{
				ai.checkInterruption();
				rangeOfOurBomb.add(neighbor);
				neighbor = neighbor.getNeighbor(Direction.LEFT);
				distance--;
			}
			neighbor = tileConsidered.getNeighbor(Direction.RIGHT);
			distance = rangeOfBomb;
			while(neighbor.isCrossableBy(this.ai.getZone().getOwnHero()) && distance > BASE_LIMIT_RANGE)
			{
				ai.checkInterruption();
				rangeOfOurBomb.add(neighbor);
				neighbor = neighbor.getNeighbor(Direction.RIGHT);
				distance--;
			}
			neighbor = tileConsidered.getNeighbor(Direction.UP);
			distance = rangeOfBomb;
			while(neighbor.isCrossableBy(this.ai.getZone().getOwnHero()) && distance > BASE_LIMIT_RANGE)
			{
				ai.checkInterruption();
				rangeOfOurBomb.add(neighbor);
				neighbor = neighbor.getNeighbor(Direction.UP);
				distance--;
			}
			neighbor = tileConsidered.getNeighbor(Direction.DOWN);
			distance = rangeOfBomb;
			while(neighbor.isCrossableBy(this.ai.getZone().getOwnHero()) && distance > BASE_LIMIT_RANGE)
			{
				ai.checkInterruption();
				rangeOfOurBomb.add(neighbor);
				neighbor = neighbor.getNeighbor(Direction.DOWN);
				distance--;
			}
		}
		return rangeOfOurBomb;
		
		
	}
    /**
     * @param tile
     * @param rangeOfBomb
     * @return boolean
     *      checks if there is an enemy in our range.
     * @throws StopRequestException
     */
    protected boolean threatenEnemy(AiTile tile, int rangeOfBomb) throws StopRequestException
    {
    	ai.checkInterruption();
    	Iterator<AiHero> iter = this.ai.getZone().getRemainingHeroes().iterator();
    	boolean control = true;
    	while(iter.hasNext() && control)
    	{
    		ai.checkInterruption();
            AiHero hero = iter.next();
            if(!hero.equals(this.ai.getZone().getOwnHero()))
            {
            	  if(this.rangeOfOurBomb(tile, rangeOfBomb).contains(hero.getTile()))
                  	control = false;
            }
            
    	}
    	return !control;

    }
    
    /**
     * @param sourceTile
     * @throws StopRequestException
     * All accessible tiles.
     */
    protected void allAccessibleTiles(AiTile sourceTile) throws StopRequestException
    {
    	ai.checkInterruption();
    	if(sourceTile.isCrossableBy(this.ai.getZone().getOwnHero()))
    	{
    		accessibleTiles.add(sourceTile);
    		if(sourceTile.getNeighbor(Direction.UP).isCrossableBy(this.ai.getZone().getOwnHero()) && !accessibleTiles.contains(sourceTile.getNeighbor(Direction.UP)))
    		{
    			ai.checkInterruption();
    			allAccessibleTiles(sourceTile.getNeighbor(Direction.UP));
    		}
    		if(sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(this.ai.getZone().getOwnHero()) && !accessibleTiles.contains(sourceTile.getNeighbor(Direction.DOWN)))
    		{
    			ai.checkInterruption();
    			allAccessibleTiles(sourceTile.getNeighbor(Direction.DOWN));
    		}
    		if(sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(this.ai.getZone().getOwnHero()) && !accessibleTiles.contains(sourceTile.getNeighbor(Direction.LEFT)))
    		{
    			ai.checkInterruption();
    			allAccessibleTiles(sourceTile.getNeighbor(Direction.LEFT));
    		}
    		if(sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(this.ai.getZone().getOwnHero()) && !accessibleTiles.contains(sourceTile.getNeighbor(Direction.RIGHT)))
    		{
    			ai.checkInterruption();
    			allAccessibleTiles(sourceTile.getNeighbor(Direction.RIGHT));
    		}
    	}
    	
    	
    }
    
    /**
     * @param tile
     * @return accesibleTiles
     * @throws StopRequestException
     */
    protected ArrayList<AiTile> allAccesibleTiles(AiTile tile) throws StopRequestException
    {
    	ai.checkInterruption();
    	this.accessibleTiles =new ArrayList<AiTile>();
    	this.allAccessibleTiles(tile);
    	return this.accessibleTiles;
    }
    

}
