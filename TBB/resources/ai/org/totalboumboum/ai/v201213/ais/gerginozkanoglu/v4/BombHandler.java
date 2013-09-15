package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<GerginOzkanoglu> {
	
	/**
	 * limit of malus count around our tile
	 */
	private static int LIMIT_MALUS_COUNT = 0;
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
		
		if(this.ai.getModeHandler().getMode().equals(AiMode.COLLECTING))
		{
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
							if (calculate.numberOfDestructibleWalls(ourTile) > LIMIT_DEST_WALL || calculate.malusItemsCount(ourTile) > LIMIT_MALUS_COUNT) {
								try {
									AiPath pathToSafestTile = pc.bestPath(this.ai
											.getZone().getOwnHero(),
											closestAndSafestTile);
									if (pathToSafestTile!=null && pc.isPathSafe(pathToSafestTile))
										return true;
									else
										return false;
								} catch (NullPointerException e) {
									return false;
								}
							} else
								return false;
						}

					
				} else
					// common calculations : non-secure.
					return false;
			}

		}
		else // attacking mode
		{
			if ( ourTile.equals( calculate.mostValuableTile()) && !calculate.isDangerous(ourTile) && calculate.canReachSafety(this.ai.getZone().getOwnHero()))
			{
				boolean enemyAccessible = false;
				for(AiHero hero : this.ai.getZone().getRemainingOpponents())
				{
					ai.checkInterruption();
					if(calculate.allAccesibleTiles(ourTile).contains(hero.getTile()))
					{
						enemyAccessible = true;
						break;
					}
				}
				if(!enemyAccessible)
				{
					boolean fireControl = false;
					for(AiTile tile : calculate.allAccesibleTiles(ourTile))
					{
						ai.checkInterruption();
						if(!tile.getFires().isEmpty())
						{
							fireControl = true;
							break;
						}
					}
					if(this.ai.getZone().getBombsByColor(PredefinedColor.PINK).isEmpty() && calculate.mostValuableTile().equals(ourTile) &&!fireControl && calculate.closestAndSafestTileAfterBombing(ourTile, this.ai.getZone().getOwnHero().getBombRange()) != null)
						return true;
					else
						return false;
				}
				else{
					
					if(calculate.threatenEnemy(ourTile, this.ai.getZone().getOwnHero().getBombRange())  && calculate.closestAndSafestTileAfterBombing(ourTile, this.ai.getZone().getOwnHero().getBombRange()) != null)
						return true;
					else
						return false;
				}
			}
			else
			{
				return false;
			}
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