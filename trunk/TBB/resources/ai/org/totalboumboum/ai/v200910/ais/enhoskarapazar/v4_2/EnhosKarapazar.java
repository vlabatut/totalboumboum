package org.totalboumboum.ai.v200910.ais.enhoskarapazar.v4_2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;


public class EnhosKarapazar extends ArtificialIntelligence {
	private boolean dropBombAction = false;
	AiAction result = new AiAction(AiActionName.NONE);
	private DangerZone dZone = null;
	private PathManagement pathManager = null;
	private boolean debug = false;
	private AiTile nextTile = null;
	private Direction leftBomb = Direction.NONE;
	private AiTile endTile = null;
	private AiTile endTileAttack = null;
	/** le personnage dirig� par cette IA */
	private AiHero ownHero = null;
	/** la zone de jeu */
	private AiZone zone = null;
	/** la case occupée actuellement par le personnage */
	private AiTile currentTile = null;
	/**
	 * renvoie la case courante
	 */
	/** la position en pixels occupée actuellement par le personnage */
	private int currentX;
	/** la position en pixels occupée actuellement par le personnage */
	private int currentY;

	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentTile;
	}

	/**
	 * renvoie l'abscisse courante (en pixels)
	 */
	public int getCurrentX() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentX;
	}

	/**
	 * renvoie l'ordonnée courante (en pixels)
	 */
	public int getCurrentY() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentY;
	}
	
	/**
	 * Update psitions
	 */
	private void updateLocation() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		zone = getPercepts();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		currentX = (int) ownHero.getPosX();
		currentY = (int) ownHero.getPosY();

	}

	/**
	 * renvoie le personnage contr�l� par cette IA
	 */
	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return ownHero;
	}

	/**
	 * renvoie la zone de jeu
	 */
	public AiZone getZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		return zone;
	}
	
	/**
	 * renvoi l'Action a faire en cas de danger 
	 */
	private returnAction dangerAction() throws StopRequestException {
		
		if (debug)
			System.out.println("dangerAction()");
		updateLocation();
		
		AiTile closestClear=findClosestClearTile();
		
		PathManagement pM = null;
		returnAction ret = new returnAction();
		if (debug)
			System.out.println("Tehlike Var");
		dropBombAction = false;
		if (leftBomb == Direction.NONE) {
			if (closestClear != null) {
				pM = new PathManagement(this, closestClear);
				if (debug)
					System.out.println("Tehlikesiz Bolge : "
							+ closestClear.toString());
				if (debug)
					System.out.println("Yol : " + pM.getPathList().toString()
							+ "/n Uzunluk : " + pM.getLength());
				if (debug)
					pM.printPath();
				goTroughPath(pM.getPathList());
				if (nextTile != null) {
					ret.move = zone.getDirection(currentTile, nextTile);
					ret.actionName = AiActionName.MOVE;
				}
			}
		} else {
			if (debug)
			System.out.println("------------------------------------------------danger action--------------------------------------------------------"+ leftBomb);
			ret.move = leftBomb;
			leftBomb = Direction.NONE;
			ret.actionName = AiActionName.MOVE;
		}
		endTile = null;
		endTileAttack = null;
		return ret;
	}

	/**
	 * renvoie drop Bomb Action pour tuer adversaire
	 */
	private returnAction attackAction() throws StopRequestException {
		if (debug)
			System.out.println("attackAction()");
		updateLocation();
		returnAction ret = new returnAction();
		AiTile temp = findRivalToAttack(false);
		if(temp!=null)
		if (inAttackRange(temp)) {
			Direction lefttoDrirection = isCleanDirection(ownHero.getBombRange() + 1, ownHero.getTile());
			if (!inDanger(ownHero.getTile().getLine(), ownHero.getTile().getCol()) && isRangeClean(ownHero.getBombRange() + 1, currentTile)	) {
				if (debug)
					System.out.println("attack bomb");
				ret.actionName = AiActionName.DROP_BOMB;
				leftBomb = lefttoDrirection;
			}
		}
		endTileAttack = null;
		nextTile = null;
		return ret;
	}

	/**
	 * renvoie drop Bomb Action pour destruct des murs
	 */
	private returnAction dropBombDestructWallAction() throws StopRequestException {
		if (debug)
			System.out.println("dropBombAction()");
		updateLocation();
		returnAction ret = new returnAction();
		if (findTileForDestructible() != null && isRangeClean(ownHero.getBombRange() + 1, currentTile)) {
			if (debug)
				System.out.println("Defanse bomb");
			ret.actionName = AiActionName.DROP_BOMB;
			leftBomb = isCleanDirection(ownHero.getBombRange() + 1, ownHero
					.getTile());
		}
		return ret;
	}

	/**
	 * renvoie l'Action a faire en cas de Bonus Bombe
	 */
	private returnAction getBonusBombeAction() throws StopRequestException {
		if (debug)
			System.out.println("getBonusBombeAction");
		updateLocation();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, findClosestBONUSBOMBE());
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}

	
	/**
	 * renvoie l'action a faire en cas de Bonus Range
	 */
	private returnAction getBonusRangeAction() throws StopRequestException {
		if (debug)
			System.out.println("getBonusRangeAction");
		updateLocation();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, findClosestBONUSRANGE());
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}

	/**
	 * renvoie l'action a faire en cas de destruction des murs
	 */
	private returnAction wallDestructAction() throws StopRequestException {
		if (debug)
			System.out.println("wallDestructAction()");
		updateLocation();
		AiTile toDestruct = findTileForDestructible();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, toDestruct);
		endTile = null;
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());
			endTile = toDestruct;
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}
	
	/**
	 * renvoie l'action a faire en cas d'attaque
	 */
	private returnAction moveattackAction(boolean rangeControl)
			throws StopRequestException {
		if (debug)
			System.out.println("moveattackAction()");
		updateLocation();
		AiTile toAttack=findRivalToAttack(rangeControl);
		returnAction ret = new returnAction();
		endTileAttack = null;
		PathManagement pM = new PathManagement(this,toAttack);
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());
			endTileAttack = toAttack;
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}

	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		dZone = null;
		init();
		updateLocation();
		returnAction ret = new returnAction();
		dZone = new DangerZone(zone, this);
		if (dropBombAction) {
			ret = dangerAction();
		} else {
			if (endTileAttack != null) {
				if (inDanger(ownHero.getLine(), ownHero.getCol())) {
					ret = dangerAction();
				} else
					ret = attackAction();

			} else if (endTile != null && currentTile == endTile) {
				if (inDanger(ownHero.getLine(), ownHero.getCol())) {
					ret = dangerAction();
				} else
					ret = dropBombDestructWallAction();

			} else {
				if (findClosestBONUSRANGE() != null	&& ownHero.getBombRange() < 8) {
					if (inDanger(ownHero.getLine(), ownHero.getCol())) {
						ret = dangerAction();
					} else
						ret = getBonusRangeAction();
				} else if (findClosestBONUSBOMBE() != null	&& ownHero.getBombNumber() < 5) {
					if (inDanger(ownHero.getLine(), ownHero.getCol())) {
						ret = dangerAction();
					} else
						ret = getBonusBombeAction();
				} else {
					if ((findTileForDestructible() != null)	&& (findRivalToAttack(true) == null	|| ownHero.getBombNumber() < 4 || ownHero.getBombRange() < 7)) {
						if (inDanger(ownHero.getLine(), ownHero.getCol())) {
							ret = dangerAction();
						} else
							ret = wallDestructAction();
					} else if (findRivalToAttack(true) != null) {
						if (inDanger(ownHero.getLine(), ownHero.getCol())) {
							ret = dangerAction();
						} else
							ret = moveattackAction(true);
					}
				}
				if (inDanger(ownHero.getLine(), ownHero.getCol())) {
					ret = dangerAction();
				} else {
					if (ret.actionName == AiActionName.NONE
							&& ret.move == Direction.NONE) {
						if (findTileForDestructible() != null) {
							ret = wallDestructAction();
						} else if (findRivalToAttack(false) != null) {
							if (inDanger(ownHero.getLine(), ownHero.getCol())) {
								ret = dangerAction();
							} else
								ret = moveattackAction(false);
						}
					}
					ret = isDirectionSafe(ret);
				}
			}
		}
		nextTile = null;
		if (ret.actionName == AiActionName.DROP_BOMB) {
			endTile = null;
			endTileAttack = null;
			dropBombAction = true;
			result = new AiAction(ret.actionName);
		} else {
			result = new AiAction(ret.actionName, ret.move);
			leftBomb = Direction.NONE;
		}
		return result;

	}

	/**
	 * control si la rival est assez proche et il est possible que le bombe peut effectuer la rival 
	 */
	private boolean inAttackRange(AiTile rival) throws StopRequestException {
		checkInterruption();
		updateLocation();
		boolean result = false;
		dZone = new DangerZone(zone, this);
		int ownHeroLine = ownHero.getLine();
		int ownHeroCol = ownHero.getCol();
		int rivalLine = rival.getLine();
		int rivalCol = rival.getCol();

		boolean UP = false, DOWN = false, LEFT = false, RIGHT = false;
		
		if(ownHero.getTile()==rival)
		{	return true;
		}
		else{
		if (ownHeroLine == rivalLine && Math.abs(rivalCol - ownHeroCol) < ownHero.getBombRange() - 1) {
			if (rivalCol - ownHeroCol < 0) {
				for (int i = rivalCol + 1; i < ownHeroCol; i++) {
					try {
						if (dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSBOMBE
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSRANGE
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.DESTRUCTIBLES
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.INDESTRUCTIBLES) {
							UP = true;
							break;
						}
					} catch (Exception e) {
						if (debug) {
							System.out
									.println("attak range patlad� d������................................................................UP"
											+ i);
							System.out.println(e.getStackTrace());
						}
					}
				}
			} else {
				for (int i = rivalCol-1 ; i < ownHeroCol; i--) {
					try {
						if (dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSBOMBE
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSRANGE
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.DESTRUCTIBLES
								|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.INDESTRUCTIBLES) {
							DOWN = true;
							break;
						}
					} catch (Exception e) {
						if (debug) {
							System.out
									.println("attak range patlad� d������................................................................DOWN"
											+ i);
							System.out.println(e.getStackTrace());
						}
					}
				}
			}
		}

		if (ownHeroCol == rivalCol
				&& Math.abs(rivalLine - ownHeroLine) < ownHero.getBombRange() - 1) {
			if (rivalLine - ownHeroLine < 0) {
				for (int i = rivalLine + 1; i < ownHeroLine; i++) {
					try {
						if (dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSBOMBE
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSRANGE
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.DESTRUCTIBLES
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.INDESTRUCTIBLES) {
							LEFT = true;
							break;
						}
					} catch (Exception e) {
						if (debug) {
							System.out
									.println("attak range patlad� d������................................................................LEFT"
											+ i);
							System.out.println(e.getStackTrace());
						}
					}
				}
			} else {
				for (int i = rivalLine ; i < ownHeroLine; i--) {
					try {
						if (dZone.getEnum(ownHeroCol, i) == ZoneEnum.BONUSBOMBE
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSRANGE
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.DESTRUCTIBLES
								|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.INDESTRUCTIBLES) {
							RIGHT = true;
							break;
						}
					} catch (Exception e) {
						if (debug) {
							System.out
									.println("attak range patlad� d������................................................................RIGHT"
											+ i);
							System.out.println(e.getStackTrace());
						}
					}
				}
			}
		}

		if (!UP && !DOWN && !LEFT && !RIGHT)
			result = true;

		return result;
		}
	}

	/**
	 * control la Danger de la Direction ce qu'on va prendre
	 */
	private returnAction isDirectionSafe(returnAction move)
			throws StopRequestException {
		int heroLine = ownHero.getLine();
		int heroCol = ownHero.getCol();
		boolean dangerDown = inDanger(heroLine + 1, heroCol);
		boolean dangerUP = inDanger(heroLine - 1, heroCol);
		boolean dangerRight = inDanger(heroLine, heroCol - 1);
		boolean dangerLeft = inDanger(heroLine, heroCol + 1);

		if (nextTile != null) {
			if ((dangerDown || isWall(heroLine + 1, heroCol))
					&& (dangerUP || isWall(heroLine - 1, heroCol))
					&& (dangerLeft || isWall(heroLine, heroCol + 1))
					&& (dangerRight || isWall(heroLine, heroCol - 1))) {
				dangerDown = inDangerLevel2(ownHero.getLine() + 1, ownHero
						.getCol());
				dangerUP = inDangerLevel2(ownHero.getLine() - 1, ownHero
						.getCol());
				dangerRight = inDangerLevel2(ownHero.getLine(), ownHero
						.getCol() - 1);
				dangerLeft = inDangerLevel2(ownHero.getLine(),
						ownHero.getCol() + 1);
			}

			dZone = new DangerZone(zone, this);
			if (dZone.getEnum(heroLine, heroCol) != ZoneEnum.BOMBE) {
				updateLocation();
				Direction dir = zone.getDirection(currentTile, nextTile);

				if (dangerDown || dangerUP) {
					if (dangerDown) {
						if (dir == Direction.DOWN || dir == Direction.DOWNLEFT
								|| dir == Direction.DOWNRIGHT) {
							move.move = Direction.NONE;
							endTile = null;
						} else {
							move.move = zone
									.getDirection(currentTile, nextTile);
						}
					}
					if (dangerUP) {
						if (dir == Direction.UP || dir == Direction.UPLEFT
								|| dir == Direction.UPRIGHT) {
							move.move = Direction.NONE;
							endTile = null;
						} else {
							move.move = zone
									.getDirection(currentTile, nextTile);
						}
					}
				} else {
					if (dangerLeft || dangerRight) {
						if (dangerLeft) {
							if (dir == Direction.LEFT) {
								move.move = Direction.NONE;
								endTile = null;
							} else {
								move.move = zone.getDirection(currentTile,
										nextTile);
							}
						}
						if (dangerRight) {
							if (dir == Direction.RIGHT) {
								endTile = null;
								move.move = Direction.NONE;
							} else {
								move.move = zone.getDirection(currentTile,
										nextTile);
							}
						}
					}
				}
			}
		}
		return move;
	}

	/**
	 * Initialise
	 */
	private void init() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (ownHero == null) {
			zone = getPercepts();
			ownHero = zone.getOwnHero();

			updateLocation();
			pathManager = new PathManagement(this, currentTile);
		}

	}

	/**
	 * Control la Tile , si elle est mur
	 */
	public boolean isWall(int line, int col) throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		ZoneEnum myZone = dZone.getEnum(line, col);
		boolean result = (myZone == ZoneEnum.DESTRUCTIBLES || myZone == ZoneEnum.INDESTRUCTIBLES);
		return result;
	}

	/**
	 * Control la Danger 
	 */
	public boolean inDanger(int line, int col) throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		ZoneEnum myzone = dZone.getEnum(line, col);
		boolean ret = (myzone == ZoneEnum.FLAMMES || myzone == ZoneEnum.FEU || myzone == ZoneEnum.BOMBE);
		return ret;
	}

	/**
	 * Control la Danger en utilisant temps d'explosion
	 */
	public boolean inDangerLevel2(int line, int col)
			throws StopRequestException {

		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		ZoneEnum myzone = dZone.getEnum(line, col);
		boolean ret = (myzone == ZoneEnum.FEU || myzone == ZoneEnum.BOMBE);

		List<AiBomb> bombs = (ArrayList<AiBomb>) zone.getBombs();
		for (AiBomb aiBomb : bombs) {
			checkInterruption();
			boolean isrange = false;
			List<AiTile> range = (ArrayList<AiTile>) aiBomb.getBlast();
			for (AiTile aiTile : range) {
				checkInterruption();
				if (aiTile.getCol() == col && aiTile.getLine() == line)
					isrange = true;
			}
			if (isrange) {
				if (aiBomb.isWorking()
						&& (aiBomb.getLatencyDuration() - aiBomb.getTime()) < (ownHero
								.getWalkingSpeed() * 318))
					ret = true;

			}
		}

		return ret;
	}

	/**
	 * Tout cas sauf les bombs, flammes, feu et murs sont Safe
	 */
	private boolean isSafe(int line, int col) throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		ZoneEnum zone = dZone.getEnum(line, col);
		return (zone != ZoneEnum.BOMBE && zone != ZoneEnum.FLAMMES
				&& zone != ZoneEnum.FEU && zone != ZoneEnum.DESTRUCTIBLES && zone != ZoneEnum.INDESTRUCTIBLES);

	}
	
	/**
	 * renvoie la Tile de la plus proche Bonus Range
	 */
	private AiTile findClosestBONUSRANGE() throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		AiTile temp = null;
		int tempLength = 100;
		pathManager = null;
		for (int i = 0; i < zone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				if (dZone.getEnum(i, j) == ZoneEnum.BONUSRANGE) {
					if (isSafe(i, j)) {
						pathManager = null;
						pathManager = new PathManagement(this, zone.getTile(i,
								j));
						if (pathManager.isWalkable()
								&& (getCurrentTile() != zone.getTile(i, j))) {

							if (pathManager.getLength() < tempLength) {
								updateLocation();
								temp = zone.getTile(i, j);
								tempLength = pathManager.getLength();

							}
						}
					}
				}
			}

		}
		pathManager = null;
		return temp;
	}

	/**
	 * renvoie la Tile de la plus proche Bonus Bombe
	 */
	private AiTile findClosestBONUSBOMBE() throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		AiTile temp = null;
		int tempLength = 100;
		pathManager = null;
		for (int i = 0; i < zone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				if (dZone.getEnum(i, j) == ZoneEnum.BONUSBOMBE) {
					if (isSafe(i, j)) {
						pathManager = null;
						pathManager = new PathManagement(this, zone.getTile(i,
								j));
						if (pathManager.isWalkable()
								&& (getCurrentTile() != zone.getTile(i, j))) {

							if (pathManager.getLength() < tempLength) {

								temp = zone.getTile(i, j);
								tempLength = pathManager.getLength();

							}
						}
					}
				}
			}

		}
		pathManager = null;
		return temp;
	}

	/**
	 * renvoie la Tile plus proche et Safe
	 */
	private AiTile findClosestClearTile() throws StopRequestException {
		checkInterruption();
		updateLocation();
		AiTile temp = null;
		int tempLength = 100;
		pathManager = null;
		for (int i = 0; i < zone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				if (isSafe(i, j)) {
					pathManager = null;
					pathManager = new PathManagement(this, zone.getTile(i, j));
					if (pathManager.isWalkable()
							&& (getCurrentTile() != zone.getTile(i, j))) {

						if (pathManager.getLength() < tempLength) {
							if (isSafe(i, j)) {
								temp = zone.getTile(i, j);
								tempLength = pathManager.getLength();

							}
						}
					}
				}
			}

		}
		pathManager = null;
		return temp;
	}
	
	/**
	 * renvoie une Tile ou on peut detruir plus de murs destructible
	 */
	public AiTile findTileForDestructible() throws StopRequestException {
		checkInterruption();
		updateLocation();
		AiTile temp = null;
		int destructibleVoisin = 0;

		int imin = 0;
		int imax = zone.getHeight() - 1;

		int jmin = 0;
		int jmax = zone.getWidth() - 1;

		if (ownHero.getLine() - 4 > 0)
			imin = ownHero.getLine() - 4;
		if (ownHero.getLine() + 4 < zone.getHeight())
			imax = ownHero.getLine() + 4;

		if (ownHero.getCol() - 4 > 0)
			jmin = ownHero.getCol() - 4;
		if (ownHero.getCol() + 4 < zone.getWidth())
			jmax = ownHero.getCol() + 4;

		for (int i = imin; i < imax; i++) {
			checkInterruption();
			for (int j = jmin; j < jmax; j++) {
				checkInterruption();
				if (isSafe(i, j)) {
					AiTile tile = zone.getTile(i, j);
					pathManager = null;
					updateLocation();
					pathManager = new PathManagement(this, tile);
					if (pathManager.isWalkable() && (getCurrentTile() != tile)) {

						if (pathManager.getLength() < 25) {
							if (destructibleVoisin < countDestructibleVoisins(
									tile.getLine(), tile.getCol())) {
								int range = ownHero.getBombRange(); // 
								if (debug)
									System.out
											.println("range-----------------------------------------"
													+ range);
								if (isRangeClean(range + 1, tile)) {
									temp = tile;
									destructibleVoisin = countDestructibleVoisins(
											temp.getLine(), temp.getCol());
								}
							}
						}
					}

				}

			}
		}
		pathManager = null;
		return temp;
	}

	/**
	 * renvoie la Tile de la plus proche adversaire
	 */
	private AiTile findRivalToAttack(boolean rangeLimit)
			throws StopRequestException {
		checkInterruption();
		updateLocation();
		AiTile temp = null;
		int tempLength = 100;
		for (AiHero rival : zone.getHeroes()) {
			AiTile tile = rival.getTile();
			
			if (rival != ownHero) {
				pathManager = new PathManagement(this, tile);
				if (pathManager.isWalkable()) {
					if (pathManager.getLength()>=0 &&pathManager.getLength() < tempLength) {
					//if(zone.getTileDistance(ownHero.getTile(), tile)<tempLength){
						
							temp = tile;
							//tempLength = zone.getTileDistance(ownHero.getTile(), tile);
							tempLength = pathManager.getLength();
						
					}
				}
			}
		}
		if (rangeLimit)
			if (tempLength > 7)
				temp = null;
		return temp;
	}

	/**
	 * renvoie le nombre de voisins qui vont detruire si on pose une bombe a cette Tile
	 */
	public int countDestructibleVoisins(int line, int col)
			throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);

		int count = 0;
		boolean Up = true;
		boolean Down = true;
		boolean Right = true;
		boolean Left = true;
		for (int i = 0; i < ownHero.getBombRange(); i++) {
			checkInterruption();
			if (Up) {
				if (dZone.getEnum(line + i, col) == ZoneEnum.DESTRUCTIBLES
						|| dZone.getEnum(line + i, col) == ZoneEnum.BONUSBOMBE
						|| dZone.getEnum(line + i, col) == ZoneEnum.BONUSRANGE
						|| dZone.getEnum(line + i, col) == ZoneEnum.INDESTRUCTIBLES)
					Up = false;
				if (dZone.getEnum(line + i, col) == ZoneEnum.DESTRUCTIBLES)
					count++;
			}
			if (Down) {
				if (dZone.getEnum(line - i, col) == ZoneEnum.DESTRUCTIBLES
						|| dZone.getEnum(line - i, col) == ZoneEnum.BONUSBOMBE
						|| dZone.getEnum(line - i, col) == ZoneEnum.BONUSRANGE
						|| dZone.getEnum(line - i, col) == ZoneEnum.INDESTRUCTIBLES)
					Down = false;
				if (dZone.getEnum(line - i, col) == ZoneEnum.DESTRUCTIBLES)
					count++;
			}
			if (Right) {
				if (dZone.getEnum(line, col + i) == ZoneEnum.DESTRUCTIBLES
						|| dZone.getEnum(line, col + i) == ZoneEnum.BONUSBOMBE
						|| dZone.getEnum(line, col + i) == ZoneEnum.BONUSRANGE
						|| dZone.getEnum(line, col + i) == ZoneEnum.INDESTRUCTIBLES)
					Right = false;
				if (dZone.getEnum(line, col + i) == ZoneEnum.DESTRUCTIBLES)
					count++;
			}
			if (Left) {
				if (dZone.getEnum(line, col - i) == ZoneEnum.DESTRUCTIBLES
						|| dZone.getEnum(line, col - i) == ZoneEnum.BONUSBOMBE
						|| dZone.getEnum(line, col - i) == ZoneEnum.BONUSRANGE
						|| dZone.getEnum(line, col - i) == ZoneEnum.INDESTRUCTIBLES)
					Left = false;
				if (dZone.getEnum(line, col - i) == ZoneEnum.DESTRUCTIBLES)
					count++;
			}

		}

		return count;
	}

	/**
	 * renvoi la nextTile ce qu'on va prendre
	 */
	public void goTroughPath(List<AiTile> a) throws StopRequestException {
		checkInterruption();
		updateLocation();
		Iterator<AiTile> it = a.iterator();

		if (it.hasNext()) {
			nextTile = it.next();
		} else
			nextTile = currentTile;
	}

	/**
	 * controle si la direction ce qu'on va prendre n'est pas en danger
	 */
	private Direction isCleanDirection(int range, AiTile bomb)
			throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		return dZone.rangeControl(range, bomb.getLine(), bomb.getCol());
	}
	
	/**
	 * controle s'il y a une tile a fuir
	 */
	public boolean isRangeClean(int range, AiTile bomb)
			throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		Boolean result = false;
		if (dZone.rangeControl(range, bomb.getLine(), bomb.getCol()) != Direction.NONE)
			result = true;
		dZone = null;
		return result;
	}

	/**
	 * controle la path s'il y a un danger
	 */
	public boolean pathInDanger(List<AiTile> array)
			throws StopRequestException {
		checkInterruption();
		updateLocation();
		boolean result = false;
		for (AiTile aiTile : array) {
			checkInterruption();
			if (inDanger(aiTile.getLine(), aiTile.getCol())) {
				result = true;
			}
		}
		return result;
	}
}