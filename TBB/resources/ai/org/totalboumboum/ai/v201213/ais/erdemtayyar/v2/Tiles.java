package org.totalboumboum.ai.v201213.ais.erdemtayyar.v2;

import java.util.ArrayList;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * 
 * This class is aimed to contain useful tile operations. This class's methods
 * 
 * need an AI to operate (get zone etc.).
 * 
 * 
 * 
 * @author Banu Erdem
 * 
 * @author Zübeyir Tayyar
 */

public class Tiles {

	// FIELD

	/** Our IA */

	private ErdemTayyar ai;

	/**
	 * 
	 * Range's bottom limit, used in determining the potentially dangerous tiles
	 * 
	 * on bomb put.
	 */

	public final int RANGE_BOTTOM_LIMIT = 0;

	/**
	 * 
	 * Distance's bottom limit, if distance drops to this limit, the fire's edge
	 * 
	 * is reached in {@link #getDangerousTilesOnBombPut(AiTile givenTile,
			int range)}.
	 */


	public final int DISTANCE_BOTTOM_LIMIT = 0;

	/**
	 * 
	 * We replace accecible tiles in an arraylist
	 */

	private ArrayList<AiTile> accessibleTiles;

	// CONSTRUCTOR

	/**
	 * 
	 * @param ai
	 * 
	 *            Give the AI.
	 * 
	 * 
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	public Tiles(ErdemTayyar ai) throws StopRequestException

	{

		ai.checkInterruption();

		this.ai = ai;

	}

	/**
	 * 
	 * Populates a list of dangerous tiles of this AI's zone. <br />
	 * 
	 * (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @return List of the dangerous tiles.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	public ArrayList<AiTile> getCurrentDangerousTiles()
			throws StopRequestException

	{

		ai.checkInterruption();

		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		

		for (AiBomb currentBomb : this.ai.getZone().getBombs())

		{

			ai.checkInterruption();

			dangerousTiles.add(currentBomb.getTile());

			for (AiTile currentTile : currentBomb.getBlast())
			{

				ai.checkInterruption();

				dangerousTiles.add(currentTile);

			}
			

		}
		

		for (AiFire currentFire : this.ai.getZone().getFires())

		{

			ai.checkInterruption();

			dangerousTiles.add(currentFire.getTile());

		}
		return dangerousTiles;

	}

	/**
	 * 
	 * To get the Accessible tiles of this AI's own hero. (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @return List of all tiles that accessible by this object's AI's own hero.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected ArrayList<AiTile> getAccessibleTiles()
			throws StopRequestException

	{

		ai.checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();

		fillAccessibleTilesBy(this.ai.getHero().getTile());

		return this.accessibleTiles;

	}

	/**
	 * 
	 * Recursive method to fill a list of accessible tiles.
	 * 
	 * 
	 * 
	 * @param sourceTile
	 * 
	 *            The tile to start looking from. If not crossable, list will
	 * 
	 *            not be populated.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	private void fillAccessibleTilesBy(AiTile sourceTile)
			throws StopRequestException

	{

		ai.checkInterruption();

		if (sourceTile.isCrossableBy(this.ai.getHero()))

		{

			this.accessibleTiles.add(sourceTile);

			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(
					this.ai.getHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));

			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(
					this.ai.getHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));

			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(
					this.ai.getHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));

			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(
					this.ai.getHero())
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));

		}

	}

	/**
	 * 
	 * Finds the closest accessible safe tile to the given tile. Uses
	 * 
	 * "Manhattan Distance" to calculate the distances. <br />
	 * 
	 * (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @param givenTile
	 * 
	 *            The tile to operate.
	 * 
	 * @param checkRadius
	 * 
	 *            Radius to check.
	 * 
	 * @return Closest safe tile to the given tile.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected AiTile getClosestSafeTile(AiTile givenTile, int checkRadius)
			throws StopRequestException

	{

		ai.checkInterruption();

		int distance = Integer.MAX_VALUE;

		AiTile result = null;

		for (AiTile currentTile : this.getAccessibleTilesWithinRadius(
				givenTile, checkRadius))

		{

			ai.checkInterruption();

			if (!this.getCurrentDangerousTiles().contains(currentTile)
					&& distance > this.ai.getZone().getTileDistance(givenTile,
							currentTile))

			{

				distance = this.ai.getZone().getTileDistance(givenTile,
						currentTile);

				result = currentTile;

			}

		}

		return result;

	}

	/**
	 * 
	 * Returns all accessible tiles that within given radius. Uses
	 * 
	 * "Manhattan Distance" to calculate distances, A* is too slow for this. <br />
	 * 
	 * (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @param givenTile
	 * 
	 *            The center tile.
	 * 
	 * @param radius
	 * 
	 *            Max radius to look.
	 * 
	 * @return List of the tiles within the circle with the center as the given
	 * 
	 *         tile and the given radius .
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	public ArrayList<AiTile> getAccessibleTilesWithinRadius(AiTile givenTile,
			int radius) throws StopRequestException

	{

		ai.checkInterruption();

		ArrayList<AiTile> tilesWithinRadius = new ArrayList<AiTile>();

		for (AiTile currentTile : this.getAccessibleTiles(givenTile))

		{

			ai.checkInterruption();

			if (this.ai.getZone().getTileDistance(currentTile, givenTile) <= radius)
				tilesWithinRadius.add(currentTile);

		}

		return tilesWithinRadius;

	}

	/**
	 * 
	 * To get the accessible tiles from a given source tile. (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @param sourceTile
	 * 
	 *            Tile to start looking from.
	 * 
	 * @return List of all tiles that accessible from a given source tile.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected ArrayList<AiTile> getAccessibleTiles(AiTile sourceTile)
			throws StopRequestException

	{

		ai.checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();

		fillAccessibleTilesBy(sourceTile);

		return this.accessibleTiles;

	}

	/**
	 * 
	 * Finds the closest accessible safe tile to this AI's own hero's tile. Uses
	 * 
	 * "Manhattan Distance" to calculate the distances. <br />
	 * 
	 * (TESTED, WORKS)
	 * 
	 * 
	 * 
	 * @param checkRadius
	 * 
	 *            Radius to check.
	 * 
	 * @return Closest safe tile to this AI's hero.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	protected AiTile getClosestSafeTile(int checkRadius)
			throws StopRequestException

	{

		ai.checkInterruption();

		return this
				.getClosestSafeTile(this.ai.getHero().getTile(), checkRadius);

	}

	/**
	 * 
	 * Populates a list of tiles which will become dangerous if a bomb is put on
	 * 
	 * given tile. <br/>
	 * 
	 * (TESTED, WORKS) <br/>
	 * 
	 * At the beginning of the game, the ranges of the heroes can be
	 * 
	 * uninitialized. So, be careful when giving range parameter from a hero's
	 * 
	 * range. <br/>
	 * 
	 * If range is 0, this method will return an empty list.
	 * 
	 * 
	 * 
	 * @param givenTile
	 * 
	 *            The tile which will contain the potential bomb.
	 * 
	 * @param range
	 * 
	 *            The explosion range of the potential bomb.
	 * 
	 * @return The list of potentially dangerous tiles on bomb put.
	 * 
	 * @throws StopRequestException
	 * 
	 *             If the engine demands the termination of the agent.
	 */

