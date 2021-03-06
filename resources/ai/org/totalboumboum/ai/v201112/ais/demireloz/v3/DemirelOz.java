package org.totalboumboum.ai.v201112.ais.demireloz.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class DemirelOz extends ArtificialIntelligence {
	/** */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;
	/** */
	public HashMap<AiTile, Integer> hashmap;

	/**
	 * Gets the Hashmap.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return Returns the HashMap.
	 * */
	public HashMap<AiTile, Integer> getHashmap() throws StopRequestException {
		checkInterruption();
		return hashmap;
	}

	/**
	 * Permits us to make changes on the hashmap and save it.
	 * 
	 * @param hashmap
	 *            - The hashmap
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * */
	public void setHashmap(HashMap<AiTile, Integer> hashmap)
			throws StopRequestException {
		checkInterruption();
		this.hashmap = hashmap;
	}

	@Override
	protected void init() throws StopRequestException {
		checkInterruption();

		super.init();
		verbose = false;
		hashmap = new HashMap<AiTile, Integer>();

	}

	@Override
	protected void initPercepts() throws StopRequestException {
		checkInterruption();

	}

	@Override
	protected void updatePercepts() throws StopRequestException {
		checkInterruption();

	}

	/** */
	protected ModeHandler modeHandler;
	/** */
	protected UtilityHandler utilityHandler;
	/** */
	protected BombHandler bombHandler;
	/** */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException {
		checkInterruption();

		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);

	}

	@Override
	protected AiModeHandler<DemirelOz> getModeHandler()
			throws StopRequestException {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<DemirelOz> getUtilityHandler()
			throws StopRequestException {
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<DemirelOz> getBombHandler()
			throws StopRequestException {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<DemirelOz> getMoveHandler()
			throws StopRequestException {
		checkInterruption();
		return moveHandler;
	}

	@Override
	protected void updateOutput() throws StopRequestException {
		checkInterruption();
		// moveHandler.updateOutput();
		// utilityHandler.updateOutput();
	}

	/**
	 * Adds the given tile with the integer value to the hashmap.
	 * 
	 * @param aitile
	 *            - The key.
	 * @param integer
	 *            - The value.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * */
	public void callHashMap(AiTile aitile, int integer)
			throws StopRequestException {
		this.checkInterruption();
		getHashmap().put(aitile, integer);
		setHashmap(getHashmap());

	}

	/**
	 * For a given tile's neighbor, it will control the existence of blocks.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile in the given direction
	 *         has a block or wall on it, it will return false.
	 * */
	public boolean controlOfBlocks(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;

		if (aitile.getNeighbor(direction).getBlocks().isEmpty()) {
			if (aitile.getNeighbor(direction).getBombs().isEmpty()) {
				result = true;

			}
		}
		return result;

	}

	/**
	 * controlOfDestructibleBlock will look if the given tile's neighbor
	 * contains a destructible block.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile in the given direction
	 *         has a destructible wall, it will return true "
	 * */
	public boolean controlOfDestructibleBlock(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		for (AiBlock currentblock : aitile.getNeighbor(direction).getBlocks()) {
			this.checkInterruption();
			if (currentblock.isDestructible()
					&& aitile.getNeighbor(direction).getItems().isEmpty()) {

				return result = true;
			}
		}

		return result;

	}

	/**
	 * controlEnemy will look for an enemy in the neighbor of the given tile,in
	 * the specified direction.
	 * 
	 * @param aitile
	 *            - The target.
	 * @param direction
	 *            - The direction that we will be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile's neighbor in the
	 *         given direction has an enemy, it will return true "
	 * */
	public boolean controlEnemy(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();

		boolean result = false;

		if (getNearestEnemy().getTile().equals(aitile.getNeighbor(direction))) {
			result = true;
		}
		return result;
	}

	/**
	 * getAnEnemyInMyRange will look for an enemy in the given tile's neighbors
	 * for a specified direction.
	 * 
	 * @param aitile
	 *            - Our tile.
	 * @param i
	 *            - The number of times we will be cycling in the function, this
	 *            number corresponds to our current range.
	 * @param direction
	 *            - The given direction.In this direction we will be looking for
	 *            an enemy
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile's neighbors contains
	 *         an enemy in the given direction, it will be true
	 * */
	public boolean getAnEnemyInMyRange(AiTile aitile, Direction direction, int i)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		int currentrange = ourhero.getBombRange();

		if (!zone.getRemainingOpponents().isEmpty()) {

			while (i < currentrange) {
				this.checkInterruption();
				if (controlOfBlocks(aitile, direction) == true
						&& aitile.getNeighbor(direction).getItems().isEmpty()) {
					if (controlEnemy(aitile, direction) == true) {
						return result = true;
					} else {
						i++;
						return result = getAnEnemyInMyRange(
								aitile.getNeighbor(direction), direction, i);
					}
				}

				else {
					return result = false;
				}
			}
		}
		return result;

	}

	/**
	 * This method is used to learn , in which mode we are in.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return The mode of our agent is returned
	 * */
	public AiMode getMode() throws StopRequestException {
		this.checkInterruption();
		AiMode aimode = modeHandler.getMode();

		return aimode;
	}

	/**
	 * getWallInDanger controls if the given tile contains blocks and if it is
	 * in danger.
	 * 
	 * @param aitile
	 *            - The target aitile.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value.If the tile that contains a wall
	 *         is in the range of a bomb, the result will be true.
	 * */
	public boolean getWallInDanger(AiTile aitile) throws StopRequestException {
		this.checkInterruption();

		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		boolean result = false;
		if (!aitile.getBlocks().isEmpty()) {
			for (AiBlock currentBlock : aitile.getBlocks()) {
				this.checkInterruption();
				if (currentBlock.isDestructible()) {
					for (AiBomb currentBomb : zone.getBombs()) {
						this.checkInterruption();
						if (currentBomb.getBlast().contains(aitile)) {
							return result = true;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * This method will look for danger in the given tile.
	 * 
	 * @param aitile
	 *            - The target aitile.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value. If the tile that we are looking
	 *         is in danger the result will be true
	 * */
	public boolean getTileInDanger(AiTile aitile) throws StopRequestException {
		this.checkInterruption();

		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		boolean result = false;

		if (zone.getFires().contains(aitile)) {
			return result = true;
		} else {
			for (AiBomb currentBomb : zone.getBombs()) {
				this.checkInterruption();
				if (currentBomb.getBlast().contains(aitile)) {

					if (currentBomb.getExplosionDuration() > 0) {
						return result = true;
					}
				}

			}

		}

		return result;
	}

	/**
	 * This method is used for determining the accessible tiles of the hero that
	 * must be given in the parameters list. In the mean time it simulates a
	 * bomb in the given tile, and in our own tile if the value of tile is null.
	 * 
	 * @param hero
	 *            - The agent which we will be looking for.
	 * @param tile
	 *            - The tile that we want to simulate our bomb on.If tile is
	 *            null then the bomb will be created on our tile.
	 * @param simulate
	 *            - If true getSafeTiles() will make a simulation of the given
	 *            hero in the given tile.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return The return of this method will be a list of aitile which are
	 *         accesible to a specific hero
	 * */
	public List<AiTile> getSafeTiles(AiHero hero, AiTile tile, boolean simulate)
			throws StopRequestException {
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiSimZone simzone = new AiSimZone(zone);
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		ArrayList<AiTile> safe = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();

		list.add(hero.getTile());

		for (AiBomb b : simzone.getBombs()) {
			this.checkInterruption();

			if (b.getBlast().contains(hero.getTile()))
				continue;

			for (AiTile t : b.getBlast()) {
				this.checkInterruption();
				blastflamelist.add(t);
			}
		}

		for (AiFire aifire : simzone.getFires()) {
			this.checkInterruption();
			blastflamelist.add(aifire.getTile());
		}
		while (!list.isEmpty()) {
			this.checkInterruption();
			ArrayList<AiTile> list1 = new ArrayList<AiTile>();
			for (AiTile aitile : list) {
				this.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					this.checkInterruption();
					AiTile neighbor = aitile.getNeighbor(direction);

					if (neighbor.isCrossableBy(hero)
							&& !safe.contains(neighbor)) {
						if (!blastflamelist.contains(neighbor)) {
							list1.add(neighbor);
						}
					}

				}
				safe.add(aitile);
			}
			for (AiTile aitile : safe) {
				this.checkInterruption();
				if (list.contains(aitile)) {
					list.remove(aitile);
				}
			}
			for (AiTile aitile : list1) {
				this.checkInterruption();
				list.add(aitile);
			}
		}
		// simulates our bomb and removes blast range from the safe tiles.
		if (simulate) {
			AiBomb b = simzone.createBomb(tile, simzone.getOwnHero());
			safe.removeAll(b.getBlast());
		}
		// checks if we have chain reaction possibilities and removes those
		// tiles.
		/*
		 * ArrayList<AiTile> list4 = new ArrayList<AiTile>();
		 * if(!safe.isEmpty()) { for (AiTile aiTile : safe)
		 * {this.checkInterruption(); if(getDanger2(aiTile)) {
		 * list4.add(aiTile); } } } safe.removeAll(list4);
		 */
		return safe;
	}

	/**
	 * This method is the non-cyclic version of getDirection().
	 * 
	 * @param source
	 *            - The source tile.
	 * @param target
	 *            - The target tile.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return Returns the direction between the source and target tile.
	 * */
	public Direction getNonCycDirection(AiTile source, AiTile target)
			throws StopRequestException {
		this.checkInterruption();

		int dx = target.getCol() - source.getCol();
		int dy = target.getRow() - source.getRow();

		Direction temp = Direction.getCompositeFromDouble(dx, dy);
		Direction tempX = temp.getHorizontalPrimary();
		Direction tempY = temp.getVerticalPrimary();

		Direction result = Direction.getComposite(tempX, tempY);
		return result;
	}

	/**
	 * This method helps us to find out if we and the enemy agent are on the
	 * same vertical or horizontal direction.
	 * 
	 * @param tile
	 *            - It takes our tile as the first parameter.
	 * @param tile2
	 *            - This is the second parameter which must show the enemy's
	 *            tile.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return It returns 0 if there are indestructible blocks between our agent
	 *         and the enemy, and if we aren't on the same line. If not, it
	 *         returns the straight distance between our hero's tile and the
	 *         enemy agent's tile.
	 * 
	 */
	public int controlStraight(AiTile tile, AiTile tile2)
			throws StopRequestException {
		this.checkInterruption();
		int flag = 0;
		int counter = 0;
		Direction direction = getNonCycDirection(tile, tile2);

		if (direction.isVertical() || direction.isHorizontal()) {

			while (tile != tile2) {
				this.checkInterruption();
				flag = 0;
				if (tile.getNeighbor(direction).getBlocks().isEmpty()
						&& counter == 0) {
					return counter = 0;
				}

				if (!tile.getNeighbor(direction).getBlocks().isEmpty()) {
					for (AiBlock currentBlock : tile.getNeighbor(direction)
							.getBlocks()) {
						this.checkInterruption();
						if (currentBlock.isDestructible()) {
							tile = tile.getNeighbor(direction);
							counter++;
							flag = 1;
						} else {
							return counter = 0;

						}
					}

				}
				if (flag == 0 && counter > 0) {
					tile = tile.getNeighbor(direction);
					counter++;
				}

			}
		}
		
		return counter;
	}

	/**
	 * 
	 * This method looks for the tile with the biggest utility value found in
	 * the list. And it controls if those tiles are in danger or not.
	 * 
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return BiggestTile among the list is returned
	 * */
	public AiTile getBiggestTile() throws StopRequestException {
		this.checkInterruption();
		HashMap<AiTile, Float> hashmap;
		hashmap = getUtilityHandler().getUtilitiesByTile();

		AiTile biggestTile = null;
		float value = -10;

		for (AiTile currentTile : hashmap.keySet()) {
			this.checkInterruption();
			if (getUtilityHandler().getUtilitiesByTile().get(currentTile) > value
					&& !getDanger2(currentTile)) {
				value = getUtilityHandler().getUtilitiesByTile().get(
						currentTile);
				biggestTile = currentTile;

			}
		}
		return biggestTile;

	}

	/**
	 * 
	 * This method looks for the closest tile to the given tile among the
	 * selected tile list.
	 * 
	 * @param aitile
	 *            The tile that we want to perform getClosestTile().
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return ClosestTile among the selected tile list is returned
	 * */
	public AiTile getClosestTile(AiTile aitile) throws StopRequestException {
		this.checkInterruption();
		Set<AiTile> list = this.utilityHandler.selectTiles();
		AiTile closestTile = null;
		float value = 50;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();

		if (aitile == ourtile) {
			list.remove(ourtile);
		}

		for (AiTile currentTile : list) {
			this.checkInterruption();
			if (getDist(aitile, currentTile) < value
					&& !getDanger2(currentTile)) {
				value = getDist(aitile, currentTile);
				closestTile = currentTile;
			}
		}

		return closestTile;

	}

	/**
	 * 
	 * This method does the same thing as getClosestTile(), with a reduced
	 * selected tile list.
	 * 
	 * @param aitile
	 *            The tile that we want to perform getClosestTileDest().
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return ClosestTile among the selected tile list is returned
	 * */
	public AiTile getClosestTileDest(AiTile aitile) throws StopRequestException {
		this.checkInterruption();
		Set<AiTile> list = this.utilityHandler.selectTiles();
		ArrayList<AiTile> list1 = new ArrayList<AiTile>();
		AiTile closestTile = null;
		float value = 50;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		// We take only the tiles close to a destructible wall.
		for (AiTile currentTile : list) {
			this.checkInterruption();
			for (Direction direction : Direction.getPrimaryValues()) {
				this.checkInterruption();
				if (controlOfDestructibleBlock(currentTile, direction)) {
					list1.add(currentTile);
				}
			}

		}

		// And we perform getClosestTile.
		for (AiTile currentTile : list1) {
			this.checkInterruption();
			if (getDist(aitile, currentTile) < value) {
				value = getDist(aitile, currentTile);
				closestTile = currentTile;
			}
		}

		return closestTile;

	}

	/**
	 * 
	 * This method searches the zone and finds the enemy agent which is closest
	 * to our tile
	 * 
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return An enemy AiHero closest to our agent is returned
	 * */
	public AiHero getNearestEnemy() throws StopRequestException {
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if (enemy.hasEnded())
				continue;

			int dist = this.getDist(enemy.getTile(), ourtile);
			if (dist < enemydist) {
				nearestEnemy = enemy;
				enemydist = dist;
			}
		}
		return nearestEnemy;
	}

	/**
	 * 
	 * getDist is a method which returns the non cyclic distance between two
	 * tiles.
	 * 
	 * @param aitile
	 *            - The first given tile.
	 * 
	 * @param aitile1
	 *            - The second tile is our target.
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return distance between the tiles
	 * */
	public int getDist(AiTile aitile, AiTile aitile1)
			throws StopRequestException {
		this.checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}

	/**
	 * 
	 * simBlock method returns a tiles which will block the enemy if we drop
	 * bombs on them.
	 * 
	 * @param tile
	 *            - The given tile.
	 * 
	 * 
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * 
	 * @return if we can block an enemy ,the result will be true.
	 * */
	public Boolean simBlock(AiTile tile) throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		if (!zone.getRemainingOpponents().isEmpty()) {
			if (getSafeTiles(getNearestEnemy(), tile, true).isEmpty()) {
				return result = true;
			}
		}
		return result;
	}

	/**
	 * This method functions the same way as the danger method but it can be
	 * used for tiles far away from our agent.
	 * 
	 * @param tile
	 *            - The tile that we are going to be looking at.
	 * @throws StopRequestException
	 *             In case where the game engine requests an end to the game
	 * @return It will return a boolean value. If there is danger in this tile,
	 *         it will return true.
	 * 
	 * */
	public Boolean getDanger2(AiTile tile) throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiPartialModel apm = new AiPartialModel(zone);
		long crossTile = Math.round(1000 * tile.getSize()
				/ ourhero.getWalkingSpeed());
		long crossTime = crossTile * getDist(ourtile, tile);

		if (!tile.getBombs().isEmpty()) {
			result = true;
			return result;
		}

		if (apm.getExplosion(tile) != null) {
			if (apm.getExplosion(tile).getEnd() < crossTime && crossTime != 0) {
				return result = false;
			}

			else {
				return result = true;

			}
		} else {
			return result = false;
		}

	}

}
