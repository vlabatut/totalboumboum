package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
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


	/** C'est une destination qui est sure pour nous */
	public AiTile safeDestinaton = null;

	/**c'est notre IA */
	public AiHero ownHero = ai.getZone().getOwnHero();

	/**c'est pour creer astar dans AiSimZone*/
	public Astar simAstar = null;

	
	

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();

		boolean result = false;
		AiTile leftNeig = null;
		AiTile rightNeig = null;
		
		int bombLeft = ownHero.getBombNumberMax()
				- ownHero.getBombNumberCurrent();
		AiTile destination = ai.moveHandler.getCurrentDestination();
		AiTile currentTile = ai.getZone().getOwnHero().getTile();

		int currentTilePreference = ai.preferenceHandler.getPreferencesByTile()
				.get(currentTile);
		
		// if there is already a bomb in this tile return false
		if (currentTile.getBombs().isEmpty() && bombLeft != 0) {

			if (currentTilePreference == 12 || currentTilePreference == 13
					&& currentTile.equals(destination)) {
				return true;
			}

			if (ai.moveHandler.targetDetected
					&& ai.moveHandler.targetTile.equals(currentTile)) {
				return true;
			}

			if (ai.moveHandler.heroIsDead) {
				ai.moveHandler.heroIsDead = false;
				return true;
			}

			if (canReachSafety(ownHero.getTile())) {

				
				leftNeig = ownHero.getTile().getNeighbor(Direction.LEFT);
				rightNeig = ownHero.getTile().getNeighbor(Direction.RIGHT);

				// if (ai.enemyHandler.enemyTiles().size() == 1 ) {
				if (!leftNeig.getNeighbor(Direction.UP).isCrossableBy(ownHero)
						&& ai.enemyHandler.enemyTiles().contains(
								leftNeig.getNeighbor(Direction.UP))
						|| !leftNeig.getNeighbor(Direction.DOWN).isCrossableBy(
								ownHero)
						&& ai.enemyHandler.enemyTiles().contains(
								leftNeig.getNeighbor(Direction.DOWN))) {
					return true;
				}

				if (ai.enemyHandler.enemyTiles().contains(rightNeig))
					if (!rightNeig.getNeighbor(Direction.UP).isCrossableBy(
							ownHero)
							&& ai.enemyHandler.enemyTiles().contains(
									rightNeig.getNeighbor(Direction.UP))
							|| !rightNeig.getNeighbor(Direction.DOWN)
									.isCrossableBy(ownHero)
							&& ai.enemyHandler.enemyTiles().contains(
									rightNeig.getNeighbor(Direction.DOWN))) {
						return true;
					}
				
				/* astar approximate */
				if (ai.moveHandler.nextTileHasBlock == true) {

					ai.moveHandler.nextTileHasBlock = false;

					return true;
				}

				if (ai.tileHandler.tileIsCorridor(ownHero.getTile())) {
					return true;
				}

				if (currentTilePreference == 18) {
					return true;
				}

				// demi triangle
				if (currentTilePreference == 18) {
					return true;
				}
				if (currentTilePreference == 19) {
					ai.tileHandler.controlTriangleBombe++;
					return true;
				}


				// // preference is 0 and tile is our destination
				if (ai.enemyHandler.enemyTiles().size() == 1
						&& currentTilePreference == 0) {
					return true;
				}
			}
		}
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
	 * dans cette methode, on crée une simulation de la zone avec AiSimZone puis
	 * on examine la case ou on pense de poser une bombe et regarde c'est sure
	 * ou dangereuse
	 * 
	 * @param tile
	 *            c'est la case ou on se trouve
	 * @return s'il y a une case sure, retourne true, false sinon
	 */
	public boolean canReachSafety(AiTile tile) {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiSimZone simzone = new AiSimZone(zone);
		AiSimBomb simbomb = simzone.createBomb(tile, simzone.getOwnHero());
		List<AiTile> indangerTiles = simbomb.getBlast();
		safeDestinaton = null;
		AiPath path = null;
		AiHero simHero = simzone.getOwnHero();
		AiLocation currentLocation = new AiLocation(ownHero);

		TimeCostCalculator simTimeCostCalculator = new TimeCostCalculator(ai,
				simHero);
		TimeHeuristicCalculator simHeuristicCalculator = new TimeHeuristicCalculator(
				ai, simHero);
		TimePartialSuccessorCalculator simTimeSuccessorCalculator = new TimePartialSuccessorCalculator(
				ai, SearchMode.MODE_ONEBRANCH);

		simAstar = new Astar(ai, simHero, simTimeCostCalculator,
				simHeuristicCalculator, simTimeSuccessorCalculator);

		for (AiTile aiTile : ai.tileHandler.reacheableTiles) {
			ai.checkInterruption();

			if (!indangerTiles.contains(aiTile)
					&& !ai.tileHandler.dangerousTiles().contains(aiTile)
					&& aiTile.getItems().isEmpty()) {
				safeDestinaton = aiTile;
				break;

			}

		}

		if (safeDestinaton == null)
			return false;

		else {
			try {
				path = simAstar.startProcess(currentLocation, safeDestinaton);
			} catch (LimitReachedException e) {
				//
			}
		}

		if (path != null) {
			return true;
		} else
			return false;

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
				&& (!aiTile.getHeroes().isEmpty() && ai.enemyHandler
						.enemyTiles().contains(aiTile)))
			return true;
		else
			return false;
	}

}
