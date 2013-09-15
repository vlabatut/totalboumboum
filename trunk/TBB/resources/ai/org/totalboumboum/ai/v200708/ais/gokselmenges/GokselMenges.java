package org.totalboumboum.ai.v200708.ais.gokselmenges;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Atahan Göksel
 * @author Erdem Mengeş
 * 
 */
@SuppressWarnings("deprecation")
public class GokselMenges extends ArtificialIntelligence {

	/** */
	public static final long serialVersionUID = 7954120902478357292L;
	/** */
	int UP;
	/** */
	int DOWN;
	/** */
	int RIGHT;
	/** */
	int LEFT;
	/** */
	int MIDDLE;

	/** */
	int coutInitial = 0;
	/** */
	List<Integer> costs = new ArrayList<Integer>();
	/** */
	int x = 0;
	/** */
	int y = 0;
	/** */
	Point Goal;
	/** */
	boolean path = false;
	/** */
	int j1 = 0;
	/** */
	int i1 = 0;
	/** */
	int path1 = -1;
	/** */
	int bombNumber = 1;
	/** */
	boolean escaping = false;
	/** */
	boolean bombNumberIncremented = false;

	/** */
	boolean escapingFromBombIteratorNumber100 = false;

	/** */
	int[][] oldSituation = null;
	/** */
	List<Point> bombPosition = new ArrayList<Point>();
	/** */
	int getPlayerCount = -1;
	/** */
	List<Point> blockPoints = new ArrayList<Point>();

	/** */
	boolean poserBombe = false;

	/** */
	List<Point> moveCosts = new ArrayList<Point>();
	/** */
	List<Point> zoneAccessible = new ArrayList<Point>();
	/** */
	List<Point> zoneAccessible2 = new ArrayList<Point>();

	/** */
	List<Point> zoneAccessibleEscapeBomb = new ArrayList<Point>();
	/** */
	List<Point> movedPoints = new ArrayList<Point>();
	/** */
	List<Integer> pathToGo = new ArrayList<Integer>();

	/** les couts pour l'algorithm A* pour trouver le meilleur chemin */
	final int cost_empty = 1;
	/** */
	final int cost_softwall = 3;
	/** */
	final int cost_bonus = 2;
	/** */
	final int cost_bomba = 100000;
	/** */
	final int cost_feu = 100000;
	/** */
	final int cost_hardwall = 100000;
	/** */
	final int cost_portee = 1000;
	/** */
	final int cost_distance_multiplier = 10;
	/** */
	final int cost_ennemi = 5;

	/**
	 * 
	 */
	public GokselMenges()

