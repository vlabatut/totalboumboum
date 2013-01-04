package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

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
		AiTile ourTile = this.ai.getZone().getOwnHero().getTile();
		if (calculate.isThereBomb(ourTile)) // if there is a bomb in our tile.
		{
			return false;
		} else // if there is no bomb in our tile.
		{ // AiTile tile =
			// calculate.ClosestAndSafestTileAfterBombing(ourTile,this.ai.getZone().getOwnHero().getBombRange());
			// la variable tile est pour le debougage, pour bien voir si notre agent trouve le case
			// secure et proche.
			
			//common calculations
			if (calculate.closestAndSafestTileAfterBombing(ourTile, this.ai
					.getZone().getOwnHero().getBombRange()) != null
					&& !calculate.closestAndSafestTileAfterBombing(ourTile,
							ai.getZone().getOwnHero().getBombRange()).equals(
							ourTile)) 
			
			{
				if (this.ai.getModeHandler().getMode().equals(AiMode.ATTACKING)) // if
																					// mode
																					// is
																					// attacking
				{
					if (calculate.threatenEnemy(ourTile, this.ai.getZone()
							.getOwnHero().getBombRange()))
						return true;
					else
						return false;

				} else // if mode is collecting
				{
					if (calculate.threatenEnemy(ourTile, this.ai.getZone()
							.getOwnHero().getBombRange()))
						return true;
					else {
						if (calculate.numberOfDestructibleWalls(ourTile) > LIMIT_DEST_WALL)
							return true;
						else
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
