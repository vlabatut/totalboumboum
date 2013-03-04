package org.totalboumboum.ai.v200910.ais.mancuhanpinarer.v5c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * Cette classe implemente l'AI du groupe rouge. Actuellement, la strategie de
 * defense et le bonus sont implementes.
 * 
 * @author Koray Mançuhan
 * @author Özgün Pınarer
 */
@SuppressWarnings("deprecation")
public class MancuhanPinarer extends ArtificialIntelligence {
	/** la case accessible et qui n'est pas dans la portee de la bombe est 
	// represente dans la matrice da la zone.
	 */
	private final int CASE_SUR = 0;
	/** le mur non-destructible represente dans la matrice de la zone */
	private final int CASE_INACCESSIBLE = 1;
	/** la portee d'une bombe represente dans la matrice de la zone */
	private final int CASE_SCOPE = 2;

	/** */
	private final int CASE_BOMBE = 3;
	/** la mur destructible represente dans la matrice de la zone */
	private final int MUR_DESTRUCTIBLE = 4;
	/** l'item represente dans la matrice de la zone */
	private final int CASE_BONUS = 5;

	/** */
	private final int ADVERSAIRE = 7;
	/** chemin a suivre pour l'algo defense */
	private AiPath nextMove = null;
	/** chemin a suivre pour ramasser le bonus */
	private AiPath nextMoveBonus = null;
	/** notre hero sur la zone */
	private AiHero ourHero;

	/** */
	private boolean checkPath = true;

	/** Les variables de classes attaque. */
	private AiPath nextMoveAttack = null;
	/** verifie s'il faut poser la bombe quand on attaque. */
	private boolean accomplished = false;

	/** */
	private AiHero chosenEnemy = null;

	/** */
	private int dropBombLimit;
	/** verifie s'il faut poser la bombe quand on attaque pour ouvrir un chemin. */
	private boolean openPath;

	/** */
	private boolean badPath;
	
	/** */
	private boolean checkBomb = false;

	/** */
	private AiBomb openBomb = null;

	/** */
	private AiTile openBombTile = null;

	/** */
	private List<AiTile> pathTiles = new ArrayList<AiTile>();

	// le booleen pour determiner s'il est necessaire d'entrer dans l'algo de
	// bonus.

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA 
	 * */
	@Override
	public AiAction processAction() throws StopRequestException {
		// avant tout: test d'interruption
		checkInterruption();
		// La perception instantanement de l'environnement
		AiZone gameZone = getPercepts();
		// Le resultat du traitement indiquant l'action suivante
		// par defaut, il ne fait rien.
		AiAction result = new AiAction(AiActionName.NONE);
		boolean searchBonus = true;
		// la longueur de la zone
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();

		// la matrice de la zone
		int[][] matrice = new int[height][width];
		// initialisation de notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		// initialisation de matrice de la zone
		this.initialiseMatrice(matrice, gameZone);
		// remplissage de la matrice de la zone par les items

		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);
		this.fillHerosMatrice(matrice, gameZone);
		this.fillBombsMatrice(matrice, gameZone);

		// On actualise la critere pour entrer dans l'algo recherche de bonus
		// Tant qu'on ne peut pas poser simultanement 6 bombes et que la portee
		// n'est pas 6 cases, on entre dans l'algo.
		if ((ourHero.getBombNumber() >= 1 || ourHero.getBombRange() >= 1)
				|| (gameZone.getHiddenItemsCount() == 0 && gameZone.getItems()
						.size() == 0))
			searchBonus = false;

