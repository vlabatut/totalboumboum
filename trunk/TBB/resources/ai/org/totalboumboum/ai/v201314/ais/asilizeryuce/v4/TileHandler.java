package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant les calculs des cases de la zone.
 * 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class TileHandler extends AiAbstractHandler<Agent> {

	/**
	 * Construit un gestionnaire pour les cases.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	protected TileHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		this.accessibleTiles = new ArrayList<AiTile>();
		this.selectedTiles = new ArrayList<AiTile>();
		this.reacheableTiles = new ArrayList<AiTile>();
		this.suddenDeathTiles = getSuddenDeathTiles();
	}

	/**
	 * Le temps de faire le control pour mort subite
	 */
	private static final long SUDDEN_DEATH_CONTROL_TIME = 5000;

	/** cases accessibles, retourne une list de ces cases. */
	public ArrayList<AiTile> accessibleTiles;

	/** La liste des cases qui contient un mort subit. */
	public ArrayList<AiTile> suddenDeathTiles;

	/**
	 * on ajoute les cases qui contient d'ennemi dans les cases accessibles, si
	 * les cases accessibles ne contiennent pas d'ennemi c'est une liste et
	 * retourne une liste des cases accessibles avec des cases qui contiennent
	 * des ennemis
	 */
	public ArrayList<AiTile> selectedTiles;

	/**
	 * on utilise pour avoir une liste des cases accessibles qu'ils ont ete
	 * utilise dans la methode canReachSafety.
	 */
	public ArrayList<AiTile> reacheableTiles;

	/**
	 * un compteur pour controler la nombre des bombes pasées dans la critere
	 * triangle. il nous assure qu'on sort quand notre agent pose la derniere
	 * bombe.
	 */
	public int controlTriangleBombe = 0;

	/**
	 * Recursive method to fill a list of accessible tiles. For this list we
	 * treat like our agent can cross bombs and fires. We will use this list at
	 * MoveHandler to decide whether the path is direct or indirect.
	 * 
	 * @param sourceTile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 */
	public void fillAccessibleTilesBy(AiTile sourceTile) {
		ai.checkInterruption();
		AiHero hero = ai.getZone().getOwnHero();
		if (sourceTile.isCrossableBy(hero) || !sourceTile.getBombs().isEmpty()
				|| !sourceTile.getFires().isEmpty()) {
			this.accessibleTiles.add(sourceTile);

			if (sourceTile.getNeighbor(Direction.UP).getBlocks().isEmpty()
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));

			if (sourceTile.getNeighbor(Direction.DOWN).getBlocks().isEmpty()
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));

			if (sourceTile.getNeighbor(Direction.LEFT).getBlocks().isEmpty()
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));

			if (sourceTile.getNeighbor(Direction.RIGHT).getBlocks().isEmpty()
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}

	}

	/**
	 * Recursive method to fill a list of selected tiles. We select tiles which
	 * are crossable by our agent.
	 * 
	 * @param sourceTile
	 *            The tile to start looking from.
	 */
	public void fillSelectedTilesBy(AiTile sourceTile) {
		ai.checkInterruption();
		AiHero hero = ai.getZone().getOwnHero();
		if (sourceTile.isCrossableBy(hero)) {
			this.selectedTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}

	}

	/**
	 * To get the accessible tiles from a given source tile.
	 * 
	 * @param sourceTile
	 *            Tile to start looking from.
	 * @return List of all tiles that accessible from a given source tile.
	 */
	public ArrayList<AiTile> getAccessibleTilesFrom(AiTile sourceTile) {
		ai.checkInterruption();

		fillAccessibleTilesBy(sourceTile);

		return this.accessibleTiles;
	}

	/**
	 * cette methode prend les cases qui contiennent de flamme ou de bombe, et
	 * les met dans une liste
	 * 
	 * @return dangerousTiles c'est un liste concernant les cases qui
	 *         contiennent de bombe ou de flamme
	 */
	public ArrayList<AiTile> dangerousTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();

		for (AiBomb currentBomb : ai.getZone().getBombs()) {
			ai.checkInterruption();
			for (AiTile currentTile : currentBomb.getBlast()) {
				ai.checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}

		/* add sudden death tiles */
		if (suddenDeathTiles != null)
			for (AiTile tile : suddenDeathTiles) {
				ai.checkInterruption();
				dangerousTiles.add(tile);
			}

		for (AiFire currentFire : ai.getZone().getFires()) {
			ai.checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}

		return dangerousTiles;
	}

	/**
	 * pour calculer la distance entre deux cases
	 * 
	 * @param tile1
	 *            le tile depart
	 * @param tile2
	 *            le tile arrivé
	 * @return la distance entre deux cases
	 */
	public int simpleTileDistance(AiTile tile1, AiTile tile2) {
		ai.checkInterruption();
		return Math.abs(tile1.getCol() - tile2.getCol())
				+ Math.abs(tile1.getRow() - tile2.getRow());
	}

	/**
	 * C'est une methode pour trouver si la case passe a parametre est une case
	 * dans un couloir, et cette couloir contient un adversaire
	 * 
	 * @param tile
	 *            le case qu'on controle
	 * 
	 * @return true si la case est dans un couloir et il y a un adversaire dans
	 *         cette couloir
	 *         false sinon
	 */
	public boolean tileIsCorridor(AiTile tile) {
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();

		int bombRange = ownHero.getBombRange();
		int i = 0;
		boolean enemyIn = false;

		if (tile.isCrossableBy(ownHero) || tile.getBombs().isEmpty()) {
			AiTile leftNeighbour = tile;

			if (leftNeighbour.getNeighbor(Direction.LEFT)
					.isCrossableBy(ownHero)
					&& leftNeighbour.getNeighbor(Direction.LEFT).getItems()
							.isEmpty()) {

				for (i = 0; i < bombRange; i++) {
					ai.checkInterruption();

					leftNeighbour = leftNeighbour.getNeighbor(Direction.LEFT);

					if (ai.enemyHandler.enemyTiles().contains(leftNeighbour))
						enemyIn = true;

					/* leftNeigh is empty, up and down is blocked */
					if (leftNeighbour.getNeighbor(Direction.DOWN)
							.isCrossableBy(ownHero)
							|| leftNeighbour.getNeighbor(Direction.UP)
									.isCrossableBy(ownHero))
						break;

					// leftin lefti blok
					if (!leftNeighbour.getNeighbor(Direction.LEFT)
							.isCrossableBy(ownHero))
						if (enemyIn)
							return true;
						else
							break;
				}
			}

		}

		enemyIn = false;

		AiTile rightNeighbour = tile;

		if (rightNeighbour.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero)
				&& rightNeighbour.getNeighbor(Direction.RIGHT).getItems()
						.isEmpty()) {

			for (i = 0; i < bombRange; i++) {

				ai.checkInterruption();
				rightNeighbour = rightNeighbour.getNeighbor(Direction.RIGHT);

				if (ai.enemyHandler.enemyTiles().contains(rightNeighbour)) {
					// System.out.println("enemyIn " + rightNeighbour);
					enemyIn = true;
				}
				// leftNeigh bos, ustu ve altı blok
				if (rightNeighbour.getNeighbor(Direction.DOWN).isCrossableBy(
						ownHero)
						|| rightNeighbour.getNeighbor(Direction.UP)
								.isCrossableBy(ownHero))
					break;

				// leftin lefti blok
				if (!rightNeighbour.getNeighbor(Direction.RIGHT).isCrossableBy(
						ownHero))
					if (enemyIn)
						return true;
					else
						break;
			}
		}

		enemyIn = false;

		AiTile upNeighbour = tile;

		if (upNeighbour.getNeighbor(Direction.UP).isCrossableBy(ownHero)
				&& upNeighbour.getNeighbor(Direction.UP).getItems().isEmpty()) {

			for (i = 0; i < bombRange; i++) {
				ai.checkInterruption();
				upNeighbour = upNeighbour.getNeighbor(Direction.UP);

				if (ai.enemyHandler.enemyTiles().contains(upNeighbour))
					enemyIn = true;

				// if (leftNeighbour.isCrossableBy(ownHero) &&
				// leftNeighbour.getBombs().isEmpty()) {

				// leftNeigh bos, ustu ve altı blok
				if (upNeighbour.getNeighbor(Direction.LEFT).isCrossableBy(
						ownHero)
						|| upNeighbour.getNeighbor(Direction.RIGHT)
								.isCrossableBy(ownHero))
					break;

				// leftin lefti blok
				if (!upNeighbour.getNeighbor(Direction.UP).isCrossableBy(
						ownHero)) {
					if (enemyIn)
						return true;
					else
						break;
				}
			}
		}

		enemyIn = false;

		AiTile downNeighbour = tile;

		if (downNeighbour.getNeighbor(Direction.DOWN).isCrossableBy(ownHero)
				&& downNeighbour.getNeighbor(Direction.DOWN).getItems()
						.isEmpty()) {

			for (i = 0; i < bombRange; i++) {
				ai.checkInterruption();
				downNeighbour = downNeighbour.getNeighbor(Direction.DOWN);

				if (ai.enemyHandler.enemyTiles().contains(downNeighbour))
					enemyIn = true;

				// leftNeigh bos, ustu ve altı blok
				if (downNeighbour.getNeighbor(Direction.LEFT).isCrossableBy(
						ownHero)
						|| downNeighbour.getNeighbor(Direction.RIGHT)
								.isCrossableBy(ownHero))
					break;

				// leftin lefti blok
				if (!downNeighbour.getNeighbor(Direction.DOWN).isCrossableBy(
						ownHero)) {
					if (enemyIn) {
						// System.out.println("buldum4 " + downNeighbour);
						return true;
					} else
						break;

				}
			}
		}
		return false;
	}

	/**
	 * 
	 * Renvoie une liste de tile qui sont concernée de mort subite
	 * 
	 * @return ArrayListe: cases concernee de mort subite
	 * 
	 */
	public ArrayList<AiTile> getSuddenDeathTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> result = new ArrayList<AiTile>();

		if (!ai.getZone().getAllSuddenDeathEvents().isEmpty()) {
			for (AiSuddenDeathEvent suddenDeath : ai.getZone()
					.getAllSuddenDeathEvents()) {
				ai.checkInterruption();
				if (suddenDeath.getTime() < ai.getZone().getTotalTime()
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
}
