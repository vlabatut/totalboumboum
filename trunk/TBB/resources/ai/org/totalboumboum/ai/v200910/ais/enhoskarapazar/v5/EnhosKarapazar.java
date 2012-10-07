package org.totalboumboum.ai.v200910.ais.enhoskarapazar.v5;

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

/**
 * @author Sadettin Enhoş
 * @author Ali Can Karapazar
 */
@SuppressWarnings("deprecation")
public class EnhosKarapazar extends ArtificialIntelligence {

	/** */
	AiAction result = new AiAction(AiActionName.NONE);

	/** La zone cree par moi pour controler le danger */
	private DangerZone dZone = null;
	/** La classe ce qu j'utilise pour trouver un path. */
	private PathManagement pathManager = null;
	/** Si debugMode est false les sysout ne marchent pas. */
	private boolean debugMode = false;
	/** La prochain Tile ce qu'on vas prendre */
	private AiTile nextTile = null;
	/** La direction a prendre apres poser une bombe */
	private Direction leftBomb = Direction.NONE;
	/** Le Tile ou on vas poser une bombe pour detruire des murs */
	private AiTile endTileDestruct = null;
	/** */
	private AiTile endTileAttack = null;
	/** le personnage dirigé par cette IA */
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

	/**
	 * 
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public AiTile getCurrentTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentTile;
	}

	/**
	 * renvoie l'abscisse courante (en pixels)
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	public int getCurrentX() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentX;
	}

	/**
	 * renvoie l'ordonnée courante (en pixels)
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	public int getCurrentY() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return currentY;
	}

	/**
	 * Update positions
	 * @throws StopRequestException 
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
	 * renvoie le personnage contrôlé par cette IA
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return ownHero;
	}

	/**
	 * renvoie la zone de jeu
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	public AiZone getZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		return zone;
	}

	/**
	 *@return renvoi l'Action a faire en cas de danger
	 * @throws StopRequestException 
	 */
	private returnAction dangerAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("dangerAction()");
		updateLocation();

		AiTile closestClear = findClosestClearTile();

