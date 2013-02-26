package org.totalboumboum.ai.v200708.ais.baydarcamci;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * Classe implémentant un comportement non-violent : le robot ne pose jamais de
 * bombe, et essaie juste de s'éloigner le plus possible des dangers existants,
 * i.e. par ordre de dangerosité croissante : le feu, les bombes, les autres
 * joueurs.
 * 
 * @author Ayça Selva Baydar
 * @author Ayfer Camcı
 * 
 */
@SuppressWarnings("deprecation")
public class BaydarCamci extends ArtificialIntelligence {
	/** */
	public static final long serialVersionUID = 1L;

	/** dernière mouvement du personnage */
	private Integer lastMove;

	/** mouvement precedent du personnage */
	private Integer lastPreMove;

	/** */
	int thePutBomb[] = { -1, -1 }; // yol acmak icin bomba biraktiginda eski
									// pozisyonuna geri don
	/** */
	int runAwayStep = 0;
	/** */
	public int bombPower = 0;
	/** */
	public List<int[]> path = new ArrayList<int[]>();

	/**
	 * Constructeur.
	 */
	public BaydarCamci() {
		super("BaydarCamc");
	}

	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (firstTime)
			firstTime = false;
		else {
			int x = getOwnPosition()[0];
			int y = getOwnPosition()[1];
			int dangerPos[] = getClosestBlockPosition(x, y, AI_BLOCK_FIRE);
			if (dangerPos[0] + 1 != x || dangerPos[0] - 1 != 0
					|| dangerPos[1] + 1 != y || dangerPos[1] - 1 != y) {
				dangerPos = getClosestBlockPosition(x, y, AI_BLOCK_BOMB);
				bombPower = getBombPowerAt(dangerPos[0], dangerPos[1]);
			}
			if (dangerPos[0] != -1)
				return onDanger(x, y, dangerPos[0], dangerPos[1], bombPower);
			if (dangerPos[0] == -1) {
				result = moveNow(x, y);
			}
		}

