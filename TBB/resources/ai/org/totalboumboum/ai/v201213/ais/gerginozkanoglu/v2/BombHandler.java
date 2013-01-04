package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
public class BombHandler extends AiBombHandler<GerginOzkanoglu> {
	/**
	 * Base limit of number of destructible walls.
	 */
	private static int LIMIT_DEST_WALL = 0;

	/**
	 * 
	 * @param ai
	 * 
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(GerginOzkanoglu ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calculate = new TileCalculation(this.ai);
		PathCalculation pc = new PathCalculation(this.ai);
		AiTile ourTile = this.ai.getZone().getOwnHero().getTile();
		if (calculate.isThereBomb(ourTile)) // if there is a bomb in our tile.
		{
			return false;
		} else // if there is no bomb in our tile.
		{

			AiTile closestAndSafestTile = calculate
					.closestAndSafestTileAfterBombing(ourTile, this.ai
							.getZone().getOwnHero().getBombRange());
			// common calculations
			if (closestAndSafestTile != null
					&& !closestAndSafestTile.equals(ourTile))

			{
				if (this.ai.getModeHandler().getMode().equals(AiMode.ATTACKING)) // if
				// mode
				// is
				// attacking
				{
					if (!calculate.threatenEnemy(ourTile, this.ai.getZone()
							.getOwnHero().getBombRange())
							&& this.ai.getZone().getItems().isEmpty()
							&& !this.ai.getZone().getDestructibleBlocks()
									.isEmpty()) {
						if (calculate.numberOfDestructibleWalls(ourTile) > LIMIT_DEST_WALL
								&& ourTile.getBombs().isEmpty()) {
							try {
								AiPath pathToSafestTile = pc.bestPath(this.ai
										.getZone().getOwnHero(),
										closestAndSafestTile);
								if (pc.isPathSafe(pathToSafestTile))
									return true;
								else
									return false;
							} catch (NullPointerException e) {
								return false;
							}
						} else
							return false;
					} else if (calculate.threatenEnemy(ourTile, this.ai
							.getZone().getOwnHero().getBombRange())) {
						try {
							AiPath pathToSafestTile = pc.bestPath(this.ai
									.getZone().getOwnHero(),
									closestAndSafestTile);
							if (pc.isPathSafe(pathToSafestTile))
								return true;
							else
								return false;
						} catch (NullPointerException e) {
							return false;
						}
					} else
						return false;

				} else // if mode is collecting
				{
					if (calculate.threatenEnemy(ourTile, this.ai.getZone()
							.getOwnHero().getBombRange())) {
						try {
							AiPath pathToSafestTile = pc.bestPath(this.ai
									.getZone().getOwnHero(),
									closestAndSafestTile);
							if (pc.isPathSafe(pathToSafestTile))
								return true;
							else
								return false;
						} catch (NullPointerException e) {
							return false;
						}
					} else {
						if (calculate.numberOfDestructibleWalls(ourTile) > LIMIT_DEST_WALL) {
							try {
								AiPath pathToSafestTile = pc.bestPath(this.ai
										.getZone().getOwnHero(),
										closestAndSafestTile);
								if (pc.isPathSafe(pathToSafestTile))
									return true;
								else
									return false;
							} catch (NullPointerException e) {
								return false;
							}
						} else
							return false;
					}

				}
			} else
				// common calculations : non-secure.
				return false;
		}

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}
}