	public ArrayList<AiTile> getDangerousTilesOnBombPut(AiTile givenTile,
			int range) throws StopRequestException

	{

		ai.checkInterruption();

		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();

		if (givenTile.isCrossableBy(this.ai.getHero())
				&& (range > RANGE_BOTTOM_LIMIT))

		{

			dangerousTilesOnBombPut.add(givenTile);

			AiTile currentTile = givenTile.getNeighbor(Direction.LEFT);

			int distance = range;

			while (currentTile.isCrossableBy(this.ai.getHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT))

			{

				ai.checkInterruption();

				dangerousTilesOnBombPut.add(currentTile);

				if (!currentTile.getItems().isEmpty())
					break;

				currentTile = currentTile.getNeighbor(Direction.LEFT);

				distance--;

			}

			currentTile = givenTile.getNeighbor(Direction.RIGHT);

			distance = range;

			while (currentTile.isCrossableBy(this.ai.getHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT))

			{

				ai.checkInterruption();

				dangerousTilesOnBombPut.add(currentTile);

				if (!currentTile.getItems().isEmpty())
					break;

				currentTile = currentTile.getNeighbor(Direction.RIGHT);

				distance--;

			}

			currentTile = givenTile.getNeighbor(Direction.UP);

			distance = range;

			while (currentTile.isCrossableBy(this.ai.getHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT))

			{

				ai.checkInterruption();

				dangerousTilesOnBombPut.add(currentTile);

				if (!currentTile.getItems().isEmpty())
					break;

				currentTile = currentTile.getNeighbor(Direction.UP);

				distance--;

			}

			currentTile = givenTile.getNeighbor(Direction.DOWN);

			distance = range;

			while (currentTile.isCrossableBy(this.ai.getHero())
					&& (distance > DISTANCE_BOTTOM_LIMIT))

			{

				ai.checkInterruption();

				dangerousTilesOnBombPut.add(currentTile);

				if (!currentTile.getItems().isEmpty())
					break;

				currentTile = currentTile.getNeighbor(Direction.DOWN);

				distance--;

			}

		}

		return dangerousTilesOnBombPut;

	}

}