		return result;
	}

	/**
	 * Controle la mouvement du personnage par l'algorithm A*. il construire une
	 * path pour aller au milieu de la zone.
	 * 
	 * @param x
	 *            : la position x de personnage
	 * @param y
	 *            : la position y de personnage
	 * @return la mouvement possible
	 */
	public Integer moveNow(int x, int y) {

		AStar as = new AStar(getZoneMatrix(), 500);
		int[] aim = { getZoneMatrixDimX() / 2, getZoneMatrixDimY() / 2 };
		as.findPath(getOwnPosition(), aim, path);
		// on ne peut pas initialiser le path d'algorithm A*, alors:

		return dummyMethod(x, y);

	}

	/**
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param bombX
	 *            Description manquante !
	 * @param bombY
	 *            Description manquante !
	 * @param bombPower
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer onDanger(int x, int y, int bombX, int bombY, int bombPower) {
		if (x == bombX && bombY < y) {
			if (bombY + bombPower > y) {
				return downBomb(x, y);
			}
		}
		if (x == bombX && bombY > y) {
			if (bombY - bombPower < y) {
				return upBomb(x, y);
			}
		}
		if (bombY == y && bombX > x) {
			if (bombX - bombPower < x) {
				return leftBomb(x, y);
			}
		}
		if (bombY == y && bombX < x) {
			if (bombX + bombPower > x) {
				return rightBomb(x, y);

			}
		} else if (x == bombX && y == bombY) {
			return onBomb(x, y);
		}
		return getPutBombPosition(x, y);
	}

	/**
	 * Parmi les blocs dont le type correspond à la valeur blockType passée en
	 * paramètre, cette méthode cherche lequel est le plus proche du point de
	 * coordonnées (x,y) passées en paramètres. Le résultat prend la forme d'un
	 * tableau des deux coordonnées du bloc le plus proche. Le tableau est
	 * contient des -1 s'il n'y a aucun bloc du bon type dans la zone de jeu.
	 * 
	 * @param x
	 *            position de référence
	 * @param y
	 *            position de référence
	 * @param blockType
	 *            le type du bloc recherché
	 * @return les coordonnées du bloc le plus proche
	 */
	private int[] getClosestBlockPosition(int x, int y, int blockType) {
		int minDistance = Integer.MAX_VALUE;
		int result[] = { -1, -1 };
		int[][] matrix = getZoneMatrix();
		for (int i = 0; i < getZoneMatrixDimX(); i++)
			for (int j = 0; j < getZoneMatrixDimY(); j++)
				if (matrix[i][j] == blockType) {
					int tempDistance = distance(x, y, i, j);
					if (tempDistance < minDistance) {
						minDistance = tempDistance;
						result[0] = i;
						result[1] = j;
					}
				}
		return result;
	}

	/**
	 * Calcule et renvoie la distance de Manhattan (cf. :
	 * http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29) entre le
	 * point de coordonnées (x1,y1) et celui de coordonnées (x2,y2).
	 * 
	 * @param x1
	 *            position du premier point
	 * @param y1
	 *            position du premier point
	 * @param x2
	 *            position du second point
	 * @param y2
	 *            position du second point
	 * @return la distance de Manhattan entre ces deux points
	 */
	private int distance(int x1, int y1, int x2, int y2) {
		int result = 0;
		result = result + Math.abs(x1 - x2);
		result = result + Math.abs(y1 - y2);
		return result;
	}

	/**
	 * Indique si le déplacement dont le code a été passé en paramètre est
	 * possible pour un personnage situé en (x,y).
	 * 
	 * @param x
	 *            position du personnage
	 * @param y
	 *            position du personnage
	 * @param move
	 *            le déplacement à étudier
	 * @return vrai si ce déplacement est possible
	 */
	public boolean isMovePossible(int x, int y, int move) {
		boolean result;
		// calcum
		switch (move) {
		case ArtificialIntelligence.AI_ACTION_GO_UP:
			result = y > 0 && !isObstacle(x, y - 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_DOWN:
			result = y < (getZoneMatrixDimY() - 1) && !isObstacle(x, y + 1);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_LEFT:
			result = x > 0 && !isObstacle(x - 1, y);
			break;
		case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
			result = x < (getZoneMatrixDimX() - 1) && !isObstacle(x + 1, y);
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	/**
	 * Indique si la case située à la position passée en paramètre constitue un
	 * obstacle pour un personnage : bombe, feu, mur.
	 * 
	 * @param x
	 *            position à étudier
	 * @param y
	 *            position à étudier
	 * @return vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int x, int y) {
		int[][] matrix = getZoneMatrix();
		boolean result = false;
		// bombes
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y] == ArtificialIntelligence.AI_BLOCK_FIRE;
		// murs
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result
				|| matrix[x][y] == ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		result = result
				|| (x == getNextShrinkPosition()[0] && y == getNextShrinkPosition()[1]);
		return result;
	}

	/**
	 * Renvoie la liste de tous les déplacements possibles pour un personnage
	 * situé à la position (x,y)
	 * 
	 * @param x
	 *            position du personnage
	 * @param y
	 *            position du personnage
	 * @return la liste des déplacements possibles
	 */
	public Vector<Integer> getPossibleMoves(int x, int y) {
		Vector<Integer> result = new Vector<Integer>();
		for (int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++)
			if (isMovePossible(x, y, move))
				result.add(move);
		return result;
	}

	/**
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer dummyMethod(int x, int y) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		// les locations du personnage au debut du jeu
		if (x <= 8 && y <= 7)
			result = solUstKose(x, y);
		if (x > 8 && y <= 7)
			result = sagUstKose(x, y);
		if (x <= 8 && y >= 7)
			result = solAltKose(x, y);
		if (x > 8 && y >= 7)
			result = sagAltKose(x, y);
		if (x == 8 && y == 7)
			result = merkez(x, y);
		return result;

	}

	/**
	 * moi et la bombe et dans le meme cas
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return mouvement
	 */
	public Integer onBomb(int x, int y) {
		if (getBombPosition() == ArtificialIntelligence.AI_DIR_DOWN) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_RIGHT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;

		}
		if (getBombPosition() == ArtificialIntelligence.AI_DIR_UP) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_DOWN) == true)
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_RIGHT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;

		}
		if (getBombPosition() == ArtificialIntelligence.AI_DIR_LEFT) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			else
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;

		}
		if (getBombPosition() == ArtificialIntelligence.AI_DIR_RIGHT) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			else
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;
		}
		if (getBombPosition() == ArtificialIntelligence.AI_DIR_NONE) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_LEFT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_DOWN) == true)
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_RIGHT) == true)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		}

		return ArtificialIntelligence.AI_ACTION_DO_NOTHING;

	}

	/**
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return la mouvement
	 */
	public Integer solUstKose(int x, int y) {
		Integer result = solUstKoseHareketleri(x, y); // rutin durumsa zigzaglar
														// cizerek merkeze
														// ulasmaya calis
		result = Move(x, y, result);
		lastPreMove = lastMove;
		lastMove = result;

		return result;
	}

	/**
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return la mouvement
	 */
	public Integer sagUstKose(int x, int y) {
		Integer result = sagUstKoseHareketleri(x, y); // rutin durumsa zigzaglar
														// cizerek merkeze
														// ulasmaya calis
		return Move(x, y, result);

	}

	/**
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return la mouvement
	 */
	public Integer solAltKose(int x, int y) {
		Integer result = solAltKoseHareketleri(x, y); // rutin durumsa zigzaglar
														// cizerek merkeze
														// ulasmaya calis
		return Move(x, y, result);

	}

	/**
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return la mouvement
	 */
	public Integer sagAltKose(int x, int y) {
		Integer result = sagAltKoseHareketleri(x, y); // rutin durumsa zigzaglar
														// cizerek merkeze
														// ulasmaya calis
		return Move(x, y, result);

	}

	/**
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @param result
	 *            mouvement deja obtenu
	 * @return la mouvement
	 */
	public Integer Move(int x, int y, Integer result) {

		lastMove = result;

		// s'il y une danger?
		int dangerPos[] = getClosestBlockPosition(x, y, AI_BLOCK_FIRE);
		if (dangerPos[0] == -1)
			dangerPos = getClosestBlockPosition(x, y, AI_BLOCK_BOMB);

		if (lastMove == ArtificialIntelligence.AI_ACTION_PUT_BOMB) {
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_UP)
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_DOWN)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_LEFT)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_RIGHT)
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;
			lastMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
			lastPreMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		}

		if (dangerPos[0] == -1) {
			dangerPos = getClosestBlockPosition(x, y, AI_BLOCK_BOMB);
			bombPower = getBombPowerAt(dangerPos[0], dangerPos[1]);
		}

		if (dangerPos[0] == -1 && dangerPos[0] == -1
				&& result == ArtificialIntelligence.AI_ACTION_DO_NOTHING) {
			// aucune danger. putBomb:
			thePutBomb = getOwnPosition();
			lastPreMove = lastMove;
			result = ArtificialIntelligence.AI_ACTION_PUT_BOMB;

		}

		else if (result == ArtificialIntelligence.AI_ACTION_DO_NOTHING) {
			// il y une danger. choisir votre deplacement:
			int bombDir = bombaNerde(x, y, dangerPos[0], dangerPos[1]);
			if (bombDir == 5)
				return onBomb(x, y);
			if (bombDir == 6)
				return rightBomb(x, y);
			if (bombDir == 4)
				return leftBomb(x, y);
			if (bombDir == 2)
				return downBomb(x, y);
			if (bombDir == 8)
				return upBomb(x, y);
		}
		return result;
	}

	/**
	 * moi est au milieu de la zone.
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer merkez(int x, int y) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (getTimeBeforeShrink() == -1) {
			// on détermine les déplacements possibles
			Vector<Integer> possibleMoves = getPossibleMoves(x, y);
			// on teste s'il est possible d'effectuer le même déplacement que
			// précédemment
			if (possibleMoves.contains(lastMove))
				result = lastMove;
			// sinon : soit on se déplace, soit on pose une bombe
			else if (possibleMoves.size() > 0) { // on peut poser une bombe si
													// on est à la fois dans un
													// cul de sac
													// (1 seul déplacement
													// possible) et sur une case
													// vide
				if (possibleMoves.size() < 2
						&& getZoneMatrix()[x][y] == ArtificialIntelligence.AI_BLOCK_EMPTY)
					possibleMoves
							.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
				// on détermine aléatoirement l'action qui va être effectuée
				int index;
				do {
					index = (int) (Math.random() * (possibleMoves.size()));
				} while (index == possibleMoves.size());
				result = possibleMoves.get(index);
			}
			lastMove = result;
			return result;
		}

		return result;
	}

	/**
	 * la mouvement du personnage s'il commence au jeu au haut gauche de la zone
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer solUstKoseHareketleri(int x, int y) {

		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (thePutBomb[0] == -1) {
			if (x == 7 && y == 7)
				return result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_DOWN))
				result = ArtificialIntelligence.AI_ACTION_GO_DOWN;
		} else {
			if (x != thePutBomb[0] || y != thePutBomb[1]) {
				return getPutBombPosition(x, y);
			}
			thePutBomb[0] = -1;
		}
		return result;
	}

	/**
	 * la mouvement du personnage s'il commence au jeu au haut droit de la zone
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer sagUstKoseHareketleri(int x, int y) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (thePutBomb[0] == -1) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT))
				result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_DOWN))
				result = ArtificialIntelligence.AI_ACTION_GO_DOWN;
		}
		/*
		 * else { if(x !=thePutBomb[0] || y != thePutBomb[1]) { return
		 * getPutBombPosition(x,y); } thePutBomb[0]= -1; }
		 */
		return result;
	}

	/**
	 * la mouvement du personnage s'il commence au jeu au bas gauche de la zone
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer solAltKoseHareketleri(int x, int y) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (thePutBomb[0] == -1) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT))
				result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_UP))
				result = ArtificialIntelligence.AI_ACTION_GO_UP;
		} else {
			if (x != thePutBomb[0] || y != thePutBomb[1]) {
				return getPutBombPosition(x, y);
			}
			thePutBomb[0] = -1;
		}
		return result;
	}

	/**
	 * la mouvement du personnage s'il commence au jeu au bas gauche de la zone
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer sagAltKoseHareketleri(int x, int y) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if (thePutBomb[0] == -1) {
			if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT))
				result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if (isMovePossible(x, y,
					ArtificialIntelligence.AI_ACTION_GO_UP))
				result = ArtificialIntelligence.AI_ACTION_GO_UP;
		} else {
			if (x != thePutBomb[0] || y != thePutBomb[1]) {
				return getPutBombPosition(x, y);
			}
			thePutBomb[0] = -1;
		}
		return result;
	}

	/**
	 * la position du bombe par rapport a moi
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param bombX
	 *            Description manquante !
	 * @param bombY
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public int bombaNerde(int x, int y, int bombX, int bombY) {
		// bomba sagimdaysa 6 solumdaysa 4 yukardaysa 8 asagidaysa 2 altimdaysa
		// 5

		if (x < bombX && y == bombY)
			return 6;
		if (x > bombX && y == bombY)
			return 4;
		if (x == bombX && y > bombY)
			return 8;
		if (x == bombX && y < bombY)
			return 2;
		if (x == bombX && y == bombY)
			return 5;

		return solUstKoseHareketleri(x, y);
	}

	/**
	 * bombe situe au droite
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return ?
	 */
	public Integer rightBomb(int x, int y) {
		if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_DOWN))
			return ArtificialIntelligence.AI_ACTION_GO_DOWN;
		else if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_UP))
			return ArtificialIntelligence.AI_ACTION_GO_UP;
		else
			return ArtificialIntelligence.AI_ACTION_GO_LEFT;
	}

	/**
	 * bombe se situe au gauche
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return ?
	 */
	public Integer leftBomb(int x, int y) {
		if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_DOWN))
			return ArtificialIntelligence.AI_ACTION_GO_DOWN;
		else if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_UP))
			return ArtificialIntelligence.AI_ACTION_GO_UP;
		else
			return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
	}

	/**
	 * bombe est au dessous de moi
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return ?
	 */
	public Integer downBomb(int x, int y) {
		if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT))
			return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT))
			return ArtificialIntelligence.AI_ACTION_GO_LEFT;
		else
			return ArtificialIntelligence.AI_ACTION_GO_UP;
	}

	/**
	 * bombe est au dessus de moi
	 * 
	 * @param x
	 *            mon x
	 * @param y
	 *            mon y
	 * @return ?
	 */
	public Integer upBomb(int x, int y) {
		if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_RIGHT))
			return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if (isMovePossible(x, y, ArtificialIntelligence.AI_ACTION_GO_LEFT))
			return ArtificialIntelligence.AI_ACTION_GO_LEFT;
		else
			return ArtificialIntelligence.AI_ACTION_GO_DOWN;
	}

	/**
	 * se deplace au case de bombe
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @return Description manquante !
	 */
	public Integer getPutBombPosition(int x, int y) {
		if (lastPreMove != null) {
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_DOWN)
				return ArtificialIntelligence.AI_ACTION_GO_UP;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_UP)
				return ArtificialIntelligence.AI_ACTION_GO_DOWN;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_LEFT)
				return ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			if (lastPreMove == ArtificialIntelligence.AI_ACTION_GO_RIGHT)
				return ArtificialIntelligence.AI_ACTION_GO_LEFT;
		}
		return ArtificialIntelligence.AI_ACTION_DO_NOTHING;

	}
}
