package org.totalboumboum.ai.v201314.ais.saglamturgut.v3;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Contains useful tile utilities.
 * 
 * @author Esra Saglam
 * @author Siyabend Turgut
 */
public class TileUtil {

	/** The agent object (which can fire stop requests) */
	private Agent agent;

	/**
	 * Symbolizes zero.
	 */
	private final int ZERO = 0;

	/**
	 * Constructs the tile utility object with the given agent.
	 * 
	 * @param agent
	 *            Agent to work on.
	 */
	public TileUtil(Agent agent) {
		agent.checkInterruption();
		this.agent = agent;
	}

	/**
	 * All accessible tiles of the own hero.
	 * 
	 * @return All accessible tiles of the own hero.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getAccessibleTiles() throws StopRequestException {
		agent.checkInterruption();

		return getAccessibleTiles(agent.getZone().getOwnHero().getTile());
	}

	/**
	 * All accessible tiles of the given tile.
	 * 
	 * @param tile
	 *            Tile to process.
	 * @return All accessible tiles of the given tile.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getAccessibleTiles(AiTile tile)
			throws StopRequestException {
		agent.checkInterruption();
		return getAccessibleTiles(tile, Collections.<AiTile> emptySet());
	}

	/**
	 * Returns all the accessible tiles to the given tile and the assumed extra
	 * non-crossable blocks.
	 * 
	 * @param tile
	 *            Tile to process.
	 * @param assumedExtraBlocks
	 *            Extra blocks which will be treated as non-crossable.
	 * @return Accessible tiles.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getAccessibleTiles(AiTile tile,
			Set<AiTile> assumedExtraBlocks) throws StopRequestException {
		agent.checkInterruption();

		AiHero ownHero = agent.getZone().getOwnHero();

		// currently accessible tiles
		if (!tile.isCrossableBy(ownHero) || assumedExtraBlocks.contains(tile))
			return Collections.emptySet();

		Set<AiTile> accessibles = new HashSet<AiTile>();

		Queue<AiTile> toBeProcessed = new LinkedList<AiTile>();
		toBeProcessed.add(tile);

		while (!toBeProcessed.isEmpty()) {
			agent.checkInterruption();
			AiTile current = toBeProcessed.poll();
			accessibles.add(current);
			for (Direction direction : Direction.getPrimaryValues()) {
				agent.checkInterruption();
				AiTile neighbor = current.getNeighbor(direction);
				if (neighbor.isCrossableBy(ownHero)
						&& !accessibles.contains(neighbor)
						&& !assumedExtraBlocks.contains(neighbor))
					toBeProcessed.add(neighbor);
			}
		}
		return accessibles;
	}

	/**
	 * Returns all dangerous tiles on the zone.
	 * 
	 * @return All dangerous tiles on the zone.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getDangerousTiles() throws StopRequestException {
		agent.checkInterruption();
		Set<AiTile> dangerousTiles = new HashSet<AiTile>();
		for (AiBomb currentBomb : agent.getZone().getBombs()) {
			agent.checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				agent.checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : agent.getZone().getFires()) {
			agent.checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}
		return dangerousTiles;
	}

	/**
	 * Returns the closest and accessible safe tile to the given tile.
	 * 
	 * @param givenTile
	 *            The tile to check.
	 * @param checkRadius
	 *            Maximum radius to check.
	 * @return The closest safe tile to the given tile.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public AiTile getClosestSafeTile(AiTile givenTile, int checkRadius)
			throws StopRequestException {
		agent.checkInterruption();
		int distance = Integer.MAX_VALUE;
		AiTile result = null;
		for (AiTile currentTile : this.getAccessibleTilesWithinRadius(
				givenTile, checkRadius)) {
			agent.checkInterruption();
			if (!this.getCurrentDangerousTiles().contains(currentTile)
					&& distance > agent.getZone().getTileDistance(givenTile,
							currentTile)) {
				distance = agent.getZone().getTileDistance(givenTile,
						currentTile);
				result = currentTile;
			}
		}
		return result;
	}

	/**
	 * Returns all accessible tiles within given radius to the given tile.
	 * 
	 * @param givenTile
	 *            Tile to process.
	 * @param radius
	 *            Maximum radius to check.
	 * @return All accessible tiles within given radius to the given tile.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getAccessibleTilesWithinRadius(AiTile givenTile,
			int radius) throws StopRequestException {
		agent.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		for (AiTile currentTile : this.getAccessibleTiles(givenTile)) {
			agent.checkInterruption();
			if (agent.getZone().getTileDistance(currentTile, givenTile) <= radius)
				tilesWithinRadius.add(currentTile);
		}
		return tilesWithinRadius;
	}

	/**
	 * Returns the tiles which are currently in danger.
	 * 
	 * @return Dangerous tiles.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getCurrentDangerousTiles() throws StopRequestException {
		agent.checkInterruption();
		Set<AiTile> dangerousTiles = new HashSet<AiTile>();
		for (AiBomb currentBomb : agent.getZone().getBombs()) {
			agent.checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				agent.checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : agent.getZone().getFires()) {
			agent.checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}
		return dangerousTiles;
	}

	/**
	 * Returns if the given tile is dangerous or not.
	 * 
	 * @param tile
	 *            Tile to check.
	 * @return The tile is in danger or not.
	 */
	public boolean isTileDangerous(AiTile tile) {
		agent.checkInterruption();
		return (getCurrentDangerousTiles().contains(tile));
	}