	{
		super("GokslMengs");

	}

	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception {
		int result = AI_ACTION_DO_NOTHING;
		if (firstTime)
			firstTime = false;
		else {

			x = getOwnPosition()[0];
			y = getOwnPosition()[1];

			Point CurrentPlayerPos = new Point(x, y);

			bombNumber += isBombExploded();

			if (path == false) {

				Goal = getClosestPlayerPositionAlive(CurrentPlayerPos);

				zoneAccessible.clear();

				getZoneAccessible(CurrentPlayerPos);

				Goal = getObjective(zoneAccessible);
				movedPoints.clear();
				coutInitial = 0;

				if (Goal == null) {

					Goal = getClosestPlayerPositionAlive(CurrentPlayerPos);

					if (CurrentPlayerPos != Goal) {
						path = true;
						if (bombNumber > 0) {
							poserBombe = true;

							oldSituation = getZoneMatrix();
							if (!bombPosition.contains(CurrentPlayerPos)) {
								if ((getZoneMatrix()[CurrentPlayerPos.x][CurrentPlayerPos.y] != 3)
										&& (getZoneMatrix()[CurrentPlayerPos.x][CurrentPlayerPos.y] != 7)) {

									bombPosition.add(CurrentPlayerPos);
									bombNumber--;

									return AI_ACTION_PUT_BOMB;
								} else {

									return AI_ACTION_DO_NOTHING;
								}
							} else {

								return AI_ACTION_DO_NOTHING;
							}
						} else {

							path = false;

							return AI_ACTION_DO_NOTHING;
						}
					} else {

						path = false;
					}
				}

			} else {
				if (poserBombe == true) {
					movedPoints.clear();
					pathToGo.clear();
					Goal = getObjectiveRunFromBomb(
							CurrentPlayerPos,
							BringClosestBlockPosition(CurrentPlayerPos,
									AI_BLOCK_BOMB), true);

					if (Goal.equals(new Point(-10, -10)))
						return AI_ACTION_DO_NOTHING;

					getPathBomb(CurrentPlayerPos, Goal);
					pathToGo = refine(movedPoints);
					movedPoints.clear();
					path = true;
					poserBombe = false;
					escapingFromBombIteratorNumber100 = true;

				}

				costs.clear();
				moveCosts.clear();
			}

			// int i = 0;
			movedPoints.add(CurrentPlayerPos);

			if (path == false) {

				pathToGo.clear();

				int pathIterationCounter = 0;

				int pathIterationCounterFinish;
				if (escapingFromBombIteratorNumber100 == true) {

					pathIterationCounterFinish = 10;
					escapingFromBombIteratorNumber100 = false;
				} else if (getDistance(CurrentPlayerPos,
						getClosestPlayerPositionAlive(CurrentPlayerPos)) < 6) {
					pathIterationCounterFinish = 1;
				} else
					pathIterationCounterFinish = 4;

				while ((x != Goal.x) || (y != Goal.y)
						&& (pathIterationCounter < pathIterationCounterFinish)) {
					pathIterationCounter++;

					List<Point> MoveablePoints = new ArrayList<Point>();
					List<Point> temp = getPossibleMoves(x, y, false);
					MoveablePoints.addAll(temp);
					List<Integer> MoveablePointDistances = getPointDistances(
							MoveablePoints, Goal);

					int pt;

					try {
						pt = minimumCostCalculation(MoveablePoints,
								MoveablePointDistances)[1];
					} catch (RuntimeException e) {
						//e.printStackTrace();
						return AI_ACTION_DO_NOTHING;
					}

					x = moveCosts.get(pt).x;
					y = moveCosts.get(pt).y;

					// i++;

					RemoveFromList(pt);

					MoveablePoints.clear();
					MoveablePointDistances.clear();

				}

				pathToGo = refine(movedPoints);
				movedPoints.clear();

				path = true;

			}

			if (path == true) {

				while (i1 < pathToGo.size()) {
					try {
						result = pathToGo.get(i1);

					} catch (Exception ex) {
						//
					}
					if (checkNextMoveOK(result) == true) {

						j1++;
						if ((j1 % 8 == 0) && (j1 != 0)) {
							i1++;

						}

						return result;

					} else {
						path = false;
						poserBombe = false;
						return AI_ACTION_DO_NOTHING;
					}
				}

				path = false;
				i1 = 0;
				j1 = 0;
				if (bombNumberIncremented == true) {

					bombNumber++;
					bombNumberIncremented = false;
				}

			}

			i1 = 0;
			j1 = 0;
		}
		return AI_ACTION_DO_NOTHING;
	}

	/**
	 * 
	 * @param directionToGO
	 *            Description manquante !
	 * @return Description manquante !
	 */
	// renvoi true si le prochaine pas est sur.
	// @param directionToGo le sens a aller
	// @returns vrai si le prochain pas est sur et false sinon

