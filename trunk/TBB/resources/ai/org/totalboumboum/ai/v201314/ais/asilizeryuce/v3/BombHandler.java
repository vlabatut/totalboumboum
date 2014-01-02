package org.totalboumboum.ai.v201314.ais.asilizeryuce.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class BombHandler extends AiBombHandler<Agent> {

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();

		boolean result = false;
		boolean dontPutBombe = false;
		AiHero nearestEnemy = ai.enemyHandler.getNearestEnemy();
		boolean closeEnemy = false;

		AiHero ownHero = ai.getZone().getOwnHero();
		int bombLeft = ownHero.getBombNumberMax()
				- ownHero.getBombNumberCurrent();
		AiTile destination = ai.moveHandler.getCurrentDestination();
		AiTile currentTile = ai.getZone().getOwnHero().getTile();

		int currentTilePreference = ai.preferenceHandler.getPreferencesByTile()
				.get(currentTile);

		if (nearestEnemy != null)
			if (ai.tileHandler.simpleTileDistance(ownHero.getTile(), nearestEnemy.getTile()) < 4)
				closeEnemy = true;
		
		if (ai.currentPath != null)
			if (ai.currentPath.getLength() > 1 && closeEnemy) {
				if (tileCanBeCorridor(ai.nextTile)) {
					dontPutBombe = true;
				}
			}

		// if there is already a bomb in this tile return false
				if (currentTile.getBombs().isEmpty() && bombLeft != 0) {

					if (canReachSafety(ownHero.getTile())) {
						// astar approximate icin
						if (ai.moveHandler.nextTileHasBlock == true) {
							ai.moveHandler.nextTileHasBlock = false;
							return true;
						}

						// preference is 0 and tile is our destination
						if (currentTilePreference == 0
								&& currentTile.equals(destination) && !dontPutBombe) {
							// System.out.println("klasik koridor ");
							return true;
						}

						// hurry up kategorisi icin
						if (currentTilePreference == 12 || currentTilePreference == 13
								&& currentTile.equals(destination)) {
							// System.out.println("hurry up " );
							return true;
						}

						else {
							// if any of the neighbors of tile tile has an ennemy in a
							// corridor
							for (AiTile tile : currentTile.getNeighbors()) {
								ai.checkInterruption();

								if (ennemyInCorridor(tile) == true /*
																	 * &&
																	 * canReachSafety(
																	 * ownHero
																	 * .getTile())
																	 */) {
									return true;
								}
							}
						}
					}
				}

				// System.out.println("bomba koyiim mi" +result);
				return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}

	/**
	 * c'est une method qui construit une liste des cases qui contiennent des
	 * ennemis
	 * 
	 * @return la liste des cases qui contiennent des ennemis
	 */
	public ArrayList<AiTile> getEnnemyTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> ennemyTiles = new ArrayList<AiTile>();

		for (AiTile tiles : ai.enemyHandler.enemyTiles()) {
			ai.checkInterruption();
			ennemyTiles.add(tiles);
		}

		return ennemyTiles;
	}


	/**
	 * dans cette methode, on crée une simulation de la zone avec AiSimZone puis on examine
	 * la case ou on pense de poser une bombe et regarde c'est sure ou dangeruse
	 * @param tile c'est la case ou on se trouve
	 * @return s'il y a une case sure, retourne true, false sinon
	 */
	public boolean canReachSafety(AiTile tile) {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiSimZone simzone = new AiSimZone(zone);
		AiSimBomb simbomb = simzone.createBomb(tile, simzone.getOwnHero());
		List<AiTile> indangerTiles = simbomb.getBlast();
		AiTile safeDestination = null;

		for (AiTile aiTile : ai.tileHandler.reacheableTiles) {
			ai.checkInterruption();
			
			if (!indangerTiles.contains(aiTile) && !ai.tileHandler.dangerousTiles().contains(aiTile)) {
				
				safeDestination = aiTile;	
				break;
			}
			
		}

		if (safeDestination == null)
			return false;

		else
			return true;

	}

	/**
	 * on essaye de savoir si une case peut etre dans une formation d'un couloir
	 * 
	 * @param aiTile
	 *            qu'on veut savoir si cette case forme un couloir, c'est la
	 *            case on examiner
	 * @return si cette case forme un coulior, cette methode retourne true,
	 *         sinon false
	 */
	public boolean tileCanBeCorridor(AiTile aiTile) {
		ai.checkInterruption();
		int i = 0;

		for (AiTile tile : aiTile.getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBlocks().isEmpty())
				i++;
			if (!tile.getBombs().isEmpty())
				i++;
		}

		if (i > 1)
			return true;
		else
			return false;
	}

	/**
	 * cette methode essaye de trouver s'il ya un couloir, est-ce qu'il y a une
	 * ennemi dans lui. on aussi controle si notre agent dans cet couloir.
	 * 
	 * @param aiTile
	 *            est la case qu'on examine
	 * @return retourne true s'il ya une ennemi et s'il notre agent n'est pas
	 *         dans cet couloir, retourne false sinon
	 */
	public boolean ennemyInCorridor(AiTile aiTile) {
		ai.checkInterruption();
		int i = 0;

		for (AiTile tile : aiTile.getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBlocks().isEmpty())
				i++;
			if (!tile.getBombs().isEmpty())
				i++;
		}

		if (i > 2
				&& (!aiTile.getHeroes().isEmpty() && ai.enemyHandler.enemyTiles().contains(aiTile)))
			return true;
		else
			return false;
	}

}