	/**
	 * Returns the tiles set which will be under threat if the agent puts a bomb
	 * to its current location.
	 * 
	 * @return The tiles which are going to be threatened.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getThreatenedTilesOnBombPut()
			throws StopRequestException {
		agent.checkInterruption();
		return getThreatenedTilesOnBombPut(agent.getZone().getOwnHero()
				.getTile());
	}

	/**
	 * Returns the tiles set which will be under threat if an agent puts a bomb
	 * to the given tile.
	 * 
	 * @param tile
	 *            Tile to process.
	 * @return The tiles which are going to be threatened.
	 * @throws StopRequestException
	 *             Stop request by the game engine.
	 */
	public Set<AiTile> getThreatenedTilesOnBombPut(AiTile tile)
			throws StopRequestException {
		agent.checkInterruption();
		AiHero ownHero = agent.getZone().getOwnHero();
		int range = ownHero.getBombRange();

		Set<AiTile> dangerousTilesOnBombPut = new HashSet<AiTile>();
		if (tile.isCrossableBy(ownHero) && (range > ZERO)) {
			dangerousTilesOnBombPut.add(tile);

			AiTile currentTile = tile.getNeighbor(Direction.LEFT);
			int distance = range;
			while (currentTile.isCrossableBy(ownHero) && (distance > ZERO)) {
				agent.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.LEFT);
				distance--;
			}

			currentTile = tile.getNeighbor(Direction.RIGHT);
			distance = range;
			while (currentTile.isCrossableBy(ownHero) && (distance > ZERO)) {
				agent.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.RIGHT);
				distance--;
			}

