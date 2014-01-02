package org.totalboumboum.ai.v201314.ais.asilizeryuce.v3;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe pour classer et calculer les cases des zones. 
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

	}

	/** cases accessibles, retourne une list de ces cases. */
	public ArrayList<AiTile> accessibleTiles;

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

}
