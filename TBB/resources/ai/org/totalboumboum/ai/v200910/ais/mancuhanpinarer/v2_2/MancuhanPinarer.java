package org.totalboumboum.ai.v200910.ais.mancuhanpinarer.v2_2;

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
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * Cette classe implemente l'AI du groupe rouge. Actuellement, la strategie de
 * defense et le bonus sont implementes.
 * 
 * 
 * @author Koray Mançuhan
 * @author Özgün Pınarer
 * 
 */
@SuppressWarnings("deprecation")
public class MancuhanPinarer extends ArtificialIntelligence {
	// la case accessible et qui n'est pas dans la portee de la bombe est
	// represente dans la matrice da la zone.
	private final int CASE_SUR = 0;
	// le mur non-destructible represente dans la matrice de la zone
	private final int CASE_INACCESSIBLE = 1;
	// la portee d'une bombe represente dans la matrice de la zone
	private final int CASE_SCOPE = 2;
	// la mur destructible represente dans la matrice de la zone
	private final int MUR_DESTRUCTIBLE = 3;
	// l'item represente dans la matrice de la zone
	private final int ITEM = 4;
	// chemin a suivre pour l'algo defense
	private AiPath nextMove = null;
	// chemin a suivre pour ramasser le bonus
	private AiPath nextMoveBonus = null;
	// notre hero sur la zone
	private AiHero ourHero;
	// le booleen pour determiner s'il est necessaire d'entrer dans l'algo de
	// bonus.


	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
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
		// initialisation de matrice de la zone
		this.initialiseMatrice(matrice, gameZone);
		// remplissage de la matrice de la zone par les items
		this.fillBombsMatrice(matrice, gameZone);
		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);

		// initialisation de notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();

		int destructibleBlocks = 0;

		for (int i = 0; i < gameZone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				checkInterruption();
				if (matrice[i][j] == MUR_DESTRUCTIBLE)
					destructibleBlocks++;
			}
		}

		// On actualise la critere pour entrer dans l'algo recherche de bonus
		// Tant qu'on ne peut pas poser simultanement 6 bombes et que la portee
		// n'est pas 6 cases, on entre dans l'algo.
		if ((ourHero.getBombNumber() == 7 && ourHero.getBombRange() == 7)
				|| destructibleBlocks == 0)
			searchBonus = false;

		// Si le joueur est dans la portee d'une bombe, il entre dans l'algo. de
		// defense
		if (matrice[ourHero.getLine()][ourHero.getCol()] == CASE_SCOPE) {
			this.nextMoveBonus = null;
			this.defenseAlgorithm(gameZone, matrice);
			if (nextMove != null)
				if(nextMove.getLength()!=0)
					result = this.newAction(nextMove);
				else
					nextMove=null;
		} else {
			// Si le critere est vrai, il entre dans l'algo de la recherhe de
			// bonus.
			if (searchBonus) {
				this.nextMove = null;
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
					}
				}
				// Il pose la bombe
				if (dropBomb) {
					result = new AiAction(AiActionName.DROP_BOMB);
				}// Si non il fait une nouvelle action.
				else if (nextMoveBonus != null) {
					if(nextMoveBonus.getLength()!=0)
						result = this.newAction(nextMoveBonus);
					else
						nextMoveBonus=null;
				}
			}
			// L'attaque, pas encore implemente.
			else {
				// Attack
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
	 */
	private void fillBombsMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = CASE_INACCESSIBLE;
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
	 */
	private void fillItemsBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = ITEM;
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
	 */
	/*
	 * private List<AiTile> calculateEndPoints(int[][] matrice, AiZone gameZone)
	 * throws StopRequestException { checkInterruption(); // Liste des CASE_SURs
	 * terminaux. List<AiTile> endPoints = new ArrayList<AiTile>(); // la limite
	 * verticale divisant la zone en 2 parties. int verticalBound =
	 * gameZone.getWidth() / 2; //Si le hero a droite de cette limite, on
	 * remplit dans la liste //des CASE_SURs dans cette partie de la zone. if
	 * (ourHero.getCol() > verticalBound) { for (int i = 0; i <
	 * gameZone.getHeigh(); i++) { checkInterruption(); for (int j =
	 * verticalBound; j < gameZone.getWidth(); j++) { checkInterruption(); if
	 * (matrice[i][j] == CASE_SUR) { if (i != this.ourHero.getLine() && j !=
	 * this.ourHero.getCol()) endPoints.add(gameZone.getTile(i, j)); } } } }
	 * //Si le hero a gauche de cette limite, on remplit dans la liste //des
	 * CASE_SURs dans cette partie de la zone. else if (ourHero.getCol() <=
	 * verticalBound) { for (int i = 0; i < gameZone.getHeigh(); i++) {
	 * checkInterruption(); for (int j = 0; j <= verticalBound; j++) {
	 * checkInterruption(); if (matrice[i][j] == CASE_SUR) { if (i !=
	 * this.ourHero.getLine() && j != this.ourHero.getCol())
	 * endPoints.add(gameZone.getTile(i, j)); } } } } return endPoints; }
	 */

	private List<AiTile> calculateDefenseEndPoints(int[][] matrice,
			AiZone gameZone) throws StopRequestException {
		checkInterruption();
		List<AiTile> endPoints = new ArrayList<AiTile>();
		int heroLine = ourHero.getLine();
		int heroCol = ourHero.getCol();
		int leftBound = heroCol - 3, rightBound = heroCol + 3;
		int upperBound = heroLine - 3, lowerBound = heroLine + 3;
		if (leftBound < 5)
			leftBound = 5;
		if (rightBound > 17)
			rightBound = 17;
		if (upperBound < 1)
			upperBound = 1;
		if (lowerBound > 13)
			lowerBound = 13;

		for (int i = upperBound; i < lowerBound; i++) {
			checkInterruption();
			for (int j = leftBound; j < rightBound; j++) {
				checkInterruption();
				if (matrice[i][j] == CASE_SUR) {
					if (i != this.ourHero.getLine()
							&& j != this.ourHero.getCol())
						endPoints.add(gameZone.getTile(i, j));
				}
			}
		}

		return endPoints;
	}

	/**
	 * Methode implementant l'algo. de bonus.
	 * 
	 * @param gameZone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @throws StopRequestException
	 */
	private void bonusAlgorithm(AiZone gameZone, int[][] matrice)
			throws StopRequestException {
		checkInterruption();
		// Si le prochaine action n'est pas determine, elle va etre calculee ici
		if (nextMoveBonus == null) {
			List<AiItem> itemList = gameZone.getItems();
			if (itemList.size() != 0) {
				List<AiTile> itemTiles = new ArrayList<AiTile>();
				for (int i = 0; i < itemList.size(); i++) {
					checkInterruption();
					if (matrice[itemList.get(i).getLine()][itemList.get(i)
							.getCol()] != CASE_SCOPE)
						itemTiles.add(itemList.get(i).getTile());
				}
				if (findSecurePath(gameZone, matrice, ourHero.getTile(),
						itemTiles)) {
					nextMoveBonus = nextMove;
				}

			} else {
				List<AiBlock> blocks = gameZone.getBlocks();
				List<AiBlock> destructibleBlocks = new ArrayList<AiBlock>();
				List<AiTile> destructibleTiles = new ArrayList<AiTile>();
				for (int j = 0; j < blocks.size(); j++) {
					checkInterruption();
					if (matrice[blocks.get(j).getLine()][blocks.get(j).getCol()] == MUR_DESTRUCTIBLE) {
						destructibleBlocks.add(blocks.get(j));
						List<AiTile> tempList = blocks.get(j).getTile()
								.getNeighbors();
						for (int i = 0; i < tempList.size(); i++) {
							checkInterruption();
							int x = tempList.get(i).getLine(), y = tempList
									.get(i).getCol();
							if (matrice[x][y] == CASE_SUR)
								destructibleTiles.add(tempList.get(i));
						}

					}
				}

				if (findSecurePath(gameZone, matrice, ourHero.getTile(),
						destructibleTiles))
					nextMoveBonus = nextMove;

				
			}
			nextMove = null;

		}
		// Si non, il continue a son mouvement.
		else {

			if (nextMoveBonus.getLength() == 0)
				nextMoveBonus = null;
			else {
				boolean adapt = false;
				List<AiTile> nextTiles = nextMoveBonus.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) {
					if (!nextTiles.get(i).isCrossableBy(ourHero)
							|| matrice[nextTiles.get(i).getLine()][nextTiles
									.get(i).getCol()] == CASE_SCOPE)
						adapt = true;

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

	}

	/**
	 * Methode implementant l'algorithme de defense.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void defenseAlgorithm(AiZone gameZone, int[][] matrice)
			throws StopRequestException {
		checkInterruption();
		// La position actuelle de notre hero
		AiTile startPoint = ourHero.getTile();
		// Les positions finales possibles de notre hero calcule par la methode
		// calculateEndPoints
		List<AiTile> endPoints = this.calculateDefenseEndPoints(matrice,
				gameZone);
		// Si le prochaine action n'est pas determine, elle va etre calculee ici
		if (this.nextMove == null) {
			endPoints = this.calculateDefenseEndPoints(matrice, gameZone);
			leastDangerousPath(matrice, startPoint, endPoints);
		}
		// Si non, il continue a son mouvement.
		else {
			if (nextMove.getLength() == 0)
				nextMove = null;
			else {
				boolean adapt = false;
				if (matrice[nextMove.getLastTile().getLine()][nextMove
						.getLastTile().getCol()] == CASE_SCOPE)
					nextMove = null;
				else {
					List<AiTile> nextTiles = nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) {
						if (!nextTiles.get(i).isCrossableBy(ourHero))
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
	 * La methode qui trouve un chemin qui n'a pas une case de la portee d'une
	 * bombe.
	 * 
	 * @param gameZone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @param startPoint
	 *            La position de l'hero
	 * @param endPoints
	 *            Les cases finaux
	 * @return vrai s'il existe un chemin sans la portee d'une bombe
	 * 
	 * @throws StopRequestException
	 */
	private boolean findSecurePath(AiZone gameZone, int[][] matrice,
			AiTile startPoint, List<AiTile> endPoints)
			throws StopRequestException {
		checkInterruption();
		boolean result = true;
		int i = 0;
		this.nextMove = this.calculateShortestPath(ourHero, startPoint,
				endPoints);
		while (i < nextMove.getLength() && result) {
			checkInterruption();
			int x = nextMove.getTile(i).getLine(), y = nextMove.getTile(i)
					.getCol();
			if (matrice[x][y] == CASE_SCOPE) {
				endPoints.remove(nextMove.getTile(nextMove.getLength() - 1));
				this.nextMove = this.calculateShortestPath(ourHero, startPoint,
						endPoints);
				i = -1;
				if (nextMove.getLength() == 0)
					result = false;
			}
			i++;
		}
		return result;
	}

	/**
	 * La methode qui trouve un chemin qui a la moins de cases possibles dans la
	 * portee d'une bombe.
	 * 
	 * @param matrice
	 *            La matrice de zone
	 * @param startPoint
	 *            La position de l'hero
	 * @param endPoints
	 *            Les cases finaux
	 * @throws StopRequestException
	 */
	private void leastDangerousPath(int[][] matrice, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		System.out.println("Yeni Giris");
		int[] numberScopes = new int[endPoints.size()];
				
		AiPath[] alternativePaths = new AiPath[endPoints.size()];
		for (int i = 0; i < endPoints.size(); i++) {
			checkInterruption();
			List<AiTile> tempElement = new ArrayList<AiTile>();
			tempElement.add(endPoints.get(i));
			AiPath tempPath = this.calculateShortestPath(ourHero, startPoint,
					tempElement);
			alternativePaths[i] = tempPath;
			System.out.println("Alternatif Yol: "+alternativePaths[i].toString());
			for (int j = 0; j < tempPath.getLength(); j++) {
				checkInterruption();
				int x = tempPath.getTile(j).getLine(), y = tempPath.getTile(j)
						.getCol();
				if (matrice[x][y] == CASE_SCOPE)
					numberScopes[i]++;
			}
			System.out.println("Scope Miktari: "+numberScopes[i]);
			tempElement.clear();
		}
		int min = Integer.MAX_VALUE, indexMin = 0;
		for (int m = 0; m < numberScopes.length; m++) {
			checkInterruption();
			if ((numberScopes[m] < min) && (alternativePaths[m].getLength()!=0)) {
				min = numberScopes[m];
				indexMin = m;
				if(min==3)
					break;
			}

		}
		this.nextMove = alternativePaths[indexMin];
		System.out.println("Secilen Yol: "+nextMove.toString());
	}

	/**
	 * 
	 * Methode calculant le chemin le plus court que l'hero peut suivre.
	 * 
	 * @param ownHero
	 *            l'hero sollicite par notre AI
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	private AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
		shortestPath = astar.processShortestPath(startPoint, endPoints);
		return shortestPath;
	}

	/**
	 * Methode calculant la nouvelle action
	 * 
	 * @return la nouvelle action de notre hero
	 * @throws StopRequestException
	 */
	private AiAction newAction(AiPath nextMove) throws StopRequestException {
		checkInterruption();
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;
		
		
		dx = (tiles.get(0).getLine()) - (this.ourHero.getLine());
		// calcul de deplacement sur l'ordonne par rapport a la position de
		// l'hero et la premiere
		// case du chemin le plus court.
		dy = (tiles.get(0).getCol()) - (this.ourHero.getCol());

		// Determine la direction ou le hero va se deplacer.
		if (dx < 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.UP);
		} else if (dx < 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} else if (dx == 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} else if (dx > 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} else if (dx > 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} else if (dx == 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} else if (dx > 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		} else if (dx < 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		} else {
			return new AiAction(AiActionName.NONE);
		}

	}
}