			currentTile = tile.getNeighbor(Direction.UP);
			distance = range;
			while (currentTile.isCrossableBy(ownHero) && (distance > ZERO)) {
				agent.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.UP);
				distance--;
			}

			currentTile = tile.getNeighbor(Direction.DOWN);
			distance = range;
			while (currentTile.isCrossableBy(ownHero) && (distance > ZERO)) {
				agent.checkInterruption();
				dangerousTilesOnBombPut.add(currentTile);
				if (!currentTile.getItems().isEmpty())
					break;
				currentTile = currentTile.getNeighbor(Direction.DOWN);
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}

	/**
	 * Returns if it makes sense to put a bomb to the agent's current tile. This
	 * method checks this situation directly it just looks if a bomb here can
	 * threaten an enemy (in attack mode) or destroy a tile.
	 * 
	 * @return True if putting a bomb is meaningful, false otherwise.
	 */
	public boolean isTileMeaningfulToBombPut() {
		agent.checkInterruption();
		AiMode mode = agent.getModeHandler().getMode();
		if (mode == AiMode.COLLECTING) {
			return canDestroy(Direction.UP) || canDestroy(Direction.DOWN)
					|| canDestroy(Direction.LEFT)
					|| canDestroy(Direction.RIGHT);
		} else if (mode == AiMode.ATTACKING) {
			return canThreatenEnemyOnBombPut(agent.getZone().getOwnHero()
					.getTile())
					|| canDestroy(Direction.UP)
					|| canDestroy(Direction.DOWN)
					|| canDestroy(Direction.LEFT)
					|| canDestroy(Direction.RIGHT);
		}
		return false;
	}

	/**
	 * Returns the set of accessible enemies of the agent.
	 * 
	 * @return The set of accessible enemies of the agent.
	 */
	public Set<AiHero> getAccessibleEnemies() {
		agent.checkInterruption();
		Set<AiHero> result = new HashSet<AiHero>();
		Set<AiTile> accessibleTiles = getAccessibleTiles();
		for (AiHero aiHero : agent.getZone().getHeroes()) {
			agent.checkInterruption();
			if (aiHero != agent.getZone().getOwnHero()
					&& accessibleTiles.contains(aiHero.getTile())) {
				result.add(aiHero);
			}
		}
		return result;
	}

	/**
	 * Method for getting the closest tile to an enemy inside the given tile
	 * set.
	 * 
	 * @param tiles
	 *            Tiles to check for.
	 * @return Closest tile to any enemy.
	 */
	public AiTile getClosestTileToEnemy(List<AiTile> tiles) {
		agent.checkInterruption();
		AiTile result;
		Map<Integer, AiTile> enemyDistanceTileMap = new HashMap<Integer, AiTile>();
		for (AiTile tile : tiles) {
			agent.checkInterruption();
			int distance = Integer.MAX_VALUE;
			for (AiHero aiHero : agent.getZone().getHeroes()) {
				agent.checkInterruption();
				if (aiHero != agent.getZone().getOwnHero()) {
					int newDistance = getTileDistanceNonCyclic(
							aiHero.getTile(), tile);
					if (newDistance < distance) {
						distance = newDistance;
						enemyDistanceTileMap.put(distance, tile);
					}
				}
			}
		}

		if (!enemyDistanceTileMap.isEmpty()) {
			result = enemyDistanceTileMap.get(Collections
					.min(enemyDistanceTileMap.keySet()));
		} else
			result = tiles.iterator().next();
		return result;
	}

	/**
	 * Returns the non-cyclic tile distance since it is needed for some maps.
	 * 
	 * @param tile1
	 *            First tile.
	 * @param tile2
	 *            Second tile.
	 * @return Manhattan distance between these two tiles.
	 */
	private int getTileDistanceNonCyclic(AiTile tile1, AiTile tile2) {
		agent.checkInterruption();
		return Math.abs(tile1.getRow() - tile2.getRow())
				+ Math.abs(tile1.getCol() - tile2.getCol());
	}

	/**
	 * Checks if a bomb is put to the given tile, can it threaten an emeny or
	 * not.
	 * 
	 * @param tile
	 *            Tile to process.
	 * @return true if a bomb on the given tile can threaten the enemy, false
	 *         otherwise.
	 */
	private boolean canThreatenEnemyOnBombPut(AiTile tile) {
		agent.checkInterruption();
		AiHero ownHero = agent.getZone().getOwnHero();
		Set<AiTile> threatenedTiles = getThreatenedTilesOnBombPut(tile);

		if (threatenedTiles.isEmpty())
			return false;
		for (AiHero aiHero : agent.getZone().getHeroes()) {
			agent.checkInterruption();
			if (aiHero != ownHero) {
				if (threatenedTiles.contains(aiHero.getTile()))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a bomb put by the agent to its current location can destroy a
	 * tile on the given direction.
	 * 
	 * @param direction
	 *            Direction to process.
	 * @return true if a bomb put by the agent to its current location can
	 *         destroy a tile on the given direction, false otherwise.
	 */
	private boolean canDestroy(Direction direction) {
		agent.checkInterruption();
		AiHero ownHero = agent.getZone().getOwnHero();
		int myBombDistance = ownHero.getBombRange();
		int distance = ZERO;
		AiTile currentTile = ownHero.getTile();
		while (currentTile.getBlocks().isEmpty()) {
			agent.checkInterruption();
			distance++;
			currentTile = currentTile.getNeighbor(direction);
		}
		// now check
		for (AiBlock aiBlock : currentTile.getBlocks()) {
			agent.checkInterruption();
			if (aiBlock.isDestructible() && distance <= myBombDistance)
				return true;
		}
		return false;
	}
}
