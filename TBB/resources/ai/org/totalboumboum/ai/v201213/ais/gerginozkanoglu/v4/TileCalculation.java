package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu This class will be used for all the tile operations.
 *         It will be used in criterions' calculs.
 * 
 */
@SuppressWarnings("deprecation")
public class TileCalculation {



	/**
	 * Range bottom limit
	 */
	private static int RANGE_BOTTOM_LIMIT = 0;
	/**
	 * Distance bottom limit
	 */
	private static int DISTANCE_BOTTOM_LIMIT = 0;
	/**
	 * limit to find safe tile after bombing.
	 */
	private static  int SAFETY_TILE_COUNT_LIMIT = 0;
	/**
	 * agent.
	 */
	private final GerginOzkanoglu ai;
	/**
	 * all accessible tiles in the zone.
	 */
	protected ArrayList<AiTile> accessibleTiles;
	/**
	 * Maximum number of item extra bomb that we have.
	 */
	private static int EXTRABOMB_MAX = 3;

	
	
	/**
	 * this will determine the maximum range of our bomb. if our range is less
	 * than this static variable's value, we will get this item.
	 */
	private static int RANGE_MAX = 3;

	/**
	 * Utilities of tiles will be kept in this hashmap.
	 */
	protected Map<AiTile, Float> utilities;

	/**
	 * limit to determine the closest tile to our hero.
	 */
	private static int MAX_ACCESSIBLE_TILE = 2;

