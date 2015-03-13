package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant les calculs liée aux cases.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class TileCalculationHandler extends AiAbstractHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;

	/** la liste des cases accessibles dans la zone */
	public ArrayList<AiTile> reachableTiles;
	/** le temps de faire le control pour mort subite */
	private static final long SUDDEN_DEATH_CONTROL_TIME = 3500;
	/** pour utiliser les methodes qui est dans preferenceHandler */
	PreferenceHandler preferenceHandler = ai.preferenceHandler;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected TileCalculationHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ourHero = zone.getOwnHero();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Methode pour pouvoir calculer les cases dangereux.
	 * 
	 * @return La liste des cases qui ont une potentielle d'être dangereux.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();

		for (AiBomb currentBomb : zone.getBombs()) {
			ai.checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				ai.checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : zone.getFires()) {
			ai.checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}

		return dangerousTiles;
	}

	/**
	 * Methode pour calculer les cases accessibles.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 */
	private void fillAccessibleTilesBy(AiTile tile) {
		ai.checkInterruption();

		ourHero = zone.getOwnHero();
		if (tile.isCrossableBy(ourHero)) {
			reachableTiles.add(tile);
			if (tile.getNeighbor(Direction.UP).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.UP));
			if (tile.getNeighbor(Direction.DOWN).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.DOWN));
			if (tile.getNeighbor(Direction.LEFT).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.LEFT));
			if (tile.getNeighbor(Direction.RIGHT).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.RIGHT));
		}
	}

	/**
	 * Methode pour envoyer les cases accessibles.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return La liste des cases accessibles.
	 */
	protected ArrayList<AiTile> getReachableTiles(AiTile tile) {
		ai.checkInterruption();
		reachableTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy(tile);

		return reachableTiles;
	}

	/**
	 * Methode pour calculer les cases secures.
	 * 
	 * @return Cette méthode retourne la case secure.
	 */
	public AiTile getSecureTile() {
		ai.checkInterruption();
		

		AiTile secureTile = null;
		ourHero = zone.getOwnHero();
		Map<AiTile, Integer> pref = preferenceHandler.getPreferencesByTile();
		List<AiTile> rTiles = getReachableTiles(ourHero.getTile());
		AiTile tempTile = null;
		int minPreference = Integer.MAX_VALUE;
		
		int preference = 0;
		
		for (Entry<AiTile, Integer> entry : pref.entrySet()) {
			ai.checkInterruption();
			tempTile = entry.getKey();

			if (rTiles.contains(tempTile)) {
				preference = entry.getValue();
				if (minPreference >= preference) {
					minPreference = preference;
				}
			}
		}
		List<AiTile> tiles = new ArrayList<AiTile>();
		for (Entry<AiTile, Integer> entry : pref.entrySet()) {
			ai.checkInterruption();
			preference = entry.getValue();
			tempTile = entry.getKey();
			if (rTiles.contains(tempTile))
				if (preference == minPreference) {
					tiles.add(tempTile);
					secureTile = tempTile;
				}
		}
		if (tiles.contains(ourHero.getTile()))
			secureTile = ourHero.getTile();

		return secureTile;
	}

	/**
	 * Methode pour calculer l'effet mort subit.
	 * 
	 * @return Retourne la liste des cases qui ont l'effet mort subit.
	 */
	public ArrayList<AiTile> getSuddenDeathTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> result = new ArrayList<AiTile>();

		if (!zone.getAllSuddenDeathEvents().isEmpty()) {
			for (AiSuddenDeathEvent suddenDeath : zone
					.getAllSuddenDeathEvents()) {
				ai.checkInterruption();
				if (suddenDeath.getTime() < zone.getTotalTime()
						+ SUDDEN_DEATH_CONTROL_TIME) {

					for (AiTile tile : suddenDeath.getTiles()) {

						ai.checkInterruption();
						result.add(tile);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Methode pour aider les items cachees. S'il y a plusieurs murs
	 * destructibles autours d'une case, alors il y a plus de chances pour
	 * trouver un item cachée.
	 * 
	 * @param sourceTile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Le nombre de murs destructibles autour de la case.
	 */
	public int getNbMurDetruitofTile(AiTile sourceTile) {
		ai.checkInterruption();
		int result = 0;
		AiTile tileUp = sourceTile.getNeighbor(Direction.UP);
		AiTile tileDown = sourceTile.getNeighbor(Direction.DOWN);
		AiTile tileLeft = sourceTile.getNeighbor(Direction.LEFT);
		AiTile tileRight = sourceTile.getNeighbor(Direction.RIGHT);
		int i = 1;
		int bombRange = zone.getOwnHero().getBombRange();
		// obstacles sont pour terminer la recherce de murs quand on se
		// rencontre des murs.
		boolean[] obstacle = { true, true, true, true, true };

		while (obstacle[4] && (i <= bombRange)) {
			ai.checkInterruption();

			List<AiBlock> blocks;
			if (obstacle[0]) {
				blocks = tileUp.getBlocks();
				if (!tileUp.getItems().isEmpty())
					obstacle[0] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}

				tileUp = tileUp.getNeighbor(Direction.UP);
			}
			if (obstacle[1]) {
				blocks = tileDown.getBlocks();
				if (!tileDown.getItems().isEmpty())
					obstacle[1] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
				tileDown = tileDown.getNeighbor(Direction.DOWN);
			}
			if (obstacle[2]) {
				blocks = tileLeft.getBlocks();
				if (!tileLeft.getItems().isEmpty())
					obstacle[2] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
				tileLeft = tileLeft.getNeighbor(Direction.LEFT);
			}
			if (obstacle[3]) {
				blocks = tileRight.getBlocks();
				if (!tileRight.getItems().isEmpty())
					obstacle[3] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						ai.checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
				tileRight = tileRight.getNeighbor(Direction.RIGHT);
			}
			if ((!obstacle[0]) && (!obstacle[1])
					&& (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		if (result > 4)
			result = 4;

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
}
