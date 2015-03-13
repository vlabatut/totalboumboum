package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;

import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<GerginOzkanoglu> {

	/**
	 * next location.
	 */
	private static int NEXT_LOCATION = 1;
	/**
	 * first location.
	 */
	private static int FIRST_LOCATION = 0;

	/**
	 * current destination for using in bomb handler.
	 */
	protected AiTile currentDest;

	/**
	 * 
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(GerginOzkanoglu ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiTile updateCurrentDestination() throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calculate = new TileCalculation(this.ai);
		AiTile biggestTile = calculate.mostValuableTile();
		AiUtilityHandler<GerginOzkanoglu> uh = this.ai.getUtilityHandler();
		Map<AiTile, Float> utilities = uh.getUtilitiesByTile();

		if (utilities.containsKey(currentDestination)) {
			if (ai.getZone().getOwnHero().getTile().equals(currentDestination)) {
				this.currentDest = biggestTile;
				return biggestTile;
			} else if (1.2 * utilities.get(currentDestination) >= utilities
					.get(biggestTile)) {
				this.currentDest = currentDestination;
				return this.getCurrentDestination();

			}
		}

		this.currentDest = biggestTile;
		return biggestTile;

	}

	// ///////////////////////////////////////////////////////////////
	// PATH /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected AiPath updateCurrentPath() throws StopRequestException {
		ai.checkInterruption();
		PathCalculation pathOperation = new PathCalculation(this.ai);
		AiPath path = pathOperation.bestPath(this.ai.getZone().getOwnHero(),
				currentDestination);
		return path;
	}

	// ///////////////////////////////////////////////////////////////
	// DIRECTION /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction updateCurrentDirection() throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calculate = new TileCalculation(this.ai);
		PathCalculation pathCalculation = new PathCalculation(this.ai);
		AiTile mostValTile = currentDestination;
		// if we are in most valuable tile and most valuable tile is safe
		if (mostValTile.equals(this.ai.getZone().getOwnHero().getTile())
				&& !calculate.isDangerous(mostValTile)) {
			return Direction.NONE;
		}
		// if there is bomb in our tile-->run
		else if (calculate
				.isThereBomb(this.ai.getZone().getOwnHero().getTile())) {
			AiTile safestTile = calculate.closestAndSafestTileAfterBombing(
					this.ai.getZone().getOwnHero().getTile(), this.ai.getZone()
							.getOwnHero().getBombRange());

			// if there is safest tile--> go there
			if (safestTile != null) {

				return pathCalculation.getNextDirectionOnPath(safestTile);

			}
			// if there is not safest tile --> go to most valuable tile
			else {
				AiPath path = currentPath;
				AiLocation ourLocation = new AiLocation(this.ai.getZone()
						.getOwnHero().getTile());
				try {
					if (path != null && path.getLength() > NEXT_LOCATION) {
						AiLocation location = path.getLocation(NEXT_LOCATION);
						if (pathCalculation.isNextTileOnPathSafe(location
								.getTile()))
							return this.ai.getZone().getDirection(ourLocation,
									path.getLocation(NEXT_LOCATION));
						else
							return Direction.NONE;
					} else
						return Direction.NONE;
				} catch (IndexOutOfBoundsException e) {
					// our agent will die in this case
					return this.ai.getZone().getDirection(ourLocation,
							path.getLocation(FIRST_LOCATION));
				} catch (NullPointerException e) {
					return this.ai.getZone().getDirection(ourLocation,
							path.getLocation(FIRST_LOCATION));
				}
			}

		}
		// if we are not in the most valuable tile or most valuable tile is not
		// safe and there is not a bomb in our tile-->we must go to the most
		// valuable tile
		else {

			try {
				AiPath path = currentPath;
				AiLocation location = new AiLocation(this.ai.getZone()
						.getOwnHero().getTile());
				if (path == null || path.getLocations().size() == 1)
					path = pathCalculation.bestPath(this.ai.getZone()
							.getOwnHero(), calculate
							.closestAndSafestTileForHero(this.ai.getZone()
									.getOwnHero()));
				if (path == null || path.isEmpty())
					return Direction.NONE;
				else if (path.getLength()>NEXT_LOCATION && !pathCalculation.isNextTileOnPathSafe(path.getLocation(NEXT_LOCATION).getTile())) 
				{	// the tile is in danger.
					// go shortest and safest tile
					AiPath newPath = pathCalculation.bestPath(this.ai.getZone().getOwnHero(), calculate.closestAndSafestTileForHero(this.ai.getZone().getOwnHero()));
					if (newPath != null
							&& newPath.getLength()>NEXT_LOCATION
							&& pathCalculation.isNextTileOnPathSafe(newPath.getLocation(NEXT_LOCATION).getTile()))
						return this.ai.getZone().getDirection(location,newPath.getLocation(NEXT_LOCATION));
					else
						return Direction.NONE;

				} else {
					if(path.getLength()>NEXT_LOCATION)
						return this.ai.getZone().getDirection(location,path.getLocation(NEXT_LOCATION));
					else
						return Direction.NONE;

				}

			} catch (IndexOutOfBoundsException e) {
				return Direction.NONE;
			} catch (NullPointerException e) {
				return Direction.NONE;
			}
		}
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		super.updateOutput();
	}
}