	/**
	 * bomb's minimum range limit.
	 */
	private static int BASE_LIMIT_RANGE = 0;
	/**
	 * constructor
	 * 
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
			for (AiTile rangeOfBombs : this.rangeOfBombCrossableByHero(bomb.getTile(), bomb.getRange())) {
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
		TileCalculation calculate = new TileCalculation(this.ai);
		ArrayList<AiTile> dangerousBoxes = calculate.getDangerousBoxes();
		if(dangerousBoxes.contains(consideredTile))
			return true;
		return false;
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
		for (AiTile consideredTile : this.rangeOfOurBombForDestructible(tile,
				this.ai.getZone().getOwnHero().getBombRange())) {
			ai.checkInterruption();
			for (AiBlock block : consideredTile.getBlocks()) {
				ai.checkInterruption();
				if (block.isDestructible())
					counter++;
			}
		}
		return counter;
	}

	/**
	 * Evaluate item's status.
	 * we use this method in criterion of the case visible item.
	 * 
	 * @param tile
	 * @return result
	 * @throws StopRequestException
	 */
	public int evaluateVisibleItem(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		int result = 2;
		AiHero ourHero = this.ai.getZone().getOwnHero();
		for(AiItem item : tile.getItems())
		{
			ai.checkInterruption();
			if(item.isContagious())
				return 0;
		}

		if (tile.getItems().contains(AiItemType.ANTI_BOMB)
				|| tile.getItems().contains(AiItemType.ANTI_FLAME)
				|| tile.getItems().contains(AiItemType.ANTI_SPEED)
				|| tile.getItems().contains(AiItemType.NO_BOMB)
				|| tile.getItems().contains(AiItemType.NO_FLAME)
				|| tile.getItems().contains(AiItemType.NO_SPEED)
				|| tile.getItems().contains(AiItemType.RANDOM_NONE))
			result = 0;
		if (tile.getItems().contains(AiItemType.EXTRA_BOMB)) {
			if (ourHero.getBombNumberCurrent() < EXTRABOMB_MAX)
				result = 2;
			else
				result = 1;
		}
		if (tile.getItems().contains(AiItemType.GOLDEN_BOMB)) {
			if (ourHero.getBombNumberMax() == ourHero
					.getBombNumberCurrent()) {
				// then this will show that we have the item "golden bomb"
				result = 1;
			} else {
				result = 2;
			}
		}
		if (tile.getItems().contains(AiItemType.GOLDEN_FLAME)
				|| tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
			if (ourHero.getBombRange() < RANGE_MAX)
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
	 * this method will check the tile if there is an enemy or not.
	 * 
	 * @param tile
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean existanceEnemy(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if(!this.ai.getZone().getRemainingOpponents().isEmpty())
		{
			for(AiHero hero :this.ai.getZone().getRemainingOpponents())
			{
				ai.checkInterruption();
				if(hero.getTile().equals(tile))
					return true;
			}
		}
		return false;
	}
	/**
	 * @return result.
	 *  return the tile with highest utility.
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
	 * this methode will find closest accesible tiles in a range of
	 * MAX_ACCESIBLE_TILES.
	 * 
	 * @param ourTile
	 * @return closestTiles
	 * @throws StopRequestException
	 */
	protected ArrayList<AiTile> closestAccesibleTiles(AiTile ourTile)
			throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> closestTiles = new ArrayList<AiTile>();
		for (AiTile accesibleTile : this.allAccesibleTiles(ourTile)) {
			ai.checkInterruption();
			if (Math.abs(this.ai.getZone().getTileDistance(ourTile,
					accesibleTile)) <= (MAX_ACCESSIBLE_TILE + (this.ai.getZone().getOwnHero().getBombRange())))
				closestTiles.add(accesibleTile);
		}
		return closestTiles;
	}

	
	/**
	 * closest and safest tile after we put a bomb.
	 * 
	 * @param ourTile
	 * @param bombRange
	 * @return safest Tile
	 * @throws StopRequestException
	 */
	protected AiTile closestAndSafestTileAfterBombing(AiTile ourTile,
			int bombRange) throws StopRequestException {
		ai.checkInterruption();

		int distance = MAX_ACCESSIBLE_TILE + (this.ai.getZone().getOwnHero().getBombRange());

		AiTile result = null;
		for (AiTile closestTile : this.closestAccesibleTiles(ourTile)) {
			ai.checkInterruption();
			boolean control = true;
			for (AiTile dangerousTile : this.rangeOfBombCrossableByHero(ourTile, bombRange)) {
				ai.checkInterruption();
				if (closestTile.equals(dangerousTile))
					control = false;
			}
			if (control) {
				if (!this.getDangerousBoxes().contains(closestTile)
						&& distance >= Math.abs(this.ai.getZone()
								.getTileDistance(closestTile, ourTile))) {
					distance = Math.abs(this.ai.getZone().getTileDistance(
							closestTile, ourTile));
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
		if(!tile.getBombs().isEmpty())
			return true;
		return false;
	}

	

	/**
	 * @param tileConsidered
	 * @param rangeOfBomb
	 * @return this method will be used to calculate how many destructible walls
	 *         can be destroyed.
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> rangeOfOurBombForDestructible(
			AiTile tileConsidered, int rangeOfBomb) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> rangeOfOurBomb = new ArrayList<AiTile>();
		if (tileConsidered.isCrossableBy(this.ai.getZone().getOwnHero())
				&& rangeOfBomb > BASE_LIMIT_RANGE) {
			rangeOfOurBomb.add(tileConsidered);
			AiTile neighbor = tileConsidered.getNeighbor(Direction.LEFT);
			int distance = rangeOfBomb;
			boolean control = true;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else {
					Iterator<AiBlock> it = neighbor.getBlocks().iterator();
					while (it.hasNext() && control) {
						ai.checkInterruption();
						if (it.next().isDestructible())
							rangeOfOurBomb.add(neighbor);
						control = false;

					}
				}
				neighbor = neighbor.getNeighbor(Direction.LEFT);
				distance--;
			}
			control = true;
			neighbor = tileConsidered.getNeighbor(Direction.RIGHT);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else {
					Iterator<AiBlock> it = neighbor.getBlocks().iterator();
					while (it.hasNext() && control) {
						ai.checkInterruption();
						if (it.next().isDestructible())
							rangeOfOurBomb.add(neighbor);
						control = false;

					}
				}
				neighbor = neighbor.getNeighbor(Direction.RIGHT);
				distance--;
			}
			control = true;
			neighbor = tileConsidered.getNeighbor(Direction.UP);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else {
					Iterator<AiBlock> it = neighbor.getBlocks().iterator();
					while (it.hasNext() && control) {
						ai.checkInterruption();
						if (it.next().isDestructible())
							rangeOfOurBomb.add(neighbor);
						control = false;

					}
				}
				neighbor = neighbor.getNeighbor(Direction.UP);
				distance--;
			}
			control = true;
			neighbor = tileConsidered.getNeighbor(Direction.DOWN);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else {
					Iterator<AiBlock> it = neighbor.getBlocks().iterator();
					while (it.hasNext() && control) {
						ai.checkInterruption();
						if (it.next().isDestructible())
							rangeOfOurBomb.add(neighbor);
						control = false;

					}
				}
				neighbor = neighbor.getNeighbor(Direction.DOWN);
				distance--;
			}
		}
		return rangeOfOurBomb;

	}
	
	/**
	 * @param tileOfBomb
	 * @param rangeOfBomb
	 * @return
	 *    this method only counts the crossable tiles affected by a bomb and add them to a list
	 *    The empty-crossable tiles in danger will be supposed as not-secure soon.
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> rangeOfBombCrossableByHero(
			AiTile tileOfBomb, int rangeOfBomb) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> rangeOfOurBomb = new ArrayList<AiTile>();
		if (rangeOfBomb > BASE_LIMIT_RANGE ) {
			rangeOfOurBomb.add(tileOfBomb);
			AiTile neighbor = tileOfBomb.getNeighbor(Direction.LEFT);
			int distance = rangeOfBomb;
			boolean control = true;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else 
					control = false;
				neighbor = neighbor.getNeighbor(Direction.LEFT);
				distance--;
			}
			control = true;
			neighbor = tileOfBomb.getNeighbor(Direction.RIGHT);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else 
					control = false;
				neighbor = neighbor.getNeighbor(Direction.RIGHT);
				distance--;
			}
			control = true;
			neighbor = tileOfBomb.getNeighbor(Direction.UP);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else 
					control = false;
				neighbor = neighbor.getNeighbor(Direction.UP);
				distance--;
			}
			control = true;
			neighbor = tileOfBomb.getNeighbor(Direction.DOWN);
			distance = rangeOfBomb;
			while (distance > BASE_LIMIT_RANGE && control) {
				ai.checkInterruption();
				if (neighbor.isCrossableBy(this.ai.getZone().getOwnHero()))
					rangeOfOurBomb.add(neighbor);
				else
					control = false;
				neighbor = neighbor.getNeighbor(Direction.DOWN);
				distance--;
			}
		}
		return rangeOfOurBomb;

	}


	/**
	 * checks if there is an enemy in our range.
	 * @param tile
	 * @param rangeOfBomb
	 * @return boolean 
	 * @throws StopRequestException
	 */
	protected boolean threatenEnemy(AiTile tile, int rangeOfBomb)
			throws StopRequestException {
		ai.checkInterruption();

		for(AiHero hero : this.ai.getZone().getRemainingHeroes())
		{
			ai.checkInterruption();
			if(!hero.equals(this.ai.getZone().getOwnHero()))
			{
				if(this.rangeOfBombCrossableByHero(tile, rangeOfBomb).contains(hero.getTile()))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param sourceTile
	 * @throws StopRequestException
	 *             All accessible tiles.
	 */
	protected void allAccessibleTiles(AiTile sourceTile)
			throws StopRequestException {
		ai.checkInterruption();
		if (sourceTile.isCrossableBy(this.ai.getZone().getOwnHero())) {
			accessibleTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(
					this.ai.getZone().getOwnHero())
					&& !accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP))) {
				ai.checkInterruption();
				allAccessibleTiles(sourceTile.getNeighbor(Direction.UP));
			}
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(
					this.ai.getZone().getOwnHero())
					&& !accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN))) {
				ai.checkInterruption();
				allAccessibleTiles(sourceTile.getNeighbor(Direction.DOWN));
			}
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(
					this.ai.getZone().getOwnHero())
					&& !accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT))) {
				ai.checkInterruption();
				allAccessibleTiles(sourceTile.getNeighbor(Direction.LEFT));
			}
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(
					this.ai.getZone().getOwnHero())
					&& !accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT))) {
				ai.checkInterruption();
				allAccessibleTiles(sourceTile.getNeighbor(Direction.RIGHT));
			}
		}

	}

	/**
	 * calculates all accessible tiles.
	 * @param tile
	 * @return accesibleTiles
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> allAccesibleTiles(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		this.accessibleTiles = new ArrayList<AiTile>();
		this.allAccessibleTiles(tile);
		return this.accessibleTiles;
	}
	/**
	 * returns the closest and safest tile for an hero.
	 * if there is not a safest tile, it will return null
	  * @param hero
	  * @return safestTile 
	  * @throws StopRequestException
	  */
	 protected AiTile closestAndSafestTileForHero(AiHero hero) throws StopRequestException
	 {
	  ai.checkInterruption();
	  int distance = MAX_ACCESSIBLE_TILE + (this.ai.getZone().getOwnHero().getBombRange());
	  AiTile  safestTile = null;
	  for(AiTile accesibleTile : this.closestAccesibleTiles(hero.getTile()))
	  {
	   ai.checkInterruption();
	   if(!accesibleTile.equals(hero.getTile()))
	   {
	    if(Math.abs(this.ai.getZone().getTileDistance(accesibleTile, hero.getTile())) <= distance && !this.isDangerous(accesibleTile))
	    {
	     distance = Math.abs(this.ai.getZone().getTileDistance(accesibleTile, hero.getTile()));
	     safestTile = accesibleTile;
	    }
	   }
	  }
	   return safestTile;
	 }
	/**
	  * @param ourTile
	  * @return boolean
	  * @throws StopRequestException
	  */
	 /**
	  * @param ourTile
	  * @return boolean
	  * @throws StopRequestException
	  */
	protected boolean blockingEnemyIsPossible(AiTile ourTile)
			throws StopRequestException {
		ai.checkInterruption();
		int bombRange = this.ai.getZone().getOwnHero().getBombRange();
		AiTile neighbor = ourTile;
		boolean directionControl = true;
		boolean enemyControl = false;
		AiHero enemy = null;
		AiHero ourHero = this.ai.getZone().getOwnHero();
		for (int i = 0; i < bombRange; i++) {
			ai.checkInterruption();
			neighbor = neighbor.getNeighbor(Direction.UP);
		    if(!neighbor.isCrossableBy(ourHero))
		    	break;
			if (this.existanceEnemy(neighbor)) {
				enemy = neighbor.getHeroes().get(0);
				enemyControl = true;
				break;
			}
		}
		if(enemyControl)
		{
			int counter = 0;
			neighbor = ourTile;
			boolean control = false;
			for(int i = 0; i < bombRange; i++)
			{
				ai.checkInterruption();
				neighbor = neighbor.getNeighbor(Direction.UP);
				if(!neighbor.isCrossableBy(enemy))
				{
					control = true;
					break;
				}
				else
					counter++;
				
			}
			if(counter == bombRange)
			{
				neighbor = neighbor.getNeighbor(Direction.UP);
				if(!neighbor.isCrossableBy(enemy))
					control = true;
			}
			if(counter > 0 && control)
			{
//				System.out.println(counter);
				neighbor = ourTile;
				for(int i = 0; i < counter; i++)
				{
					ai.checkInterruption();
					neighbor = neighbor.getNeighbor(Direction.UP);
					if (neighbor.getNeighbor(Direction.LEFT).isCrossableBy(enemy)
							|| neighbor.getNeighbor(Direction.RIGHT).isCrossableBy(
									enemy)) {
						directionControl = false;
						break;
					}
				}
				if (directionControl)
					return true;
			}
		}
		enemy = null;
		directionControl = true;
		enemyControl = false;
		neighbor = ourTile;
		for (int i = 0; i < bombRange; i++) {
			ai.checkInterruption();
			neighbor = neighbor.getNeighbor(Direction.DOWN);
		    if(!neighbor.isCrossableBy(ourHero))
		    	break;
			if (this.existanceEnemy(neighbor)) {
				enemy = neighbor.getHeroes().get(0);
				enemyControl = true;
				break;
			}
		}
		if(enemyControl)
		{
			boolean control = false;
			int counter = 0;
			neighbor = ourTile;
			for(int i = 0; i < bombRange; i++)
			{
				ai.checkInterruption();
				neighbor = neighbor.getNeighbor(Direction.DOWN);
				if(!neighbor.isCrossableBy(enemy))
				{
					control = true;
					break;
				}
				else
					counter++;
			}
			if(counter == bombRange)
			{
				neighbor = neighbor.getNeighbor(Direction.UP);
				if(!neighbor.isCrossableBy(enemy))
					control = true;
			}
			if(counter > 0 && control)
			{
//				System.out.println(counter);
				neighbor = ourTile;
				for(int i = 0; i < counter; i++)
				{
					ai.checkInterruption();
					neighbor = neighbor.getNeighbor(Direction.DOWN);
					if (neighbor.getNeighbor(Direction.LEFT).isCrossableBy(enemy)
							|| neighbor.getNeighbor(Direction.RIGHT).isCrossableBy(
									enemy)) {
						directionControl = false;
						break;
					}
				}
				if (directionControl)
					return true;
			}
		}
		enemy = null;
		directionControl = true;
		enemyControl = false;
		neighbor = ourTile;
		for (int i = 0; i < bombRange; i++) {
			ai.checkInterruption();
			neighbor = neighbor.getNeighbor(Direction.LEFT);
		    if(!neighbor.isCrossableBy(ourHero))
		    	break;
			if (this.existanceEnemy(neighbor)) {
				enemy = neighbor.getHeroes().get(0);
				enemyControl = true;
				break;
			}
		}
		if(enemyControl)
		{
			boolean control = false;
			int counter = 0;
			neighbor = ourTile;
			for(int i = 0; i < bombRange; i++)
			{
				ai.checkInterruption();
				neighbor = neighbor.getNeighbor(Direction.LEFT);
				if(!neighbor.isCrossableBy(enemy))
					{
					control = true;
					break;
					}
				else
					counter++;
			}
			if(counter == bombRange)
			{
				neighbor = neighbor.getNeighbor(Direction.UP);
				if(!neighbor.isCrossableBy(enemy))
					control = true;
			}
			if(counter > 0 && control)
			{
//				System.out.println(counter);
				neighbor = ourTile;
				for(int i = 0; i < counter; i++)
				{
					ai.checkInterruption();
					neighbor = neighbor.getNeighbor(Direction.LEFT);
					if (neighbor.getNeighbor(Direction.UP).isCrossableBy(enemy)
							|| neighbor.getNeighbor(Direction.DOWN).isCrossableBy(
									enemy)) {
						directionControl = false;
						break;
					}
				}
				if (directionControl)
					return true;
			}
		}
		enemy = null;
		directionControl = true;
		enemyControl = false;
		neighbor = ourTile;
		for (int i = 0; i < bombRange; i++) {
			ai.checkInterruption();
			neighbor = neighbor.getNeighbor(Direction.RIGHT);
		    if(!neighbor.isCrossableBy(ourHero))
		    	break;
			if (this.existanceEnemy(neighbor)) {
				enemy = neighbor.getHeroes().get(0);
				enemyControl = true;
				break;
			}
		}
		if(enemyControl)
		{
			boolean control = false;
			int counter = 0;
			neighbor = ourTile;
			for(int i = 0; i < bombRange; i++)
			{
				ai.checkInterruption();
				neighbor = neighbor.getNeighbor(Direction.RIGHT);
				if(!neighbor.isCrossableBy(enemy))
					{
					control = true;
					break;
					}
				else
					counter++;
			}
			if(counter == bombRange)
			{
				neighbor = neighbor.getNeighbor(Direction.UP);
				if(!neighbor.isCrossableBy(enemy))
					control = true;
			}
			if(counter > 0 && control)
			{
//				System.out.println(counter);
				neighbor = ourTile;
				for(int i = 0; i < counter; i++)
				{
					ai.checkInterruption();
					neighbor = neighbor.getNeighbor(Direction.RIGHT);
					if (neighbor.getNeighbor(Direction.UP).isCrossableBy(enemy)
							|| neighbor.getNeighbor(Direction.DOWN).isCrossableBy(
									enemy)) {
						directionControl = false;
						break;
					}
				}
				if (directionControl)
					return true;
			}
		}
		return false;
	}
	/**
	 * finds closest enemy to our agent.
	 * @param ourTile
	 * @return AiHero
	 * @throws StopRequestException
	 */
	public AiHero closestEnemyToUs(AiTile ourTile) throws StopRequestException 
	{
		ai.checkInterruption();
		AiHero hero = null;
		int distance = Integer.MAX_VALUE;
		for(AiHero enemy : this.ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			int distanceToEnemy = this.ai.getZone().getTileDistance(enemy.getTile(), ourTile);
			if(distance > distanceToEnemy)
			{
				distance = distanceToEnemy;
				hero = enemy;
			}
		}
		return hero;
	}
	/**
	 * finds closest wall to enemy.
	 * @param tile
	 * @param closestEnemy
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean closestWallToEnemy(AiTile tile,AiHero closestEnemy) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile ourTile = this.ai.getZone().getOwnHero().getTile();
		Direction ourDirectionToEnemy = this.ai.getZone().getDirection(ourTile, closestEnemy.getTile());
		if(ourDirectionToEnemy == Direction.UP || ourDirectionToEnemy == Direction.DOWN || ourDirectionToEnemy == Direction.LEFT || ourDirectionToEnemy == Direction.RIGHT)
		{
			for(AiBlock block : tile.getNeighbor(ourDirectionToEnemy).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
		}
		else if(ourDirectionToEnemy == Direction.DOWNLEFT)
		{
			for(AiBlock block : tile.getNeighbor(Direction.LEFT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
			for(AiBlock block : tile.getNeighbor(Direction.DOWN).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
		}
		else if(ourDirectionToEnemy == Direction.DOWNRIGHT)
		{
			for(AiBlock block : tile.getNeighbor(Direction.RIGHT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
			for(AiBlock block : tile.getNeighbor(Direction.DOWN).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
		}
		else if(ourDirectionToEnemy == Direction.UPLEFT)
		{
			for(AiBlock block : tile.getNeighbor(Direction.LEFT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
			for(AiBlock block : tile.getNeighbor(Direction.UP).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
		}
		else
		{
			for(AiBlock block : tile.getNeighbor(Direction.RIGHT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}for(AiBlock block : tile.getNeighbor(Direction.UP).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					return true;
			}
		}
		return false;
	}
	/**
	 * checks if there is an enemy in all accesible tiles or not
	 * @param ourTile
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean isThereEnemyInAllAccessibleTiles(AiTile ourTile) throws StopRequestException
	{
		ai.checkInterruption();
		for(AiTile tile : this.allAccesibleTiles(ourTile))
		{
			ai.checkInterruption();
			if(this.existanceEnemy(tile))
				return true;
		}
		return false;
	}
	/**
	 * calculates number of malus items around a tile. We will bomb them in bomb handler.
	 * @param currentTile
	 * @return integer.
	 * @throws StopRequestException
	 */
	protected int malusItemsCount(AiTile currentTile) throws StopRequestException
	{
		ai.checkInterruption();
		int counter = 0;
		for (AiTile consideredTile : this.rangeOfOurBombForDestructible(currentTile,
				this.ai.getZone().getOwnHero().getBombRange())) {
			ai.checkInterruption();
			for (AiItem item : consideredTile.getItems()) {
				ai.checkInterruption();
				if (item.isContagious() || item.getType().equals(AiItemType.ANTI_FLAME) || item.getType().equals(AiItemType.ANTI_BOMB) ||  item.getType().equals(AiItemType.ANTI_SPEED) ||  item.getType().equals(AiItemType.NO_BOMB) ||  item.getType().equals(AiItemType.NO_FLAME) ||  item.getType().equals(AiItemType.NO_SPEED) ||  item.getType().equals(AiItemType.RANDOM_NONE))
					counter++;
			}
		}
		return counter;
	}
	/**
	 * calculates a list of tiles which will become dangerous if a bomb is put on
	 * given tile.
	 * @param givenTile
	 * @param range
	 * @return arrayList
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut( AiTile givenTile, int range ) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();
		if ( givenTile.isCrossableBy( this.ai.getZone().getOwnHero() ) && ( range > RANGE_BOTTOM_LIMIT ) )
		{
			dangerousTilesOnBombPut.add( givenTile );

			AiTile currentTile = givenTile.getNeighbor( Direction.LEFT );
			int distance = range;
			while ( currentTile.isCrossableBy( this.ai.getZone().getOwnHero()) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.LEFT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.RIGHT );
			distance = range;
			while ( currentTile.isCrossableBy( this.ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.RIGHT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.UP );
			distance = range;
			while ( currentTile.isCrossableBy( this.ai.getZone().getOwnHero()) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.UP );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.DOWN );
			distance = range;
			while ( currentTile.isCrossableBy( this.ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.DOWN );
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}
	/**
	 * checks if our hero can reach safely in a tile.
	 * @param givenHero
	 * @return boolean
	 * @throws StopRequestException
	 */
	protected boolean canReachSafety( AiHero givenHero ) throws StopRequestException
	{
		ai.checkInterruption();
		
		ArrayList<AiTile> accessibleTiles = this.allAccesibleTiles(givenHero.getTile());
		int safeTileCount = accessibleTiles.size();
		for ( AiTile currentTile : accessibleTiles )
		{
			ai.checkInterruption();
			if ( this.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentTile ) || this.getDangerousBoxes().contains( currentTile ) ) safeTileCount--;
		}
		return ( safeTileCount > SAFETY_TILE_COUNT_LIMIT );
	}
	/**
	 * controls if there is sudden death in considered tile or not
	 * @param consideredTile
	 * @return boolean 
	 * @throws StopRequestException
	 */
	public boolean isThereSuddenDeathEventInTile(AiTile consideredTile) throws StopRequestException
    {
    	ai.checkInterruption();
    	AiSuddenDeathEvent event = this.ai.getZone().getNextSuddenDeathEvent();
    	if(event!=null && event.getTiles().contains(consideredTile))
    		return true;
    	return false;
    }
	/**
	 * calculates closest accessible destructible wall to enemy
	 * @return AiTile
	 * @throws StopRequestException
	 */
	public AiTile getClosestAccDesWalltoEnemy() throws StopRequestException
	{
		ai.checkInterruption();
		double distance = Double.MAX_VALUE;
		AiTile result = null;
		AiHero ourHero = this.ai.getZone().getOwnHero();
		for ( AiTile currentTile : this.getAccessibleDestructibleWallTiles( ourHero.getTile() ) )
		{
			ai.checkInterruption();
			if ( this.getDistanceBetween( currentTile, this.getClosestEnemy().getTile() ) < distance )
			{
				result = currentTile;
				distance = this.getDistanceBetween( currentTile, this.getClosestEnemy().getTile() );
			}
		}
		return result;
	}
	/**
	 * calculates closest enemy to us.
	 * @return AiHero
	 * @throws StopRequestException
	 */
	public AiHero getClosestEnemy() throws StopRequestException
	{
		ai.checkInterruption();
		double distance = Double.MAX_VALUE;
		AiHero result = null;

		for ( AiHero currentHero : this.ai.getZone().getRemainingOpponents() )
		{
			ai.checkInterruption();
			if ( this.getDistanceBetween( this.ai.getZone().getOwnHero().getTile(), currentHero.getTile() ) < distance )
			{

				distance = this.getDistanceBetween( this.ai.getZone().getOwnHero().getTile(), currentHero.getTile() );
				result = currentHero;
			}
		}
		return result;
	}
	/**
	 * calculates all accessible dest wall tiles.
	 * @param heroTile
	 * @return ArrayList
	 * @throws StopRequestException
	 */
	protected ArrayList<AiTile> getAccessibleDestructibleWallTiles( AiTile heroTile ) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> accessibleDestructibleWallTiles = new ArrayList<AiTile>();
		for ( AiTile currentTile : this.allAccesibleTiles( heroTile ) )
		{
			ai.checkInterruption();
			for ( AiTile neighborTile : currentTile.getNeighbors() )
			{
				ai.checkInterruption();
				for ( AiBlock neighborBlock : neighborTile.getBlocks() )
				{
					ai.checkInterruption();
					if ( neighborBlock.isDestructible() && !accessibleDestructibleWallTiles.contains( neighborBlock ) )
					{
						accessibleDestructibleWallTiles.add( neighborTile );
					}
				}
			}
		}
		return accessibleDestructibleWallTiles;
	}
	/**
	 * calculates distance between two tile.
	 * @param startTile
	 * @param endTile
	 * @return double
	 * @throws StopRequestException
	 */
	public double getDistanceBetween( AiTile startTile, AiTile endTile ) throws StopRequestException
	{
		ai.checkInterruption();
		return Math.sqrt( ( ( startTile.getCol() - endTile.getCol() ) * ( startTile.getCol() - endTile.getCol() ) ) + ( ( startTile.getRow() - endTile.getRow() ) * ( startTile.getRow() - endTile.getRow() ) ) );
	}


	
}



