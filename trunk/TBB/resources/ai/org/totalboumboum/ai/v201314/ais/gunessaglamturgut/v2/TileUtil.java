/**
 *
 */
package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2;

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains useful tile utilities.
 * @author Esra Saglam
 */
public class TileUtil {
	/** */
	private Agent agent;
	/** */
	private Set<AiTile> accessibleTiles;

	/**
	 * @param agent
	 * 		? 
	 * 
	 */
	public TileUtil(Agent agent) {
		agent.checkInterruption();
		this.agent = agent;
	}


	/**
	 * All accessible tiles of the own hero.
	 * 
	 * @return 
	 * 		All accessible tiles of the own hero.
	 * @throws StopRequestException
	 * 		?
	 */
	public Set<AiTile> getAccessibleTiles() throws StopRequestException {
		agent.checkInterruption();
		this.accessibleTiles = new HashSet<AiTile>();
		fillAccessibleTilesBy(agent.getZone().getOwnHero().getTile());

		return this.accessibleTiles;
	}

	/**
	 * All accessible tiles of the given tile.
	 * 
	 * @param tile 
	 * 		Tile to process.
	 * @return 
	 * 		All accessible tiles of the given tile.
	 * @throws StopRequestException
	 * 		?
	 */
	public Set<AiTile> getAccessibleTiles(AiTile tile) throws StopRequestException {
		agent.checkInterruption();
		this.accessibleTiles = new HashSet<AiTile>();
		fillAccessibleTilesBy(tile);

		return this.accessibleTiles;
	}

	/**
	 * Helper method for getAccessibleTiles method.
	 * 
	 * @param sourceTile 
	 * 		tile to start filling the accessible tiles.
	 * @throws StopRequestException
	 * 		?
	 */
	private void fillAccessibleTilesBy(AiTile sourceTile) throws StopRequestException {
		agent.checkInterruption();
		if (sourceTile.isCrossableBy(agent.getZone().getOwnHero())) {
			this.accessibleTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(agent.getZone().getOwnHero()) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(agent.getZone().getOwnHero()) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(agent.getZone().getOwnHero()) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(agent.getZone().getOwnHero()) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}
	}

	/**
	 * Returns the tiles which contain an enemy within the bomb range.
	 * @param bombRange 
	 * 		Bomb range to check.
	 * @return 
	 * 		The tiles which contain an enemy within the bomb range.
	 * @throws StopRequestException
	 * 		?
	 */
	public Set<AiTile> getEnemyTilesInBombRange(int bombRange) throws StopRequestException {
		agent.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		Set<AiTile> dangerousTiles = getDangerousTiles();
		for (AiHero ennemy : agent.getZone().getRemainingOpponents()) {
			agent.checkInterruption();
			AiTile neighbor = ennemy.getTile();
			if (!agent.getZone().getOwnHero().getTile().equals(neighbor))
				tilesWithinRadius.add(neighbor);
			agent.checkInterruption();
			for (Direction direction : Direction.getPrimaryValues()) {
				agent.checkInterruption();
				int i = 1;
				neighbor = ennemy.getTile();
				while (i <= bombRange) {
					agent.checkInterruption();
					neighbor = neighbor.getNeighbor(direction);
					if (!neighbor.isCrossableBy(ennemy) || dangerousTiles.contains(neighbor))
						break;
					tilesWithinRadius.add(neighbor);
					i++;
				}
			}
		}
		tilesWithinRadius.remove(agent.getZone().getOwnHero().getTile());
		return tilesWithinRadius;
	}

	/**
	 * Returns all dangerous tiles on the zone.
	 * 
	 * @return 
	 * 		All dangerous tiles on the zone.
	 * @throws StopRequestException
	 * 		?
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
	 * 		The tile to check.
	 * @param checkRadius 
	 * 		Maximum radius to check.
	 * @return 
	 * 		The closest safe tile to the given tile.
	 * @throws StopRequestException
	 * 		?
	 */
	protected AiTile getClosestSafeTile(AiTile givenTile, int checkRadius) throws StopRequestException {
		agent.checkInterruption();
		int distance = Integer.MAX_VALUE;
		AiTile result = null;
		for (AiTile currentTile : this.getAccessibleTilesWithinRadius(givenTile, checkRadius)) {
			agent.checkInterruption();
			if (!this.getCurrentDangerousTiles().contains(currentTile) && distance > agent.getZone().getTileDistance(givenTile, currentTile)) {
				distance = agent.getZone().getTileDistance(givenTile, currentTile);
				result = currentTile;
			}
		}
		return result;
	}

	/**
	 * Returns all accessible tiles within given radius to the given tile.
	 * 
	 * @param givenTile 
	 * 		Tile to process.
	 * @param radius 
	 * 		Maximum radius to check.
	 * @return 
	 * 		All accessible tiles within given radius to the given tile.
	 * @throws StopRequestException
	 * 		?
	 */
	public Set<AiTile> getAccessibleTilesWithinRadius(AiTile givenTile, int radius) throws StopRequestException {
		agent.checkInterruption();
		Set<AiTile> tilesWithinRadius = new HashSet<AiTile>();
		for (AiTile currentTile : this.getAccessibleTiles(givenTile)) {
			agent.checkInterruption();
			if (agent.getZone().getTileDistance(currentTile, givenTile) <= radius) tilesWithinRadius.add(currentTile);
		}
		return tilesWithinRadius;
	}

	/**
	 * Returns the tiles which are currently in danger.
	 * 
	 * @return 
	 * 		Dangerous tiles.
	 * @throws StopRequestException
	 * 		?
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
}
