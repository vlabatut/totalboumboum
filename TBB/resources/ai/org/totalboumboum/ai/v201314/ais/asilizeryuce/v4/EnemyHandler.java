package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

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
		this.enemyInTiles = new ArrayList<AiTile>();
	}

	/** on construit cette liste quand on entre la categorie demitriangle. cette liste
	 * nous donne les 3 cases ou un ennemi peut se trouver quand on est dans cette 
	 * categorie
	 * */
	public ArrayList<AiTile> enemyInTiles;

	/** la case qu l'ennemi se trouve */
	public AiTile dangerEnemy = null;

	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null;
	/**
	 * A titre d'exemple, je stocke le sprite controlé par cet agent, car on en
	 * a aussi souvent besoin
	 */
	private AiHero ownHero = null;

	/** la premiere bombe qu'on pose a la case de preference 18,
	 *  dans la categorie demitriangle*/
	public AiTile bombFirst = null;
	/** la deuxieme bombe qu'on pose a la case de preference 19,
	 *  dans la categorie demitriangle*/
	public AiTile bombSecond = null;

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

		/*
		 * if (this.enemyTiles().contains(tile)) return 2;
		 */

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

		/* burda gerek yok enemyInDanger de yazdım */
		/*
		 * if (this.enemyTiles().contains(tile)) return 2;
		 */

		for (int i = 0; i < range; i++) {
			ai.checkInterruption();
			tile = tile.getNeighbor(Direction.LEFT);
			/* YILDIZ */
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
		for (AiHero hero : zone.getRemainingOpponents()) {
			ai.checkInterruption();
			enemyTiles.add(hero.getTile());
		}
		return enemyTiles;
	}

	/**
	 * C'est une methode pour trouver l'ennemi plus proche
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
			if (((temp_distance = ai.tileHandler.simpleTileDistance(
					hero.getTile(), enemy.getTile())) < distance)
					&& (ai.tileHandler.accessibleTiles
							.contains(enemy.getTile()))
					&& (enemy.getId() != zone.getOwnHero().getId())) {
				distance = temp_distance;
				target = enemy;
			}
		}

		return target;
	}

	/**
	 * C'est une methode pour trouver si on peut appliquer notre strategie de
	 * demi-triangle a un adversaire.
	 * @param hero l'ennemi qu'on observe
	 * 
	 * @return si on peut appliquer le demi-triangle a agent passe en parametre
	 *         elle return true sinon elle return false.
	 *         
	 */
	public boolean triangle(AiHero hero) {
		ai.checkInterruption();
		boolean result = false;
		AiTile neigbours = null;

		// / aşağı yukarı
		if (!hero.getTile().getNeighbor(Direction.DOWN).getBlocks().isEmpty()
				&& !hero.getTile().getNeighbor(Direction.UP).getBlocks()
						.isEmpty()) {
			if (!hero.getTile().getNeighbor(Direction.RIGHT).getBombs()
					.isEmpty()
					&& hero.getTile().getNeighbor(Direction.LEFT)
							.isCrossableBy(hero)) {
				neigbours = hero.getTile().getNeighbor(Direction.LEFT);
				if (!neigbours.getNeighbor(Direction.UP).isCrossableBy(hero)
						&& neigbours.getNeighbor(Direction.DOWN).isCrossableBy(
								hero)) {
					if (!neigbours.getNeighbor(Direction.LEFT).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.DOWN);
						if (neigbours.getNeighbor(Direction.DOWN).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.DOWN);
							bombSecond = neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.DOWN);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.LEFT));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.LEFT));

							
							return true;
						}
					}
				}

				else if (neigbours.getNeighbor(Direction.UP)
						.isCrossableBy(hero)
						&& !neigbours.getNeighbor(Direction.DOWN)
								.isCrossableBy(hero)) {
					if (!neigbours.getNeighbor(Direction.LEFT).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.UP)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.UP);
						if (neigbours.getNeighbor(Direction.UP).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getNeighbor(Direction.UP).getBlocks()
										.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getNeighbor(Direction.UP).getBlocks()
										.isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.UP);
							bombSecond = neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.UP);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.LEFT));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.LEFT));

							return true;
						}
					}
				}
			} else if (!hero.getTile().getNeighbor(Direction.LEFT).getBombs()
					.isEmpty()
					&& hero.getTile().getNeighbor(Direction.RIGHT)
							.isCrossableBy(hero)) {
				neigbours = hero.getTile().getNeighbor(Direction.RIGHT);
				if (!neigbours.getNeighbor(Direction.UP).isCrossableBy(hero)
						&& neigbours.getNeighbor(Direction.DOWN).isCrossableBy(
								hero)) {
					if (!neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.DOWN);
						if (neigbours.getNeighbor(Direction.DOWN).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.DOWN);
							bombSecond = neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.DOWN);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.RIGHT));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.RIGHT));


							return true;
						}
					}
				}

				else if (neigbours.getNeighbor(Direction.UP)
						.isCrossableBy(hero)
						&& !neigbours.getNeighbor(Direction.DOWN)
								.isCrossableBy(hero)) {
					if (!neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.UP)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.UP);
						if (neigbours.getNeighbor(Direction.UP).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.RIGHT)
										.getNeighbor(Direction.UP).getBlocks()
										.isEmpty()
								&& neigbours.getNeighbor(Direction.LEFT)
										.getNeighbor(Direction.UP).getBlocks()
										.isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.LEFT)
									.getNeighbor(Direction.UP);
							bombSecond = neigbours.getNeighbor(Direction.RIGHT)
									.getNeighbor(Direction.UP);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.RIGHT));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.RIGHT));

							return true;
						}
					}
				}

			}

		}
		// / sağ sol
		else if (!hero.getTile().getNeighbor(Direction.LEFT).getBlocks()
				.isEmpty()
				&& !hero.getTile().getNeighbor(Direction.RIGHT).getBlocks()
						.isEmpty()) {
			if (!hero.getTile().getNeighbor(Direction.UP).getBombs().isEmpty()
					&& hero.getTile().getNeighbor(Direction.DOWN)
							.isCrossableBy(hero)) {
				neigbours = hero.getTile().getNeighbor(Direction.DOWN);
				if (!neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
						&& neigbours.getNeighbor(Direction.LEFT).isCrossableBy(
								hero)) {
					if (!neigbours.getNeighbor(Direction.DOWN).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.LEFT)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.LEFT);
						if (neigbours.getNeighbor(Direction.LEFT).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.LEFT);
							bombSecond = neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.LEFT);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.DOWN));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.DOWN));

							return true;
						}
					}
				}

				else if (neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(
						hero)
						&& !neigbours.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero)) {
					if (!neigbours.getNeighbor(Direction.DOWN).isCrossableBy(
							hero)
							&& !neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.RIGHT);
						if (neigbours.getNeighbor(Direction.RIGHT).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.RIGHT);
							bombSecond = neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.RIGHT);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.DOWN));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.DOWN));

							return true;
						}
					}
				}
			} else if (!hero.getTile().getNeighbor(Direction.DOWN).getBombs()
					.isEmpty()
					&& hero.getTile().getNeighbor(Direction.UP)
							.isCrossableBy(hero)) {
				neigbours = hero.getTile().getNeighbor(Direction.UP);
				if (!neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
						&& neigbours.getNeighbor(Direction.LEFT).isCrossableBy(
								hero)) {
					if (!neigbours.getNeighbor(Direction.UP)
							.isCrossableBy(hero)
							&& !neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.LEFT)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.LEFT);
						if (neigbours.getNeighbor(Direction.LEFT).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getNeighbor(Direction.LEFT)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.LEFT);
							bombSecond = neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.LEFT);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.UP));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.UP));
							return true;
						}
					}
				}

				else if (neigbours.getNeighbor(Direction.RIGHT).isCrossableBy(
						hero)
						&& !neigbours.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero)) {
					if (!neigbours.getNeighbor(Direction.UP)
							.isCrossableBy(hero)
							&& !neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero)) {
						neigbours = hero.getTile().getNeighbor(Direction.RIGHT);
						if (neigbours.getNeighbor(Direction.RIGHT).getBlocks()
								.isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.UP)
										.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()
								&& neigbours.getNeighbor(Direction.DOWN)
										.getNeighbor(Direction.RIGHT)
										.getBlocks().isEmpty()) {
							bombFirst = neigbours.getNeighbor(Direction.DOWN)
									.getNeighbor(Direction.RIGHT);
							bombSecond = neigbours.getNeighbor(Direction.UP)
									.getNeighbor(Direction.RIGHT);

							fillEnemyInTiles(hero.getTile());
							fillEnemyInTiles(hero.getTile().getNeighbor(
									Direction.UP));
							fillEnemyInTiles(neigbours
									.getNeighbor(Direction.UP));

							return true;
						}
					}
				}

			}

		}

		return result;
	}

	/**
	 * Cette methode pour ajoute la case source renvoie dans la liste enemyInTiles
	 * @param sourceTile qu'on observe
	 * @return la liste enemyInTiles
	 */
	public ArrayList<AiTile> fillEnemyInTiles(AiTile sourceTile) {
		ai.checkInterruption();

		if (!this.enemyInTiles.contains(sourceTile))
			this.enemyInTiles.add(sourceTile);

		return enemyInTiles;
	}

}
