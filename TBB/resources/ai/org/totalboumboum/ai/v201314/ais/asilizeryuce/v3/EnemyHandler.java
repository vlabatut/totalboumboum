package org.totalboumboum.ai.v201314.ais.asilizeryuce.v3;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant les calculs de nos adversaires.
 * 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class EnemyHandler extends AiAbstractHandler<Agent> {

	/**
	 * Construit un gestionnaire pour les ennemies.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	protected EnemyHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	/** la case qu l'ennemi se trouve */
	public AiTile dangerEnemy = null;
	
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;
	
	/**
	 * on controle le colon selon la portee de notre bombe
	 * 
	 * @param tile
	 *            qu'on veut savoir si on pose une bombe dans ce tile, est-ce
	 *            que cette bombe peut tuer l'adversaire, ou elle peut menacer
	 *            l'adversaire. on controle le colonne selon la portee de notre
	 *            bombe
	 * @return 0 s'il ya une ennemi dans cette colonne et 1 s'il n'y a pas
	 *         d'ennemi dans cette cologne.
	 */
	public int getEnemyInColumn(AiTile tile) {
		ai.checkInterruption();
		int range = ownHero.getBombRange();
		if (range > 8)
			range = 8;
		int result = 1, block = 0, item = 0;
		AiTile tile2 = tile;

		if (this.enemyTiles().contains(tile))
			return 2;

		for (int i = 0; i < range; i++) {
			ai.checkInterruption();
			tile = tile.getNeighbor(Direction.UP);
			if (this.enemyTiles().contains(tile) && block == 0 && item == 0) {
				dangerEnemy = tile;
				return 0;
			}

			if (!tile.getBlocks().isEmpty())
				block++;
			if (!tile.getItems().isEmpty())
				item++;
		}
		block = 0;
		item = 0;
		for (int i = 0; i < range; i++) {
			ai.checkInterruption();
			tile2 = tile2.getNeighbor(Direction.DOWN);
			if (this.enemyTiles().contains(tile2) && block == 0 && item == 0) {
				dangerEnemy = tile2;
				return 0;
			}
			if (!tile2.getBlocks().isEmpty())
				block++;
			if (!tile2.getItems().isEmpty())
				item++;
		}

		return result;

	}

	/**
	 * on controle la ligne selon la portee de notre bombe
	 * 
	 * @param tile
	 *            qu'on veut savoir si on pose une bombe dans ce tile, est-ce
	 *            que cette bombe peut tuer l'adversaire, ou elle peut menacer
	 *            l'adversaire.
	 * @return 0 s'il ya une ennemi dans cette ligne et 1 s'il n'y a pas
	 *         d'ennemi dans cette ligne.
	 */
	public int getEnemyInRow(AiTile tile) {
		ai.checkInterruption();
		int range = ownHero.getBombRange();
		if (range > 8)
			range = 8;
		int result = 1, block = 0, item = 0;
		AiTile tile2 = tile;
		if (this.enemyTiles().contains(tile))
			return 2;

		for (int i = 0; i < range; i++) {
			ai.checkInterruption();
			tile = tile.getNeighbor(Direction.LEFT);
			if (this.enemyTiles().contains(tile) && block == 0 && item == 0) {
				dangerEnemy = tile;
				return 0;
			}
			if (!tile.getBlocks().isEmpty())
				block++;
			if (!tile.getItems().isEmpty())
				item++;
		}

		block = 0;
		item = 0;
		for (int i = 0; i < range; i++) {
			ai.checkInterruption();
			tile2 = tile2.getNeighbor(Direction.RIGHT);
			if (this.enemyTiles().contains(tile2) && block == 0 && item == 0) {
				dangerEnemy = tile2;
				return 0;
			}
			if (!tile2.getBlocks().isEmpty())
				block++;
			if (!tile2.getItems().isEmpty())
				item++;
		}

		return result;
	}
	
	/**
	 * Cette methode nous donne la liste des ennemies vivant.
	 * 
	 * @return Cette methode nous donne la liste des ennemies vivant.
	 */
	public ArrayList<AiTile> enemyTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> enemyTiles = new ArrayList<AiTile>();
		for (AiHero hero :zone.getRemainingOpponents()) {
			ai.checkInterruption();
			enemyTiles.add(hero.getTile());
		}
		return enemyTiles;
	}

	/**
	 * cette une methode pour trouver l'ennemi plus proche
	 * 
	 * @return AiHero de l'ennemi plus proche
	 * 
	 */
	public AiHero getNearestEnemy() {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiHero hero = zone.getOwnHero();
		AiHero target = null;

		int distance = 5000;
		int temp_distance = 0;

		for (AiHero enemy : zone.getRemainingOpponents()) {
			ai.checkInterruption();
			if (((temp_distance = ai.tileHandler.simpleTileDistance(hero.getTile(),
					enemy.getTile())) < distance)
					&& (ai.tileHandler.accessibleTiles.contains(enemy.getTile()))
					&& (enemy.getId() != zone.getOwnHero().getId())) {
				distance = temp_distance;
				target = enemy;
			}
		}

		return target;
	}

}