		PathManagement pM = null;
		returnAction ret = new returnAction();
		if (debugMode)
			System.out.println("Tehlike Var");
		if (leftBomb == Direction.NONE) {
			if (closestClear != null) {
				pM = new PathManagement(this, closestClear);
				if (debugMode)
					System.out.println("Tehlikesiz Bolge : "
							+ closestClear.toString());
				if (debugMode)
					System.out.println("Yol : " + pM.getPathList().toString()
							+ "/n Uzunluk : " + pM.getLength());
				if (debugMode)
					pM.printPath();
				goTroughPath(pM.getPathList());
				if (nextTile != null) {
					ret.move = zone.getDirection(currentTile, nextTile);
					ret.actionName = AiActionName.MOVE;
				}
			}
		} else {
			if (debugMode)
				System.out
						.println("------------------------------------------------danger action--------------------------------------------------------"
								+ leftBomb);
			ret.move = leftBomb;
			leftBomb = Direction.NONE;
			ret.actionName = AiActionName.MOVE;
		}
		endTileDestruct = null;
		endTileAttack = null;
		return ret;
	}

	/**
	 * @return renvoie drop Bomb Action pour tuer adversaire
	 * @throws StopRequestException 
	 */
	private returnAction dropBombAttackAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("attackAction()");
		updateLocation();
		returnAction ret = new returnAction();
		if (isSafe(currentTile.getLine(), currentTile.getCol()))
			if (inAttackRange(endTileAttack)) {
				if (isRangeClean(ownHero.getBombRange() + 1, currentTile)) {
					if (debugMode)
						System.out.println("attack bomb______Ben burdaym :"
								+ ownHero.getTile().toString()
								+ "Dman Burda : " + endTileAttack.toString());
					ret.actionName = AiActionName.DROP_BOMB;
					leftBomb = isCleanDirection(ownHero.getBombRange() + 1,
							ownHero.getTile());
					;
					if (debugMode)
						System.out.println("................................."
								+ leftBomb);
				}
			}
		endTileAttack = null;
		nextTile = null;
		return ret;
	}

	/**
	 * @return renvoie drop Bomb Action pour destruct des murs
	 * @throws StopRequestException 
	 */
	private returnAction dropBombDestructWallAction()
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("dropBombAction()");
		updateLocation();
		returnAction ret = new returnAction();
		if (isRangeClean(ownHero.getBombRange() + 1, currentTile)) {
			if (debugMode)
				System.out.println("Defanse bomb");
			ret.actionName = AiActionName.DROP_BOMB;
			leftBomb = isCleanDirection(ownHero.getBombRange() + 1, ownHero
					.getTile());
		}
		return ret;
	}

	/**
	 * @param target 
	 * @return renvoie l'Action a faire en cas de prendre Bonus Bombe
	 * @throws StopRequestException 
	 */
	private returnAction getBonusBombeAction(AiTile target)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("getBonusBombeAction");
		updateLocation();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, target);
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
	 * @param target 
	 * @return renvoie l'action a faire en cas de prendre Bonus Range
	 * @throws StopRequestException 
	 */
	private returnAction getBonusRangeAction(AiTile target)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("getBonusRangeAction");
		updateLocation();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, target);
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
	 * @param target 
	 * @return renvoie l'action a faire en cas de destruction des murs
	 * @throws StopRequestException 
	 */
	private returnAction wallDestructAction(AiTile target)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("wallDestructAction()");
		updateLocation();
		// AiTile toDestruct = findTileForDestructible();
		returnAction ret = new returnAction();
		PathManagement pM = new PathManagement(this, target);
		endTileDestruct = null;
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());
			endTileDestruct = target;
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}

	/**
	 * @param target 
	 * @return renvoie l'action a faire en cas d'attaque
	 * @throws StopRequestException 
	 */
	private returnAction moveattackAction(AiTile target)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (debugMode)
			System.out.println("moveattackAction()");
		updateLocation();
		// AiTile toAttack = findRivalToAttack();
		returnAction ret = new returnAction();

		PathManagement pM = new PathManagement(this, target);
		if (!pathInDanger(pM.getPathList())) {
			goTroughPath(pM.getPathList());

			endTileAttack = target;
			if (nextTile != null) {
				ret.move = zone.getDirection(currentTile, nextTile);
				ret.actionName = AiActionName.MOVE;
			}
		}
		return ret;
	}

	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		init();

		updateLocation();

		returnAction ret = new returnAction();
		AiTile target = null;
		dZone = new DangerZone(zone, this);

		// Si on est en train d'attaquer a qqun
		if (endTileAttack != null) {
			// S'il y a un danger
			if (inDanger(ownHero.getLine(), ownHero.getCol())) {
				// Fuir
				ret = dangerAction();
			} else
				// Dans le dropBombAttackAction on controle s'il ya une
				// situation on peut poser une bombe pour attaquer.
				ret = dropBombAttackAction();
		}
		if (ret.actionName != AiActionName.DROP_BOMB)

			if (endTileDestruct != null && currentTile == endTileDestruct) {
				if (inDanger(ownHero.getLine(), ownHero.getCol())) {
					ret = dangerAction();
				} else
					ret = dropBombDestructWallAction();

			} else {

				// Si on est en Danger
				if (inDanger(ownHero.getLine(), ownHero.getCol())) {
					// Fuir
					ret = dangerAction();
				} else {
					target = findRivalToAttack();
					// S'il ya un rival a attaquer
					if (target != null)
						// prendre la chemin a poser une bombe
						ret = moveattackAction(target);

					else {
						target = null;
						target = findClosestBONUSRANGE();
						// s'il ya une bonus range a prendre et nos range est
						// plus petit que 7
						if (target != null && ownHero.getBombRange() < 7) {
							if (inDanger(ownHero.getLine(), ownHero.getCol())) {
								ret = dangerAction();
							} else {
								// prendre la chemin a bonus
								ret = getBonusRangeAction(target);
							}
						} else {
							target = null;
							target = findClosestBONUSBOMBE();
							// s'il ya une bonus bombe a prendre et on a
							// moins de 5 bombes
							if (target != null && ownHero.getBombNumber() < 5) {
								if (inDanger(ownHero.getLine(), ownHero
										.getCol())) {
									ret = dangerAction();
								} else
									// prendre la chemin a bonus
									ret = getBonusBombeAction(target);
							} else {
								target = null;
								target = findTileForDestructible();
								//S'il y a des murs a detruire
								if ((target != null)) {
									if (inDanger(ownHero.getLine(), ownHero
											.getCol())) {
										ret = dangerAction();
									} else
										// prendre la chemin a poser une bombe
										ret = wallDestructAction(target);
								}
							}
							//Controle la direction ce qu'on vas prendre est en danger
							//Si elle est en danger 
							//Renvoir une direction a prendre.
							ret = isDirectionSafe(ret);

						}
					}
				}
			}

		nextTile = null;
		if (ret.actionName == AiActionName.DROP_BOMB) {
			endTileDestruct = null;
			endTileAttack = null;
			result = new AiAction(ret.actionName);
		} else {
			result = new AiAction(ret.actionName, ret.move);
			leftBomb = Direction.NONE;
		}
		return result;

	}

	/**
	 * control si la rival est assez proche et il est possible que le bombe peut
	 * effectuer la rival
	 * @param rival 
	 * @return renvoi true s'il est bien d'attaquer
	 * @throws StopRequestException 
	 */
	private boolean inAttackRange(AiTile rival) throws StopRequestException {
		checkInterruption();
		updateLocation();

		dZone = new DangerZone(zone, this);
		int ownHeroLine = ownHero.getLine();
		int ownHeroCol = ownHero.getCol();
		int rivalLine = rival.getLine();
		int rivalCol = rival.getCol();
		int range = ownHero.getBombRange();
		if (range > 5)
			range = 5;
		else if (range > 2)
			range = 3;
		boolean UP = false, DOWN = false, LEFT = false, RIGHT = false;

		if (ownHero.getTile() == rival) {
			return true;
		} else if (ownHeroLine != rivalLine && ownHeroCol != rivalCol) {
			return false;
		} else if (Math.abs(rivalCol - ownHeroCol) > range
				|| Math.abs(rivalLine - ownHeroLine) > range) {
			return false;
		} else {
			if (Math.abs(rivalCol - ownHeroCol) < range) {
				if (ownHeroLine == rivalLine) {
					if (rivalCol < ownHeroCol) {
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
								if (debugMode) {
									System.out
											.println("attak range patladû d................................................................UP"
													+ i);
									System.out.println(e.getStackTrace());
								}
							}
						}
					} else {
						for (int i = rivalCol - 1; i > ownHeroCol; i--) {
							checkInterruption(); // APPEL OBLIGATOIRE
							try {
								if (dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSBOMBE
										|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.BONUSRANGE
										|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.DESTRUCTIBLES
										|| dZone.getEnum(ownHeroLine, i) == ZoneEnum.INDESTRUCTIBLES) {

									DOWN = true;
									break;
								}
							} catch (Exception e) {
								if (debugMode) {
									System.out
											.println("attak range patladû d................................................................DOWN"
													+ i);
									System.out.println(e.getStackTrace());
								}
							}
						}
					}
				} else

				if (Math.abs(rivalLine - ownHeroLine) < range) {
					if (ownHeroCol == rivalCol) {
						if (rivalLine < ownHeroLine) {
							for (int i = rivalLine + 1; i < ownHeroLine; i++) {
								checkInterruption(); // APPEL OBLIGATOIRE
								try {
									if (dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSBOMBE
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSRANGE
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.DESTRUCTIBLES
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.INDESTRUCTIBLES) {

										LEFT = true;
										break;
									}
								} catch (Exception e) {
									if (debugMode) {
										System.out
												.println("attak range patladû d................................................................LEFT"
														+ i);
										System.out.println(e.getStackTrace());
									}
								}
							}
						} else {
							for (int i = rivalLine - 1; i > ownHeroLine; i--) {
								checkInterruption(); // APPEL OBLIGATOIRE
								try {
									if (dZone.getEnum(ownHeroCol, i) == ZoneEnum.BONUSBOMBE
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.BONUSRANGE
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.DESTRUCTIBLES
											|| dZone.getEnum(i, ownHeroCol) == ZoneEnum.INDESTRUCTIBLES) {
										RIGHT = true;
										break;
									}
								} catch (Exception e) {
									if (debugMode) {
										System.out
												.println("attak range patladû d................................................................RIGHT"
														+ i);
										System.out.println(e.getStackTrace());
									}
								}
							}
						}
					}
				}
			}
			if (!UP && !DOWN && !LEFT && !RIGHT)
				return true;
			else
				return false;

		}
	}

	/**
	 * control la Danger de la Direction ce qu'on va prendre
	 * @param move 
	 * @return renvoi une direction plus sur
	 * @throws StopRequestException 
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
							endTileDestruct = null;
						} else {
							move.move = zone
									.getDirection(currentTile, nextTile);
						}
					}
					if (dangerUP) {
						if (dir == Direction.UP || dir == Direction.UPLEFT
								|| dir == Direction.UPRIGHT) {
							move.move = Direction.NONE;
							endTileDestruct = null;
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
								endTileDestruct = null;
							} else {
								move.move = zone.getDirection(currentTile,
										nextTile);
							}
						}
						if (dangerRight) {
							if (dir == Direction.RIGHT) {
								endTileDestruct = null;
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
	 * @throws StopRequestException 
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
	 * @param line 
	 * @param col 
	 * @return renvoi true si ce Tile est mur
	 * @throws StopRequestException 
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
	 * @param line 
	 * @param col 
	 * @return renvoi true si ce Tile est en danger
	 * @throws StopRequestException 
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
	 * @param line 
	 * @param col 
	 * @return renvoi true si ce Tile est en danger
	 * @throws StopRequestException 
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
			checkInterruption(); // APPEL OBLIGATOIRE
			boolean isrange = false;
			List<AiTile> range = (ArrayList<AiTile>) aiBomb.getBlast();
			for (AiTile aiTile : range) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if (aiTile.getCol() == col && aiTile.getLine() == line)
					isrange = true;
			}
			if (isrange) {
				if (aiBomb.isWorking()
						&& (aiBomb.getLatencyDuration() - aiBomb.getTime()) < (ownHero
								.getWalkingSpeed() * 400))
					ret = true;

			}
		}

		return ret;
	}

	/**
	 * Tout cas sauf les bombs, flammes, feu et murs sont Safe
	 * @param line 
	 * @param col 
	 * @return renvoi true si ce Tile est sur
	 * @throws StopRequestException 
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
	 * @return renvoie la Tile de la plus proche Bonus Range
	 * @throws StopRequestException 
	 */
	private AiTile findClosestBONUSRANGE() throws StopRequestException {
		checkInterruption();
		updateLocation();
		dZone = new DangerZone(zone, this);
		AiTile temp = null;
		int tempLength = 100;
		pathManager = null;
		for (int i = 0; i < zone.getHeight(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
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
	 * @return renvoie la Tile de la plus proche Bonus Bombe
	 * @throws StopRequestException 
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
	 * @return renvoie la Tile plus proche et Safe
	 * @throws StopRequestException 
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
//							DangerZone dZone = new DangerZone(zone, this);
//							if (dZone.getEnum(i, j) != ZoneEnum.BONUSBOMBE
//									&& dZone.getEnum(i, j) != ZoneEnum.BONUSBOMBE)
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
	 * @return renvoie une Tile ou on peut detruir plus de murs destructible
	 * @throws StopRequestException 
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
					if (pathManager.isWalkable() || (getCurrentTile() == tile)) {

						// if (pathManager.getLength() < 25) {
						if (destructibleVoisin < countDestructibleVoisins(tile
								.getLine(), tile.getCol())) {
							int range = ownHero.getBombRange(); // 
							if (debugMode)
								System.out
										.println("range-----------------------------------------"
												+ range);
							if (isRangeClean(range + 1, tile)) {
								temp = tile;
								destructibleVoisin = countDestructibleVoisins(
										temp.getLine(), temp.getCol());
							}
							// }
						}
					}

				}

			}
		}
		pathManager = null;
		return temp;
	}

	/**
	 * @return renvoie la Tile de la plus proche adversaire
	 * @throws StopRequestException 
	 */
	private AiTile findRivalToAttack() throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		updateLocation();
		AiTile temp = null;
		int tempLength = 100;
		for (AiHero rival : zone.getRemainingHeroes()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			AiTile tile = rival.getTile();

			if (!rival.equals(ownHero)) {
				if (rival.getTile() == ownHero.getTile()) {
					temp = rival.getTile();
					tempLength = 0;
				} else {
					pathManager = new PathManagement(this, tile);

					if (pathManager.isWalkable()) {
						if (pathManager.getLength() >= 0
								&& pathManager.getLength() < tempLength) {

							temp = tile;
							tempLength = pathManager.getLength();

						}
					}
				}
			}
		}
		return temp;
	}

	/**
	 * @param line 
	 * @param col 
	 * @return renvoie le nombre de voisins qui vont detruire si on pose une bombe a
	 * cette Tile
	 * @throws StopRequestException 
	 */
	public int countDestructibleVoisins(int line, int col)
			throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
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
	 * @param a 
	 * @throws StopRequestException 
	 * 
	 */
	public void goTroughPath(List<AiTile> a) throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		updateLocation();
		Iterator<AiTile> it = a.iterator();

		if (it.hasNext()) {
			nextTile = it.next();
		} else
			nextTile = currentTile;
	}

	/**
	 * controle si on a la chance de fuir.
	 * @param range 
	 * @param bomb 
	 * @return renvoi une direction pour a fuir. 
	 * @throws StopRequestException 
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
	 * @param range 
	 * @param bomb 
	 * @return renvoi true si on a la chance de fuir
	 * @throws StopRequestException 
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
	 * @param array 
	 * @return renvoi true si la path est en danger
	 * @throws StopRequestException 
	 */
	public boolean pathInDanger(List<AiTile> array)
			throws StopRequestException {
		checkInterruption(); // Appel Obligatoire
		updateLocation();
		boolean result = false;
		for (AiTile aiTile : array) {
			checkInterruption(); // Appel Obligatoire
			if (inDanger(aiTile.getLine(), aiTile.getCol())) {
				result = true;
			}
		}
		return result;
	}
}