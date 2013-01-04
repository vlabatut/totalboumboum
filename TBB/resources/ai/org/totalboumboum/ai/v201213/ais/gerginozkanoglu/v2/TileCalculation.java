package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
 * @author Seçil Özkanoğlu This class will be used for all the tile operations.
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
	protected ArrayList<AiTile> accessibleTiles;
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
	 * limit to determine the closest tile to our hero.
	 */
	private static int MAX_ACCESSIBLE_TILE = 1;

	/**
	 * bomb's minimum range limit.
	 */
	private static int BASE_LIMIT_RANGE = 0;
	
	/**
	 * limit size.
	 */
	private static int LIMIT_SIZE = 1;

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
	 * this method will check the tile if there is an enemy or not.
	 * 
	 * @param tile
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean existanceEnemy(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		if (!tile.getHeroes().isEmpty())
		{
			if(tile.getHeroes().size() > LIMIT_SIZE)
				return true;
			else
			{
				if(!tile.getHeroes().contains(this.ai.getZone().getOwnHero()))
					return true;
				else
					return false;
			}
		}
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
	protected ArrayList<AiTile> allAccesibleTiles(AiTile tile)
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
	  * calculates number of destructible walls around a tile
	 * @param tile
	 * @return integer
	 * @throws StopRequestException
	 */
	public int neighborhoodOfDestructibleWalls(AiTile tile)
				throws StopRequestException {
			ai.checkInterruption();
			int counter = 0;
			for(AiBlock block : tile.getNeighbor(Direction.UP).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					counter++;
			}
			for(AiBlock block : tile.getNeighbor(Direction.DOWN).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					counter++;
			}
			for(AiBlock block : tile.getNeighbor(Direction.LEFT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					counter++;
			}
			for(AiBlock block : tile.getNeighbor(Direction.RIGHT).getBlocks())
			{
				ai.checkInterruption();
				if(block.isDestructible())
					counter++;
			}
			return counter;
		}
	
	/**
	 * checks if there is an enemy in a range considered in all accessible tiles
	 * @param ourTile
	 * @param bombRange
	 * @return boolean
	 * @throws StopRequestException
	 */
	protected boolean isThereEnemyInRange(AiTile ourTile,int bombRange) throws StopRequestException
	{
		ai.checkInterruption();
		for(AiTile tile : this.allAccesibleTiles(ourTile))
		{
			ai.checkInterruption();
			if(this.ai.getZone().getTileDistance(ourTile, tile) <= bombRange)
			{
				if(this.existanceEnemy(tile))
					return true;
			}
		}
		return false;
	}
	
	


}