	public boolean checkNextMoveOK(int directionToGO) {
		int result = 0;
		boolean currentmoveBAD = false;
		Point CurrentPos = new Point(getOwnPosition()[0], getOwnPosition()[1]);
		if ((BetterMatrix()[getOwnPosition()[0]][getOwnPosition()[1]] == 7)
				|| (BetterMatrix()[getOwnPosition()[0]][getOwnPosition()[1]] == 4)) {
			result++;

			currentmoveBAD = true;

		}

		if (directionToGO == AI_ACTION_GO_UP) {
			Point temp = new Point(x, y - 1);
			// BOMBNUMBER
			if (BetterMatrix()[x][y - 1] == AI_BLOCK_ITEM_BOMB) {
				bombNumberIncremented = true;
			}

			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x][y - 1] == 7)
						|| (BetterMatrix()[x][y - 1] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;

				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_DOWN) {
			Point temp = new Point(x, y + 1);
			// BOMBNUMBER
			if (BetterMatrix()[x][y + 1] == AI_BLOCK_ITEM_BOMB) {
				bombNumberIncremented = true;
			}

			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x][y + 1] == 7)
						|| (BetterMatrix()[x][y + 1] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;

				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_LEFT) {
			Point temp = new Point(x - 1, y);
			// BOMBNUMBER
			if (BetterMatrix()[x - 1][y] == AI_BLOCK_ITEM_BOMB) {

				bombNumberIncremented = true;
			}

			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x - 1][y] == 7)
						|| (BetterMatrix()[x - 1][y] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;

				}
			} else {
				return true;
			}
		} else if (directionToGO == AI_ACTION_GO_RIGHT) {
			Point temp = new Point(x + 1, y);
			// BOMBNUMBER
			if (BetterMatrix()[x + 1][y] == AI_BLOCK_ITEM_BOMB) {

				bombNumberIncremented = true;
			}

			if (!CurrentPos.equals(temp)) {
				if ((BetterMatrix()[x + 1][y] == 7)
						|| (BetterMatrix()[x + 1][y] == 4)
						|| (BetterMatrix()[x][y - 1] == 3)) {
					result++;

				}
			} else {

				return true;
			}
		}

		if (currentmoveBAD == true) {
			return true;
		} else if ((result == 1) && (currentmoveBAD == true)) {
			return true;
		} else if ((result == 1) && (currentmoveBAD == false)) {
			path = false;
			return false;
		} else if (result == 0) {
			return true;
		} else {

			return true;
		}

	}

	/**
	 * 
	 * @return ?
	 */
	// @returns renvoi 0 si la bombe est detruite.
	public int isBombExploded() {
		int result1 = 0;
		for (int i = 0; i < bombPosition.size(); i++) {

			if (BetterMatrix()[bombPosition.get(i).x][bombPosition.get(i).y] == 0) {
				bombPosition.remove(i);
				result1++;
			}
		}
		return result1;
	}

	/**
	 * 
	 * @param CurrentPlayerPos
	 *            Description manquante !
	 * @param bomb
	 *            Description manquante !
	 * @param seven
	 *            Description manquante !
	 * @return Description manquante !
	 */
	// @returns renvoi le point a aller pour eviter la bombe
	public Point getObjectiveRunFromBomb(Point CurrentPlayerPos, Point bomb,
			boolean seven) {

		Point result = new Point();

		zoneAccessible.clear();
		getZoneAccessibleForBomb(CurrentPlayerPos);

		for (int i = 0; i < zoneAccessible.size(); i++)
			if (getDistance(CurrentPlayerPos, zoneAccessible.get(i)) == 0)
				zoneAccessible.remove(i);

		if (seven == true) {
			zoneAccessible2.addAll(zoneAccessible);

			for (int i = 0; i < zoneAccessible.size(); i++) {

				if (BetterMatrix()[zoneAccessible.get(i).x][zoneAccessible
						.get(i).y] == 7) {

					zoneAccessible.remove(i);
					i--;
				}
			}
		}

		int temp = 0;

		if(zoneAccessible!=null && !zoneAccessible.isEmpty())
			temp = getDistance(CurrentPlayerPos, zoneAccessible.get(0));
		else
			return new Point(-10, -10);
		
		int tempind = 0;

		for (int i = 0; i < zoneAccessible.size(); i++) {
			if (temp > getDistance(CurrentPlayerPos, zoneAccessible.get(i))) {
				temp = getDistance(CurrentPlayerPos, zoneAccessible.get(i));
				tempind = i;
			}
		}

		result = zoneAccessible.get(tempind);

		return result;
	}

	/**
	 * 
	 * @param p
	 *            Description manquante !
	 */
	// change le vecteur contenant les pas possibles.
	// @param p les coordonnees de la bombe
	public void getZoneAccessibleForBomb(Point p) {

		if (!zoneAccessible.contains(p))
			zoneAccessible.add(p);

		List<Point> result = getPossibleMovesForBomb(p.x, p.y, zoneAccessible);

		for (int i = 0; i < result.size(); i++) {
			getZoneAccessibleForBomb(result.get(i));
		}

	}

	/**
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param zone
	 *            Description manquante !
	 * @return Description manquante !
	 */
	// change le vecteur contenant les pas possibles.
	// @param p les coordonnees de la bombe
	public List<Point> getPossibleMovesForBomb2(int x, int y,
			List<Point> zone) {

		int[] direction = new int[4];

		List<Point> points = new ArrayList<Point>();

		int mat[][] = BetterMatrix();
		if(y-1>=0)
			direction[0] = mat[x][y - 1]; // UP
		else
			direction[0] = -1;
		if(y+1<mat[0].length)
			direction[1] = mat[x][y + 1]; // DOWN
		else
			direction[1] = -1;
		if(x-1>=0)
			direction[2] = mat[x - 1][y]; // LEFT
		else
			direction[2] = -1;
		if(x+1<mat.length)
			direction[3] = mat[x + 1][y]; // RIGHT
		else
			direction[3] = -1;

		if ((zone.contains(new Point(x, y - 1)))) {
			if (!movedPoints.contains(new Point(x, y - 1))) {
				if ((direction[0] == 0) || (direction[0] == 5)
						|| (direction[0] == 6) || (direction[0] == 7))
					points.add(new Point(x, y - 1));
			}
		}
		if (zone.contains(new Point(x, y + 1))) {
			if (!movedPoints.contains(new Point(x, y + 1))) {
				if ((direction[1] == 0) || (direction[1] == 5)
						|| (direction[1] == 6) || (direction[1] == 7))
					points.add(new Point(x, y + 1));
			}
		}
		if (zone.contains(new Point(x - 1, y))) {
			if (!movedPoints.contains(new Point(x - 1, y))) {
				if ((direction[2] == 0) || (direction[2] == 5)
						|| (direction[2] == 6) || (direction[2] == 7))
					points.add(new Point(x - 1, y));
			}
		}
		if (zone.contains(new Point(x + 1, y))) {
			if (!movedPoints.contains(new Point(x + 1, y))) {
				if ((direction[3] == 0) || (direction[3] == 5)
						|| (direction[3] == 6) || (direction[3] == 7))
					points.add(new Point(x + 1, y));
			}
		}

		return points;

	}

	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param zone
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	// change le vecteur contenant les pas possibles.
	// @param p les coordonnees de la bombe
	public List<Point> getPossibleMovesForBomb(int x, int y,
			List<Point> zone) {

		int[] direction = new int[4];

		List<Point> points = new ArrayList<Point>();

		try {
			direction[0] = oldSituation[x][y - 1]; // UP
		} catch (RuntimeException e) {
			direction[0] = -1;

		}
		try {
			direction[1] = oldSituation[x][y + 1]; // DOWN
		} catch (RuntimeException e) {
			direction[1] = -1;

		}
		try {
			direction[2] = oldSituation[x - 1][y]; // LEFT
		} catch (RuntimeException e) {
			direction[2] = -1;

		}
		try {
			direction[3] = oldSituation[x + 1][y]; // RIGHT
		} catch (RuntimeException e) {
			direction[3] = -1;

		}

		if (!zone.contains(new Point(x, y - 1))) {
			if ((direction[0] == 0) || (direction[0] == 5)
					|| (direction[0] == 6) || (direction[0] == 7))
				points.add(new Point(x, y - 1));
		}
		if (!zone.contains(new Point(x, y + 1))) {
			if ((direction[1] == 0) || (direction[1] == 5)
					|| (direction[1] == 6) || (direction[1] == 7))
				points.add(new Point(x, y + 1));
		}
		if (!zone.contains(new Point(x - 1, y))) {
			if ((direction[2] == 0) || (direction[2] == 5)
					|| (direction[2] == 6) || (direction[2] == 7))
				points.add(new Point(x - 1, y));
		}
		if (!zone.contains(new Point(x + 1, y))) {
			if ((direction[3] == 0) || (direction[3] == 5)
					|| (direction[3] == 6) || (direction[3] == 7))
				points.add(new Point(x + 1, y));
		}

		return points;

	}

	/**
	 * 
	 * @param CurrentPlayerPos
	 * 		Description manquante !
	 * @param Goal
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public List<Point> getPathBomb(Point CurrentPlayerPos, Point Goal) {

		movedPoints.clear();
		List<Point> result = new ArrayList<Point>();
		// int i = 0;
		pathToGo.clear();
		movedPoints.add(CurrentPlayerPos);
		int x = CurrentPlayerPos.x;
		int y = CurrentPlayerPos.y;
		boolean changed = true;
		while (((x != Goal.x) || (y != Goal.y)) && changed) {

			changed = false;
			List<Point> MoveablePoints = new ArrayList<Point>();
			MoveablePoints.clear();

			List<Point> temp = getPossibleMovesForBomb2(x, y, zoneAccessible2);

			List<Point> temp2 = new ArrayList<Point>();
			temp2.addAll(temp);

			temp.clear();

			Point Ptemp = null;
			for (int aa = 0; aa < temp2.size(); aa++) {
				if ((oldSituation[temp2.get(aa).x][temp2.get(aa).y] != AI_BLOCK_WALL_HARD)
						&& (oldSituation[temp2.get(aa).x][temp2.get(aa).y] != AI_BLOCK_WALL_SOFT)) {
					Ptemp = new Point(temp2.get(aa));
					temp.add(Ptemp);

				}
			}

			MoveablePoints.addAll(temp);
			List<Integer> MoveablePointDistances = getPointDistances(
					MoveablePoints, Goal);
			int pts[] = minimumCostCalculation(MoveablePoints,MoveablePointDistances);
			if(pts!=null && pts.length>0)
			{	x = moveCosts.get(pts[1]).x;
				y = moveCosts.get(pts[1]).y;
				changed = true;
				//System.out.println("x: "+x+"y: "+y);
				RemoveFromList(pts[1]);
			}
			MoveablePoints.clear();
			MoveablePointDistances.clear();
			// i++;
		}
		result = movedPoints;

		return result;
	}

	/**
	 * 
	 * @param p
	 * 		Description manquante !
	 */
	// retourne la zone accessible a partir d'un point donné.
	// @param p point actuel de notre bonhomme
	public void getZoneAccessible(Point p) {
		if (!zoneAccessible.contains(p))
			zoneAccessible.add(p);

		List<Point> result = getPossibleMoves(p.x, p.y, true);

		for (int i = 0; i < result.size(); i++) {
			getZoneAccessible(result.get(i));
		}

	}

	/**
	 * 
	 * @param zoneAccessible
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public Point getObjective(List<Point> zoneAccessible) {
		Point result = new Point();
		List<Point> objectives = new ArrayList<Point>();
		List<Integer> objectivesCost = new ArrayList<Integer>();
		int[][] matrix = BetterMatrix();
		Point myPos = new Point(getOwnPosition()[0], getOwnPosition()[1]);
		Point ennemiPos = new Point(getClosestPlayerPositionAlive(myPos));

		for (int i = 0; i < zoneAccessible.size(); i++) {
			if ((matrix[zoneAccessible.get(i).x][zoneAccessible.get(i).y] == AI_BLOCK_ITEM_BOMB)
					|| (matrix[zoneAccessible.get(i).x][zoneAccessible.get(i).y] == AI_BLOCK_ITEM_FIRE)
					|| (zoneAccessible.get(i).equals(ennemiPos))) {
				objectives.add(new Point(zoneAccessible.get(i).x,
						zoneAccessible.get(i).y));
				int cost = BetterMatrix()[zoneAccessible.get(i).x][zoneAccessible
						.get(i).y];

				switch (cost) {
				case 5:
					objectivesCost.add(cost_bonus);
					break;
				case 6:
					objectivesCost.add(cost_bonus);
					break;
				default:
					objectivesCost.add(cost_ennemi);
					break;
				}
			}
		}
		if (objectives.size() != 0) {

			int dist = getDistance(myPos, objectives.get(0))
					+ objectivesCost.get(0);
			int distindice = 0;
			for (int i = 0; i < objectives.size(); i++) {

				if (dist > getDistance(myPos, objectives.get(i))
						+ objectivesCost.get(i)) {
					dist = getDistance(myPos, objectives.get(i))
							+ objectivesCost.get(i);
					distindice = i;
				}
			}
			result = objectives.get(distindice);

			objectives.clear();
			objectivesCost.clear();
		} else {
			int dist = getDistance(zoneAccessible.get(0), ennemiPos);
			int distindice = 0;
			for (int i = 0; i < zoneAccessible.size(); i++) {
				if (dist > getDistance(zoneAccessible.get(i), ennemiPos)) {
					dist = getDistance(zoneAccessible.get(i), ennemiPos);
					distindice = i;
				}
			}
			result = zoneAccessible.get(distindice);

			objectives.clear();
			objectivesCost.clear();
		}

		if (myPos.equals(result)) {
			return null;
		} else {

			return result;
		}
	}

	/**
	 * 
	 * @param p1
	 * 		Description manquante !
	 * @param p2
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public boolean isAdjacent(Point p1, Point p2) {
		if ((((p1.x == p2.x) && (Math.abs(p1.y - p2.y) == 1)))
				|| (((p1.y == p2.y) && (Math.abs(p1.x - p2.x) == 1)))) {

			return true;
		} else {

			return false;
		}
	}

	/**
	 * 
	 * @param MovedPoints
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public List<Integer> refine(List<Point> MovedPoints) {

		List<Integer> result = new ArrayList<Integer>();
		List<Point> preresult = new ArrayList<Point>();
		List<Point> temp = new ArrayList<Point>();

		preresult.add(MovedPoints.get(MovedPoints.size() - 1));
		int j = 1;
		for (int i = MovedPoints.size() - 1; i-j >= 0; i--) {
			try {
				if (isAdjacent(MovedPoints.get(i), MovedPoints.get(i - j))) {
					preresult.add(MovedPoints.get(i - j));
					i = i - j + 1;
					j = 1;

				} else {
					i++;
					j++;
				}
			} catch (Exception ee) {
				// System.out.println(ee.toString());
			}
		}
		for (int i = preresult.size() - 1; i >= 0; i--) {
			temp.add(preresult.get(i));
		}

		for (int i = 0; i < temp.size() - 1; i++) {
			if (temp.get(i).x - temp.get(i + 1).x == 1)
				result.add(AI_ACTION_GO_LEFT);
			else if (temp.get(i).x - temp.get(i + 1).x == -1)
				result.add(AI_ACTION_GO_RIGHT);
			else if (temp.get(i).y - temp.get(i + 1).y == 1)
				result.add(AI_ACTION_GO_UP);
			else if (temp.get(i).y - temp.get(i + 1).y == -1)
				result.add(AI_ACTION_GO_DOWN);
		}

		for (int i = 0; i < result.size(); i++)

			path = true;

		return result;
	}

	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param objORnot
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public List<Point> getPossibleMoves(int x, int y, boolean objORnot) {

		int[] direction = new int[4];

		List<Point> points = new ArrayList<Point>();

		try {
			direction[0] = BetterMatrix()[x][y - 1]; // UP
		} catch (RuntimeException e) {
			direction[0] = -1;

		}
		try {
			direction[1] = BetterMatrix()[x][y + 1]; // DOWN
		} catch (RuntimeException e) {
			direction[1] = -1;

		}
		try {
			direction[2] = BetterMatrix()[x - 1][y]; // LEFT
		} catch (RuntimeException e) {
			direction[2] = -1;

		}
		try {
			direction[3] = BetterMatrix()[x + 1][y]; // RIGHT
		} catch (RuntimeException e) {
			direction[3] = -1;

		}

		if (objORnot == true) {

			if (!zoneAccessible.contains(new Point(x, y - 1))) {
				if ((direction[0] == 0) || (direction[0] == 5)
						|| (direction[0] == 6))
					points.add(new Point(x, y - 1));
			}
			if (!zoneAccessible.contains(new Point(x, y + 1))) {
				if ((direction[1] == 0) || (direction[1] == 5)
						|| (direction[1] == 6))
					points.add(new Point(x, y + 1));
			}
			if (!zoneAccessible.contains(new Point(x - 1, y))) {
				if ((direction[2] == 0) || (direction[2] == 5)
						|| (direction[2] == 6))
					points.add(new Point(x - 1, y));
			}
			if (!zoneAccessible.contains(new Point(x + 1, y))) {
				if ((direction[3] == 0) || (direction[3] == 5)
						|| (direction[3] == 6))
					points.add(new Point(x + 1, y));
			}

		} else {
			if ((zoneAccessible.contains(new Point(x, y - 1)))) {
				if (!movedPoints.contains(new Point(x, y - 1))) {
					if ((direction[0] == 0) || (direction[0] == 5)
							|| (direction[0] == 6))
						points.add(new Point(x, y - 1));
				}
			}
			if (zoneAccessible.contains(new Point(x, y + 1))) {
				if (!movedPoints.contains(new Point(x, y + 1))) {
					if ((direction[1] == 0) || (direction[1] == 5)
							|| (direction[1] == 6))
						points.add(new Point(x, y + 1));
				}
			}
			if (zoneAccessible.contains(new Point(x - 1, y))) {
				if (!movedPoints.contains(new Point(x - 1, y))) {
					if ((direction[2] == 0) || (direction[2] == 5)
							|| (direction[2] == 6))
						points.add(new Point(x - 1, y));
				}
			}
			if (zoneAccessible.contains(new Point(x + 1, y))) {
				if (!movedPoints.contains(new Point(x + 1, y))) {
					if ((direction[3] == 0) || (direction[3] == 5)
							|| (direction[3] == 6))
						points.add(new Point(x + 1, y));
				}
			}

		}

		return points;

	}

	/**
	 * 
	 * @param point1
	 * 		Description manquante !
	 * @param point2
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	private int getDistance(Point point1, Point point2) {
		int result = 0;
		result = result + Math.abs(point1.x - point2.x);
		result = result + Math.abs(point1.y - point2.y);
		return result;
	}

	/**
	 * 
	 * @param points
	 * 		Description manquante !
	 * @param Goal
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	public List<Integer> getPointDistances(List<Point> points, Point Goal) {
		List<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < points.size(); i++) {
			result.add(getDistance(points.get(i), Goal));
		}
		return result;

	}

	/**
	 * 
	 * @param CurrentPoint
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	private Point getClosestPlayerPositionAlive(Point CurrentPoint) {
		int minDistance = Integer.MAX_VALUE;
		Point result = CurrentPoint;
		for (int i = 0; i < getPlayerCount(); i++) {
			int pos[] = getPlayerPosition(i);
			Point PlayerPos = new Point(pos[0], pos[1]);
			int temp = getDistance(CurrentPoint, PlayerPos);
			if ((temp < minDistance) && (isPlayerAlive(i))) {
				result = PlayerPos;
				minDistance = temp;
			}

		}

		return result;
	}

	/**
	 * 
	 * @param MinimumIndice
	 * 		Description manquante !
	 */
	public void RemoveFromList(int MinimumIndice) {

		movedPoints.add(moveCosts.get(MinimumIndice));

		costs.remove(MinimumIndice);
		moveCosts.remove(MinimumIndice);
	}

	/**
	 * 
	 * @param MoveablePoints
	 * 		Description manquante !
	 * @param MoveablePointDistances
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	// calcule le cout minimal en considerant les points a ou le bonhomme peut
	// aller.
	public int[] minimumCostCalculation(List<Point> MoveablePoints,
			List<Integer> MoveablePointDistances) {
		int[] MinimumCost = new int[2];

		int TempCost = coutInitial;

		for (int i = 0; i < MoveablePoints.size(); i++) {

			costs.add(TempCost + cost_empty + MoveablePointDistances.get(i));
			moveCosts.add(MoveablePoints.get(i));

		}

		int TempMini;
		if(costs!=null && !costs.isEmpty())
			TempMini = costs.get(0);
		else
			return null;

		int MiniIndice = 0;
		for (int i = 0; i < costs.size(); i++) {
			if (costs.get(i) < TempMini)

			{
				TempMini = costs.get(i);
				MiniIndice = i;
			}
		}

		MinimumCost[0] = TempMini;
		MinimumCost[1] = MiniIndice;

		coutInitial = TempCost + cost_empty;

		return MinimumCost;

	}

	/**
	 * 
	 * @param MoveablePoints
	 * 		Description manquante !
	 * @param MoveablePointDistances
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	// calcule le cout minimal en considerant les points a ou le bonhomme peut
	// aller.
	public int[] calculateMinimumCost(List<Point> MoveablePoints,
			List<Integer> MoveablePointDistances) {
		int[] MinimumCost = new int[2];
		int TempCost = coutInitial;

		for (int i = 0; i < MoveablePoints.size(); i++) {
			int[][] tempo = BetterMatrix();
			int cost = tempo[MoveablePoints.get(i).x][MoveablePoints.get(i).y];

			switch (cost) {
			case 0:
				costs.add(TempCost + cost_empty + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 1:
				costs.add(TempCost + cost_softwall + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 2:
				costs.add(TempCost + cost_hardwall + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 3:
				costs.add(TempCost + cost_feu + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 4:
				costs.add(TempCost + cost_bomba + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 5:
				costs.add(TempCost + cost_bonus + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 6:
				costs.add(TempCost + cost_bonus + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;
			case 7:
				costs.add(TempCost + cost_portee + cost_distance_multiplier
						* MoveablePointDistances.get(i));
				moveCosts.add(MoveablePoints.get(i));
				break;

			}
		}

		int TempMini = costs.get(0);
		int MiniIndice = 0;
		for (int i = 0; i < costs.size(); i++) {
			if (costs.get(i) < TempMini) {
				TempMini = costs.get(i);
				MiniIndice = i;
			}
		}

		MinimumCost[0] = TempMini;
		MinimumCost[1] = MiniIndice;

		coutInitial = coutInitial + MinimumCost[0];

		return MinimumCost;

	}

	/**
	 * 
	 * @return ?
	 */
	// prend les donnees de getZoneMatris et renvoi une matrice ameliorée en
	// l'ajoutant les couts specifiques.
	public int[][] BetterMatrix() {
		int[][] ZoneMatrix = getZoneMatrix();

		List<Point> BombPos = new ArrayList<Point>();
		blockPoints.clear();
		BombPos = BringAllBlockPositions(AI_BLOCK_BOMB);

		for (int p = 0; p < BombPos.size(); p++) {
			int BombePortee = getBombPowerAt(BombPos.get(p).x, BombPos.get(p).y);

			for (int i = 1; i <= BombePortee; i++) {
				if (BombPos.get(p).x + i < getZoneMatrixDimX() - 1) {
					ZoneMatrix[BombPos.get(p).x + i][BombPos.get(p).y] = 7;
				}
				if (BombPos.get(p).x - i > 0) {
					ZoneMatrix[BombPos.get(p).x - i][BombPos.get(p).y] = 7;
				}
			}

			for (int i = 1; i <= BombePortee; i++) {
				if (BombPos.get(p).y + i < getZoneMatrixDimY() - 1) {
					ZoneMatrix[BombPos.get(p).x][BombPos.get(p).y + i] = 7;
				}
				if (BombPos.get(p).y - i > 0) {
					ZoneMatrix[BombPos.get(p).x][BombPos.get(p).y - i] = 7;
				}
			}
		}

		return ZoneMatrix;
	}

	/**
	 * 
	 * @param CurrentPos
	 * 		Description manquante !
	 * @param blockType
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	private Point BringClosestBlockPosition(Point CurrentPos, int blockType) {
		int minDistance = Integer.MAX_VALUE;
		Point result = new Point(CurrentPos.x, CurrentPos.y);
		int[][] matrix = getZoneMatrix();
		for (int i = 0; i < getZoneMatrixDimX(); i++)
			for (int j = 0; j < getZoneMatrixDimY(); j++)
				if (matrix[i][j] == blockType) {
					int tempDistance = distance(CurrentPos.x, CurrentPos.y, i,
							j);
					if (tempDistance < minDistance) {
						minDistance = tempDistance;
						result.x = i;
						result.y = j;

					}
				}
		return result;
	}

	/**
	 * 
	 * @param blockType
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	private List<Point> BringAllBlockPositions(int blockType) {

		int[][] matrix = getZoneMatrix();
		for (int i = 0; i < getZoneMatrixDimX(); i++) {
			for (int j = 0; j < getZoneMatrixDimY(); j++) {
				if (matrix[i][j] == blockType) {
					Point Presult = new Point(i, j);
					blockPoints.add(Presult);
				}
			}
		}

		return blockPoints;
	}

	/**
	 * calcule la distance entre deuz points donnes.
	 * 
	 * @param x1
	 * 		Description manquante !
	 * @param y1
	 * 		Description manquante !
	 * @param x2
	 * 		Description manquante !
	 * @param y2
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 */
	private int distance(int x1, int y1, int x2, int y2) {
		int result = 0;
		result = result + Math.abs(x1 - x2);
		result = result + Math.abs(y1 - y2);
		return result;
	}

	/**
	 * 
	 */
	// imprime la meilleure matrice
	public void printBetterZoneMatrix() {
		int[][] zoneMatrix = BetterMatrix();

		for (int i1 = 0; i1 < zoneMatrix[0].length; i1++) { // for(int
															// i2=0;i2<zoneMatrix.length;i2++)
															// System.out.print(zoneMatrix[i2][i1]+" ");
			// System.out.println();
		}
	}

}