		// Si le joueur est dans la portee d'une bombe, il entre dans l'algo. de
		// defense
		if (matrice[ourHero.getLine()][ourHero.getCol()] == CASE_SCOPE) {
			this.nextMoveBonus = null;
			this.nextMoveAttack = null;
			this.defenseAlgorithm(gameZone, matrice);
			if (nextMove != null)
				if (nextMove.getLength() != 0)
					result = this.newAction(nextMove);
				else
					nextMove = null;

		} else {
			// Si le critere est vrai, il entre dans l'algo de la recherhe de
			// bonus.

			if (searchBonus && checkDistance(matrice, gameZone)) {

				this.nextMove = null;
				this.nextMoveAttack = null;
				// l'algo de bonus
				bonusAlgorithm(gameZone, matrice);
				// le teste pour deposer la bombe commence
				boolean dropBomb = false;
				// On commence a trouver si le hero est arrive au voisinage
				// d'une case mur.
				List<AiTile> neighboors = ourHero.getTile().getNeighbors();
				for (int i = 0; i < neighboors.size(); i++) {
					checkInterruption();
					// Si la case est au voisinage d'une mure destructible,
					// alors il pose la bombe
					if (matrice[neighboors.get(i).getLine()][neighboors.get(i)
							.getCol()] == MUR_DESTRUCTIBLE) {
						dropBomb = true;
					}
					// Au cas de trouver une case dans la portee d'une bombe au
					// voisinage,
					// le hero annule de poser une bombe et s'arrete pour ne pas
					// etre tue
					// quand il s'enfuit.
					else if (matrice[neighboors.get(i).getLine()][neighboors
							.get(i).getCol()] == CASE_SCOPE) {
						dropBomb = false;
						break;
					} else if (ourHero.getBombCount() == 1) {
						dropBomb = false;
						break;
					}
				}
				// Il pose la bombe
				if (dropBomb) {
					result = new AiAction(AiActionName.DROP_BOMB);
				}// Si non il fait une nouvelle action.
				else if (nextMoveBonus != null) {
					if (nextMoveBonus.getLength() != 0)
						result = this.newAction(nextMoveBonus);
					else
						nextMoveBonus = null;
				}
			}
			// L'attaque, pas encore implemente.
			else {
				// Attack
				if (gameZone.getRemainingHeroes().size() > 1) {
					this.nextMove = null;
					this.nextMoveBonus = null;
//					int scopeCount = 0;
//					int blockCount = 0;
					List<AiTile> neighboors = ourHero.getTile().getNeighbors();
					for (int i = 0; i < neighboors.size(); i++) {
						checkInterruption();
						if (matrice[neighboors.get(i).getLine()][neighboors
								.get(i).getCol()] == CASE_SCOPE) {
//							scopeCount++;
						}
						if (matrice[neighboors.get(i).getLine()][neighboors
								.get(i).getCol()] == MUR_DESTRUCTIBLE
								|| matrice[neighboors.get(i).getLine()][neighboors
										.get(i).getCol()] == CASE_INACCESSIBLE) {
//							blockCount++;
						}
					}
					boolean isDead = false;
					if (chosenEnemy != null) {
						isDead = this.isHeroDead(gameZone, chosenEnemy);
					}
					if (isDead)
						chosenEnemy = null;
					if (!checkBomb) {
						this.attackAlgorithm(gameZone, matrice);
					} else {
						if (!isBombExists(openBombTile)) {
							checkBomb = false;
							this.openBomb = null;
							this.nextMoveAttack = null;
							result = new AiAction(AiActionName.NONE);
						} else {
							result = new AiAction(AiActionName.NONE);
							this.nextMoveAttack = null;
						}
					}

					if (gameZone.getItems().size() > 10) {
						if (gameZone.getTotalTime() < 10000)
							dropBombLimit = 1;
					}

					if (openPath && accessibleEnemyExists(gameZone) != null) {
						chosenEnemy = accessibleEnemyExists(gameZone);
					}

					// this.attackAlgorithm(gameZone, matrice);
					if (accomplished && ourHero.getBombCount() < dropBombLimit) {
						result = new AiAction(AiActionName.DROP_BOMB);
						accomplished = false;
					} else if (nextMoveAttack != null) {
						if (nextMoveAttack.getLength() != 0) {
							result = this.newAction(nextMoveAttack);
						} else
							nextMoveAttack = null;
					}
				}

			}
		}
		// On assigne la nouvelle action
		return result;
	}

	/**
	 * Methode initialisant notre matrice de zone avant la remplissage. Chaque
	 * case est initialement considere comme CASE_SUR
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void initialiseMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				matrice[i][j] = CASE_SUR;
			}
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * bombe et les cases qui sont dans la portee de ces bombes. Ces nouveaux
	 * cases sont represente par CASE_INACCESSIBLE et CASE_SCOPE respectivement.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void fillBombsMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = CASE_BOMBE;
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i)
						.getCol()] = CASE_SCOPE;
			}
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une mur.
	 * Les cases des mures non-destructibles sont representees par
	 * CASE_INACCESSIBLE et destructibles par MUR_DESTRUCTIBLE.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void fillBlocksMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible())
				matrice[block.getLine()][block.getCol()] = MUR_DESTRUCTIBLE;
			else
				matrice[block.getLine()][block.getCol()] = CASE_INACCESSIBLE;
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * flamme. Les cases des flammes sont represente par CASE_INACCESSIBLE
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void fillFiresMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = CASE_INACCESSIBLE;
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * item. Les cases des items sont represente par ITEM.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void fillItemsBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = CASE_BONUS;
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone possedant les heros.
	 * Les cases des items sont represente par ITEM.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void fillHerosMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiHero> heros = gameZone.getRemainingHeroes();
		for (int i = 0; i < heros.size(); i++) {
			checkInterruption();
			if (ourHero.getLine() != heros.get(i).getLine()
					&& ourHero.getCol() != heros.get(i).getCol())
				matrice[heros.get(i).getLine()][heros.get(i).getCol()] = ADVERSAIRE;
		}

	}

	/**
	 * Methode calculant la liste des cases ou le hero peut aller. On prend
	 * aussi en compte les cases qui sont dans la portee des bombes et les deux
	 * sous-parties de la zone. Notre hero peut se deplacer en traversant ces
	 * cases.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 * 		Description manquante !
	 */

	private List<AiTile> calculateEndPoints(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiTile> endPoints = new ArrayList<AiTile>();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				checkInterruption();
				if (matrice[i][j] == CASE_SUR || matrice[i][j] == CASE_BONUS) {
					endPoints.add(gameZone.getTile(i, j));
				}
			}
		}
		return endPoints;
	}

	/**
	 * 
	 * Verifie la distance entre notre hero et adversaire. Si
	 * c'est faux, il n'entre pas dans l'algo d'attaque, si c'est
	 * vrai il entre 
	 * 
	 * @param matrice
	 * 			La matrice representant la zone du jeu.
	 * @param gameZone
	 * 			La zone du jeu.
	 * @return
	 * 			Vrai si la distance de manhatten entre
	 * 			l'ennemi et notre hero est cinq cases ou plus.
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean checkDistance(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		boolean result = true;
		for (int i = 0; i < gameZone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				checkInterruption();
				if (matrice[i][j] == ADVERSAIRE) {
					AiTile tile = gameZone.getTile(i, j), ourTile = ourHero
							.getTile();
					int distance = gameZone.getTileDistance(ourTile, tile);
					if (distance < 5)
						result = false;
				}

			}
		}
		return result;
	}

	/**
	 * Methode implementant l'algo d'attaque.
	 * 
	 * @param gameZone
	 * 			La zone du jeu.
	 * @param matrice
	 * 			La matrice representant la zone du jeu.
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void attackAlgorithm(AiZone gameZone, int[][] matrice)
			throws StopRequestException {
		checkInterruption();
		//Si le chemin n'est pas calcule on calcule le nouveau
		//chemin
		if (nextMoveAttack == null) {
			//On choisit l'hero le plus proche pour l'attaque.
			if (chosenEnemy == null) {
				AiHero isEnemyExists = accessibleEnemyExists(gameZone);

				if (isEnemyExists == null) {
					chosenEnemy = this.closestHero(gameZone);
				} else {
					chosenEnemy = isEnemyExists;
				}
			}
			boolean isAccessible = this.isAccessibleEnemy(chosenEnemy);
			
			//Si l'hero n'est pas accessible, on commence a ouvrir un
			//nouveau chemin.
			if (!isAccessible) {
				openPath = true;
				this.dropBombLimit = 1;
				List<AiTile> accessibleTiles = this
						.accessibleDestructibleTiles(gameZone, matrice);

				if (accessibleTiles.size() != 0) {
					AiTile closestTile = this.closestTileToEnemy(gameZone,
							accessibleTiles, chosenEnemy);
					List<AiTile> tileList = new ArrayList<AiTile>();
					tileList.add(closestTile);
					this.nextMoveAttack = this.calculateShortestOffensePath(
							ourHero, ourHero.getTile(), tileList);
				}
			} 
			//Si non on s'approche l'adversaire choisit
			else {
				openPath = false;
				this.dropBombLimit = 5;
				List<AiTile> tileList = new ArrayList<AiTile>();
				tileList.add(chosenEnemy.getTile());
				this.nextMoveAttack = this.calculateShortestOffensePath(ourHero,
						ourHero.getTile(), tileList);
				if (nextMoveAttack.getLength() > 3) {
					AiTile lastTile = nextMoveAttack.getLastTile();
					nextMoveAttack.removeTile(lastTile);
					AiTile enemyPos = chosenEnemy.getTile();
					int blockCount = 0;
					List<AiTile> neighboors = enemyPos.getNeighbors();
					for (int i = 0; i < neighboors.size(); i++) {
						checkInterruption();
						int x = neighboors.get(i).getLine();
						int y = neighboors.get(i).getCol();
						if (matrice[x][y] == MUR_DESTRUCTIBLE
								|| matrice[x][y] == CASE_INACCESSIBLE)
							blockCount++;
					}
					if (blockCount < 2) {
						lastTile = nextMoveAttack.getLastTile();
						nextMoveAttack.removeTile(lastTile);
						lastTile = nextMoveAttack.getLastTile();
						nextMoveAttack.removeTile(lastTile);
					}
				}

			}
			if (nextMoveAttack != null)
				if (nextMoveAttack.getLength() != 0)
					this.pathTiles = nextMoveAttack.getTiles();

		}
		//Si un chemin est deja calcule, on enleve les cases traverse
		//precedemment.
		else {

			if (nextMoveAttack.getLength() == 0) {
				nextMoveAttack = null;
				accomplished = true;
			} else {

				boolean adapt = false;
				List<AiTile> nextTiles = nextMoveAttack.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) {
					checkInterruption();
					if (!nextTiles.get(i).isCrossableBy(ourHero)
							|| matrice[nextTiles.get(i).getLine()][nextTiles
									.get(i).getCol()] == CASE_SCOPE)
						adapt = true;
				}
				if (chosenEnemy != null)
					if (openPath && isAccessibleEnemy(chosenEnemy))
						adapt = true;

				if (openPath && chosenEnemy != null
						&& !isHeroDead(gameZone, chosenEnemy)) {
					List<AiTile> accessibleTiles = this
							.accessibleDestructibleTiles(gameZone, matrice);
					AiTile closestTile = this.closestTileToEnemy(gameZone,
							accessibleTiles, chosenEnemy);
					AiTile end = nextMoveAttack.getLastTile();
					if (gameZone.getTileDistance(closestTile, chosenEnemy
							.getTile()) < gameZone.getTileDistance(end,
							chosenEnemy.getTile()))
						adapt = true;
				}

				if (adapt)
					nextMoveAttack = null;
				else {
					if ((this.ourHero.getLine() == this.nextMoveAttack.getTile(
							0).getLine())
							&& (this.ourHero.getCol() == this.nextMoveAttack
									.getTile(0).getCol())) {
						this.nextMoveAttack.getTiles().remove(0);
						//On verifie si on est arrive au dernier case du chemin. 
						//Si c'est le cas, on pose la bombe, sinon on ne la pose 
						//pas
						if (this.nextMoveAttack.getTiles().isEmpty()) {
							this.nextMoveAttack = null;
							if (chosenEnemy != null) {
								if (isSecurePathExists(gameZone, matrice,
										chosenEnemy))
									accomplished = true;
								else
									accomplished = false;

							}

						}
					}

				}

			}
		}
	}

	/**
	 * Methode qui determine s'il existe un chemin
	 * qui ne contient aucune case d'une portee d'un bombe.
	 * 
	 * @param gameZone
	 * 			La zone du jeu.
	 * @param matrice
	 * 			La matrice du jeu.
	 * @param enemy
	 * 			L'adversaire.
	 * @return
	 * 			Vrai 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean isSecurePathExists(AiZone gameZone, int[][] matrice,
			AiHero enemy) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		List<AiTile> endPoints = this.calculateEndPoints(matrice, gameZone);
		AiTile ourTile = ourHero.getTile();
		endPoints.remove(ourTile);
		int distance = gameZone.getTileDistance(ourHero.getTile(), enemy
				.getTile());
		if (distance < 5) {
			if (ourHero.getCol() <= gameZone.getWidth() / 2
					&& ourHero.getLine() <= gameZone.getHeight() / 2) {
				for (int i = 0; i < gameZone.getHeight() / 2; i++) {
					checkInterruption();
					for (int j = 0; j < gameZone.getWidth() / 2; j++) {
						checkInterruption();
						if (matrice[i][j] == CASE_SUR) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.remove(temp);
						}
					}
				}
			} else if (ourHero.getCol() <= gameZone.getWidth() / 2
					&& ourHero.getLine() >= gameZone.getHeight() / 2) {
				for (int i = gameZone.getHeight() / 2; i < gameZone.getHeight(); i++) {
					checkInterruption();
					for (int j = 0; j < gameZone.getWidth() / 2; j++) {
						checkInterruption();
						if (matrice[i][j] == CASE_SUR) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.remove(temp);
						}
					}
				}
			} else if (ourHero.getCol() >= gameZone.getWidth() / 2
					&& ourHero.getLine() <= gameZone.getHeight() / 2) {
				for (int i = 0; i < gameZone.getHeight() / 2; i++) {
					checkInterruption();
					for (int j = gameZone.getWidth() / 2; j < gameZone
							.getWidth(); j++) {
						checkInterruption();
						if (matrice[i][j] == CASE_SUR) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.remove(temp);
						}
					}
				}
			} else if (ourHero.getCol() >= gameZone.getWidth() / 2
					&& ourHero.getLine() >= gameZone.getHeight() / 2) {
				for (int i = gameZone.getHeight() / 2; i < gameZone.getHeight(); i++) {
					checkInterruption();
					for (int j = gameZone.getWidth() / 2; j < gameZone
							.getWidth(); j++) {
						checkInterruption();
						if (matrice[i][j] == CASE_SUR) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.remove(temp);
						}
					}
				}
			}
		}
		AiPath path = this.calculateShortestPath(ourHero, ourHero.getTile(),
				endPoints);
		if (path.getLength() == 0)
			result = false;
		return result;
	}

	/**
	 * Verifie s'il existe encore notre bombe sur la case
	 * passe en parametre.
	 * 
	 * @param bombTile
	 * 			La case de bombe passe en parametre.
	 * @return
	 * 			vrai si la bombe existe encore
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private boolean isBombExists(AiTile bombTile) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		if (bombTile!=null && bombTile.getBombs().size()==0)
			result = false;
		return result;
	}

	/**
	 * Verifie si l'ennemi est accessible ou pas
	 * 
	 * @param enemy
	 * 			L'ennemi 
	 * @return
	 * 			vrai si l'hero est accessible
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean isAccessibleEnemy(AiHero enemy) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		List<AiTile> endPoint = new ArrayList<AiTile>();
		endPoint.add(enemy.getTile());
		AiPath temp = this.calculateShortestOffensePath(ourHero, ourHero
				.getTile(), endPoint);
		int x = enemy.getLine(), y = enemy.getCol();
		boolean samePos = false;
		if (x == ourHero.getLine() && y == ourHero.getCol())
			samePos = true;
		if (temp.getLength() == 0 && !samePos)
			result = false;
		return result;
	}

	/**
	 * Verifie si l'hero est mort ou pas
	 * 
	 * @param gameZone
	 * 			La zone du jeu
	 * @param hero
	 * 			L'hero adversaire
	 * @return
	 * 			vrai si l'hero adversaire est mort.
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	private boolean isHeroDead(AiZone gameZone, AiHero hero) throws StopRequestException {
		checkInterruption();
		boolean result = false;
		List<AiHero> allHeros = gameZone.getHeroes();
		List<AiHero> remainingHeros = gameZone.getRemainingHeroes();
		if (allHeros.contains(hero) && !remainingHeros.contains(hero))
			result = true;
		return result;
	}

	/**
	 * Retourne un hero accessible sur la zone du jeu. S'il n'y a
	 * aucun, la methode retourne null. 
	 * 
	 * @param gameZone
	 * 			La zone du jeu.
	 * @return
	 * 			L'hero accessible
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiHero accessibleEnemyExists(AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		AiHero enemy = null;
		List<AiHero> heros = gameZone.getRemainingHeroes();
		heros.remove(ourHero);

		for (int i = 0; i < heros.size(); i++) {
			checkInterruption();
			AiHero temp = heros.get(i);
			List<AiTile> tempTiles = new ArrayList<AiTile>();
			tempTiles.add(temp.getTile());
			AiPath tempPath = this.calculateShortestOffensePath(ourHero, ourHero
					.getTile(), tempTiles);
			boolean samePosition = false;
			if (temp.getLine() == ourHero.getLine()
					&& temp.getCol() == ourHero.getCol())
				samePosition = true;
			if (tempPath.getLength() != 0 || samePosition) {
				enemy = temp;
				break;
			}

		}
		return enemy;
	}

	/**
	 * retourne l'hero le plus proche sur la zone du jeu.
	 *  
	 * @param gameZone
	 * 			La zone du jeu.
	 * @return
	 * 			L'hero le plus proche
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiHero closestHero(AiZone gameZone) throws StopRequestException {
		checkInterruption();
		List<AiHero> heros = gameZone.getRemainingHeroes();
		AiHero enemy = heros.get(0);
		heros.remove(ourHero);
		AiTile ourPos = ourHero.getTile(), enemyPos = enemy.getTile();
		int distance = gameZone.getTileDistance(ourPos, enemyPos);

		for (int i = 1; i < heros.size(); i++) {
			checkInterruption();
			AiHero temp = heros.get(i);
			int tempDis = gameZone.getTileDistance(ourPos, temp.getTile());
			if (tempDis < distance) {
				enemy = temp;
				distance = tempDis;
			}
		}

		return enemy;
	}
	
	
	/**
	 * Retourne la liste des cases voisines aux murs destructibles accessibles. Ce sont des
	 * cases qui ne sont pas dans la portee d'une bombe ou une case contenant un item.
	 *  
	 * @param gameZone
	 * 			La zone du jeu.
	 * @param matrice
	 * 			La matrice
	 * @return
	 * 			la liste des cases voisines aux murs destructibles accessibles
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiTile> accessibleDestructibleTiles(AiZone gameZone,
			int[][] matrice) throws StopRequestException {
		checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> destructibleTiles = new ArrayList<AiTile>();

		List<AiBlock> blocks = gameZone.getBlocks();
		for (int j = 0; j < blocks.size(); j++) {
			checkInterruption();
			if (matrice[blocks.get(j).getLine()][blocks.get(j).getCol()] == MUR_DESTRUCTIBLE) {
				List<AiTile> tempList = blocks.get(j).getTile().getNeighbors();
				for (int i = 0; i < tempList.size(); i++) {
					checkInterruption();
					int x = tempList.get(i).getLine(), y = tempList.get(i)
							.getCol();
					if ((matrice[x][y] == CASE_SUR || matrice[x][y] == CASE_BONUS))
						destructibleTiles.add(tempList.get(i));
				}

			}
		}

		for (int j = 0; j < destructibleTiles.size(); j++) {
			checkInterruption();
			List<AiTile> tiles = new ArrayList<AiTile>();
			tiles.add(destructibleTiles.get(j));
			AiPath tempPath = this.calculateShortestOffensePath(ourHero, ourHero
					.getTile(), tiles);
			if (tempPath.getLength() != 0)
				result.add(destructibleTiles.get(j));
			tiles.remove(0);
		}

		return result;
	}

	/**
	 * Retourne la case voisine au mur destructible dont la distance a l'adversaire
	 * est la plus courte.
	 * 
	 * @param gameZone
	 * 			La zone du jeu
	 * @param destructibleTiles
	 * 			la liste des cases voisines aux murs destructibles accessibles
	 * @param enemy
	 * 			l'hero adversaire.
	 * @return
	 * 			la case la plus proche.
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiTile closestTileToEnemy(AiZone gameZone,
			List<AiTile> destructibleTiles, AiHero enemy)
			throws StopRequestException {
		checkInterruption();
		AiTile closestTile = destructibleTiles.get(0);
		int distance = gameZone.getTileDistance(closestTile, enemy.getTile());
		for (int i = 1; i < destructibleTiles.size(); i++) {
			checkInterruption();
			AiTile temp = destructibleTiles.get(i);
			int tempDis = gameZone.getTileDistance(temp, enemy.getTile());
			if (tempDis < distance) {
				distance = tempDis;
				closestTile = temp;
			}
		}
		return closestTile;
	}

	/**
	 * Methode implementant l'algo. de bonus.
	 * 
	 * @param gameZone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void bonusAlgorithm(AiZone gameZone, int[][] matrice)
			throws StopRequestException {
		checkInterruption();
		// Si le prochaine action n'est pas determine, elle va etre calculee ici
		if (nextMoveBonus == null) {
			List<AiItem> itemList = gameZone.getItems();
			if (itemList.size() != 0 && checkPath) {
				List<AiTile> itemTiles = new ArrayList<AiTile>();
				for (int i = 0; i < itemList.size(); i++) {
					checkInterruption();
					if (matrice[itemList.get(i).getLine()][itemList.get(i)
							.getCol()] != CASE_SCOPE)
						itemTiles.add(itemList.get(i).getTile());
				}
				nextMoveBonus = this.calculateShortestOffensePath(ourHero,
						ourHero.getTile(), itemTiles);
				if (nextMoveBonus.getLength() == 0) {
					checkPath = false;
					nextMoveBonus = null;
				}

			} else {
				List<AiBlock> blocks = gameZone.getBlocks();
				List<AiTile> destructibleTiles = new ArrayList<AiTile>();
				for (int j = 0; j < blocks.size(); j++) {
					checkInterruption();
					if (matrice[blocks.get(j).getLine()][blocks.get(j).getCol()] == MUR_DESTRUCTIBLE) {
						List<AiTile> tempList = blocks.get(j).getTile()
								.getNeighbors();
						for (int i = 0; i < tempList.size(); i++) {
							checkInterruption();
							int x = tempList.get(i).getLine(), y = tempList
									.get(i).getCol();
							if (matrice[x][y] == CASE_SUR
									&& (tempList.get(i).getBombs().size() == 0))
								destructibleTiles.add(tempList.get(i));
						}

					}
				}

				nextMoveBonus = this.calculateShortestOffensePath(ourHero,
						ourHero.getTile(), destructibleTiles);
				checkPath = true;
			}
			this.pathTiles = nextMoveBonus.getTiles();
		}
		// Si non, il continue a son mouvement en annulant
		//des cases qui sont deja traverse.
		else {

			if (nextMoveBonus.getLength() == 0)
				nextMoveBonus = null;
			else {
				boolean adapt = false;
				List<AiTile> nextTiles = nextMoveBonus.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) {
					checkInterruption();
					if (!nextTiles.get(i).isCrossableBy(ourHero)
							|| matrice[nextTiles.get(i).getLine()][nextTiles
									.get(i).getCol()] == CASE_SCOPE)
						adapt = true;
				}
				if (adapt)
					nextMoveBonus = null;
				else {
					if ((this.ourHero.getLine() == this.nextMoveBonus
							.getTile(0).getLine())
							&& (this.ourHero.getCol() == this.nextMoveBonus
									.getTile(0).getCol())) {
						this.nextMoveBonus.getTiles().remove(0);
						if (this.nextMoveBonus.getTiles().isEmpty()) {
							this.nextMoveBonus = null;
						}
					}
				}

			}
		}

	}

	/**
	 * Methode implementant l'algorithme de defense.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param matrice 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void defenseAlgorithm(AiZone gameZone, int[][] matrice)
			throws StopRequestException {
		checkInterruption();
		if (openPath) {
			if (openBomb == null) {
				if (ourHero.getTile().getBombs().size() != 0) {
					this.openBomb = ourHero.getTile().getBombs().get(0);
					this.openBombTile = openBomb.getTile();
				}
			}
		}
		// La position actuelle de notre hero
		AiTile startPoint = ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = this.calculateEndPoints(matrice, gameZone);
		// Si le prochaine action n'est pas determine, elle va etre calculee ici
		if (this.nextMove == null) {
			endPoints = this.calculateEndPoints(matrice, gameZone);
			nextMove = this.calculateShortestPath(ourHero, startPoint,
					endPoints);
			this.badPath = false;
			if (nextMove.getLength() == 0 && gameZone.getBombs().size() != 0) {

				List<AiBomb> bombList = this.orderBombTimes(gameZone);
				nextMove = this.calculateDangerousPath(bombList);
				badPath = true;
			}
			if (nextMove.getLength() != 0)
				this.pathTiles = nextMove.getTiles();
		}
		//Si non, il continue a son mouvement en annulant
		//des cases qui sont deja traverse.
		else {
			if (openPath) {
				checkBomb = true;
			} else {
				checkBomb = false;
			}
			if (nextMove.getLength() == 0) {
				nextMove = null;
			} else {
				boolean adapt = false;
				if (matrice[nextMove.getLastTile().getLine()][nextMove
						.getLastTile().getCol()] == CASE_SCOPE)
					nextMove = null;
				else {
					List<AiTile> nextTiles = nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) {
						checkInterruption();
						if (!nextTiles.get(i).isCrossableBy(ourHero))
							adapt = true;
					}
					if (badPath) {
						endPoints = this.calculateEndPoints(matrice, gameZone);
						nextMove = this.calculateShortestPath(ourHero,
								startPoint, endPoints);
						if (nextMove.getLength() != 0)
							adapt = true;
					}
					if (adapt)
						nextMove = null;
					else {
						if ((this.ourHero.getLine() == this.nextMove.getTile(0)
								.getLine())
								&& (this.ourHero.getCol() == this.nextMove
										.getTile(0).getCol())) {
							this.nextMove.getTiles().remove(0);
							if (this.nextMove.getTiles().isEmpty()) {
								this.nextMove = null;
							}
						}
					}
				}
			}
		}

	}

	/**
	 * L'ordonne le temps d'explosion des bombes dont la portee
	 * contient notre héro.
	 * 
	 * @param gameZone
	 * 			La zone du jeu.
	 * @return
	 * 			La liste des bombes.
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiBomb> orderBombTimes(AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiBomb> bombList = gameZone.getBombs();
		boolean changed = true;
		while (changed) {
			checkInterruption();
			changed = false;
			for (int i = 0; i < (bombList.size() - 1); i++) {
				checkInterruption();
				AiBomb left = bombList.get(i);
				AiBomb right = bombList.get(i + 1);
				if (right.getNormalDuration() > left.getNormalDuration()) {
					changed = true;
					AiBomb temp = right;
					bombList.set(i + 1, left);
					bombList.set(i, temp);
				}
			}
		}
		return bombList;
	}

	/**
	 * Retourne le chemin le plus court pour l'algo de defense s'il n'y a plus
	 * un chemin vers la case sur.
	 * 
	 * @param bombList
	 * 			La liste des bombes
	 * @return
	 * 			Le chemin a suivre
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiPath calculateDangerousPath(List<AiBomb> bombList)
			throws StopRequestException {
		checkInterruption();
		AiPath result;
		List<AiTile> neighboors = bombList.get(0).getTile().getNeighbors();
		result = this.calculateShortestPath(ourHero, ourHero.getTile(),
				neighboors);
		while (result.getLength() == 0) {
			checkInterruption();
			bombList.remove(0);
			if (bombList.size() != 0) {
				neighboors = bombList.get(0).getTile().getNeighbors();
				result = this.calculateShortestPath(ourHero, ourHero.getTile(),
						neighboors);
			} else
				break;
		}
		return result;
	}
	
	/**
	 * 
	 * Methode calculant le chemin le plus court pour le bonus et l'attaque.
	 * 
	 * @param ownHero
	 *            notre hero
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            les cases cibles ou le hero peut aller
	 *            
	 * @return 
	 * 		le chemin le plus court a parcourir
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiPath calculateShortestOffensePath(AiHero ownHero,
			AiTile startPoint, List<AiTile> endPoints)
			throws StopRequestException {
		checkInterruption();

		// le chemin le plus court possible
		AiPath shortestPath;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new RedGroupOffenseCostCalculator(this);
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
		shortestPath = astar.processShortestPath(startPoint, endPoints);
		return shortestPath;

	}

	/**
	 * 
	 * Methode calculant le chemin le plus court pour la defense que l'hero peut suivre.
	 * 
	 * @param ownHero
	 *            notre hero
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            les cases cibles ou le hero peut aller
	 *            
	 * @return 
	 * 		le chemin le plus court a parcourir
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new RedGroupDefenseCostCalculator(this);
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
		shortestPath = astar.processShortestPath(startPoint, endPoints);
		return shortestPath;
	}

	/**
	 * Methode calculant la nouvelle action a effectuer
	 * 
	 * @param nextMove
	 * 			Le chemin precis a suivre.
	 *  
	 * @return la nouvelle action de notre hero dans ce chemin
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private AiAction newAction(AiPath nextMove) throws StopRequestException {
		checkInterruption();
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;

		AiAction result = new AiAction(AiActionName.NONE);

		dx = (tiles.get(0).getLine()) - (this.ourHero.getLine());
		// calcul de deplacement sur l'ordonne par rapport a la position de
		// l'hero et la premiere
		// case du chemin le plus court.
		dy = (tiles.get(0).getCol()) - (this.ourHero.getCol());
		boolean check = true;
		if (tiles.get(0).getBlocks().size() != 0) {
			this.nextMoveAttack = null;
			check = false;
		}

		if (pathTiles.size() != 0)
			if (!pathTiles.contains(tiles.get(0))) {
				check = false;
				result = new AiAction(AiActionName.NONE);
			}
		// Determine la direction ou le hero va se deplacer.
		if (check) {
			if (dx < 0 && dy == 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UP);
			} else if (dx < 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UPLEFT);
			} else if (dx == 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.LEFT);
			} else if (dx > 0 && dy == 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWN);
			} else if (dx > 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
			} else if (dx == 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
			} else if (dx > 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
			} else if (dx < 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
			} else {
				result = new AiAction(AiActionName.NONE);
			}
		}

		return result;
	}
}
