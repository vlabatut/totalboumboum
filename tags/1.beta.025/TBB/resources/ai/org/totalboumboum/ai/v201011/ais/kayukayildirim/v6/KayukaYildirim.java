package org.totalboumboum.ai.v201011.ais.kayukayildirim.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Pol Kayuka
 * @author Ozan Yıldırım
 */
@SuppressWarnings({ "unused", "deprecation" })
public class KayukaYildirim extends ArtificialIntelligence {
	/** */
	private final int VIDE = 0;
	/** */
	private final int INDES = -400;
	/** */
	private final int DES = -400;
	/** */
	private final int BOMB = -300;
	/** */
	private final int EFBNS = 100;
	/** */
	private final int BOMBBNS = 100;
	/** */
	private final int ADV = -20;
	/** */
	private final int OWNHERO = 0;
	/** */
	private final int FLAME = -300;
	/** */
	private final int BADV = 5;
	/** */
	private AiZone gameMap;
	/** */
	private AiOutput out;
	/** */
	private double[][] matrice;
	/** */
	private AiHero ourOwnHero;
	/** */
	public AiPath nextMove = null;
	/** */
	private Direction[] dirTable = { Direction.DOWN, Direction.RIGHT,
			Direction.UP, Direction.LEFT };
	/** */
	private Direction moveDir;
	/** */
	ArtificialIntelligence ai = this;
	/** */
	private Astar astar;
	/** */
	private AiPath path;
	/** */
	private List<AiBlock> possibleBurningWalls;

	/**
	 * 
	 * @param tile1
	 * 		description manquante !
	 * @param tile2
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private Direction actionDirection(AiTile tile1, AiTile tile2)
			throws StopRequestException {
		checkInterruption();
		Direction result;
		int dx;
		double dy;
		dx = (tile2.getLine()) - (tile1.getLine());
		dy = (tile2.getCol()) - (tile1.getCol());

		if (dx < 0 && dy == 0) {
			result = Direction.UP;
		} else if (dx < 0 && dy < 0) {
			result = Direction.UP;
		} else if (dx == 0 && dy < 0) {
			result = Direction.LEFT;
		} else if (dx > 0 && dy == 0) {
			result = Direction.DOWN;
		} else if (dx > 0 && dy > 0) {
			result = Direction.DOWN;
		} else if (dx == 0 && dy > 0) {
			result = Direction.RIGHT;
		} else if (dx > 0 && dy < 0) {
			result = Direction.DOWN;
		} else if (dx < 0 && dy > 0) {
			result = Direction.UP;
		} else {
			result = Direction.NONE;
		}

		return result;
	}

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		gameMap = getPercepts();
		ourOwnHero = gameMap.getOwnHero();
		out = getOutput();
		matrice = new double[gameMap.getHeight()][gameMap.getWidth()];
//		System.out.print(ourOwnHero.getBombNumberMax());
		if (isModeCollecte()) {
			if (poserBombeCollecte())
				result = new AiAction(AiActionName.DROP_BOMB);
			fillCollecte(matrice, gameMap);
			AiTile maxTile = getMaxTile();
			getMouvementDirection(ourOwnHero.getTile(), maxTile);

			try {
				if (matrice[ourOwnHero.getTile().getNeighbor(moveDir).getLine()][gameMap
						.getOwnHero().getTile().getNeighbor(moveDir).getCol()] > -250)
					result = new AiAction(AiActionName.MOVE, this.moveDir);
				else
					result = new AiAction(AiActionName.NONE, this.moveDir);
			} catch (NullPointerException e) {
				e.getStackTrace();
			}

		}

		else {
			if (poserBombeAttack())
				result = new AiAction(AiActionName.DROP_BOMB);
			fillAttaque(matrice, gameMap);
			{
				getMouvementDirection(this.gameMap.getOwnHero().getTile(),
						getMaxTile());
				try {
					if (matrice[ourOwnHero.getTile().getNeighbor(moveDir)
							.getLine()][gameMap.getOwnHero().getTile()
							.getNeighbor(moveDir).getCol()] > -250)
						result = new AiAction(AiActionName.MOVE, this.moveDir);
					else
						result = new AiAction(AiActionName.NONE, moveDir);
				} catch (NullPointerException e) {
					e.getStackTrace();
				}
			}
		}
		// printMatrixtoConsole(matrice);
		// printOnScreen(matrice,out);

		return result;
	}

	/**
	 * 
	 * @param matriceCollecte
	 * 		description manquante !
	 * @param gameMap
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillCollecte(double[][] matriceCollecte, AiZone gameMap)
			throws StopRequestException {
		checkInterruption();
		this.initialiseMap(matriceCollecte);
		this.fillwithInDes(matriceCollecte);
		this.fillwithBombs(matriceCollecte);
		this.fillwithBns(matriceCollecte);
		this.fillwithDes(matriceCollecte);
		this.takeHeroes(matriceCollecte);
		this.takeFireHoles(matriceCollecte);
	}

	/**
	 * 
	 * @param matriceCollecte
	 * 		description manquante !
	 * @param gameMap
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillAttaque(double[][] matriceCollecte, AiZone gameMap)
			throws StopRequestException {
		checkInterruption();
		this.initialiseMap(matriceCollecte);
		this.fillwithInDes(matriceCollecte);
		this.fillwithBombs(matriceCollecte);
		this.aFillWithBns(matriceCollecte);
		this.fillwithDes(matriceCollecte);
		this.aTakeHeroes(matriceCollecte);
		this.takeFireHoles(matriceCollecte);
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void initialiseMap(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		for (int i = 0; i < gameMap.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameMap.getWidth(); j++) {
				checkInterruption();
				matriceCollect[i][j] = VIDE;
			}
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillwithInDes(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> allblocks = gameMap.getBlocks();
		Iterator<AiBlock> itallblocks = allblocks.iterator();
		while (itallblocks.hasNext()) {
			checkInterruption();
			AiBlock block = itallblocks.next();
			if (!(block.isDestructible())) {
				matriceCollect[block.getLine()][block.getCol()] = INDES;
			}
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillwithDes(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> allblocks = gameMap.getBlocks();
		Iterator<AiBlock> itallblocks = allblocks.iterator();
		while (itallblocks.hasNext()) {
			checkInterruption();
			AiBlock block = itallblocks.next();
			if (block.isDestructible()) {
				matriceCollect[block.getLine()][block.getCol()] += DES;
				matriceCollect[(block.getLine() - 1) % gameMap.getWidth()][block
						.getCol()] += 10;
				matriceCollect[(block.getLine() + 1) % gameMap.getWidth()][block
						.getCol()] += 10;
				matriceCollect[block.getLine()][(block.getCol() - 1)
						% gameMap.getHeight()] += 10;
				matriceCollect[block.getLine()][(block.getCol() + 1)
						% gameMap.getHeight()] += 10;

			}
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillwithBombs(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBomb> bombs = gameMap.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();

		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] += BOMB;

			Collection<AiTile> inScopeTiles = bomb.getBlast();

			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();

			while (iteratorScope.hasNext()) {
				checkInterruption();
				AiTile blastCase = iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] += (int) ((-100 * (bomb
						.getTime())) / 1000);

			}
		}
	}

	/**
	 * 
	 * @param l
	 * 		description manquante !
	 * @param c
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private int distancePlusProcheAdversaire(int l, int c)
			throws StopRequestException {
		checkInterruption();
		int min = gameMap.getHeight() + gameMap.getWidth();
		int d;
		Collection<AiHero> allheroes = gameMap.getHeroes();
		Iterator<AiHero> itheroes = allheroes.iterator();
		while (itheroes.hasNext()) {
			checkInterruption();
			AiHero hero = itheroes.next();
			if (hero != gameMap.getOwnHero()) {
				d = (int) gameMap.getPixelDistance(l, c, hero.getLine(),
						hero.getCol());
				if (d < min)
					min = d;
			}
		}
		if (min == 0)
			min = 1;
		return min;

	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void fillwithBns(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		int x = ourOwnHero.getLine();
		int y = ourOwnHero.getCol();
		int Z = gameMap.getHeight() + gameMap.getWidth();
		Collection<AiItem> allbns = gameMap.getItems();
		Iterator<AiItem> itbns = allbns.iterator();
		while (itbns.hasNext()) {
			checkInterruption();
			AiItem bns = itbns.next();
			if (bns.getType().toString() == "EXTRA_BOMB") {
				if (gameMap.getPixelDistance(x, y, bns.getLine(), bns.getCol()) == 0)
					matriceCollect[bns.getLine()][bns.getCol()] += (int) (BOMBBNS
							* (3 / ourOwnHero.getBombNumberMax()) + Z - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
				else
					matriceCollect[bns.getLine()][bns.getCol()] += (int) (BOMBBNS
							* (3 / ourOwnHero.getBombNumberMax())
							+ Z
							/ gameMap.getPixelDistance(x, y, bns.getLine(),
									bns.getCol()) - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
			} else {
				if (gameMap.getPixelDistance(x, y, bns.getLine(), bns.getCol()) == 0)
					matriceCollect[bns.getLine()][bns.getCol()] += (int) (EFBNS
							* Z / (4 * ourOwnHero.getBombRange()) + Z - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
				else
					matriceCollect[bns.getLine()][bns.getCol()] += (int) (EFBNS
							* Z
							/ (4 * ourOwnHero.getBombRange())
							+ Z
							/ gameMap.getPixelDistance(x, y, bns.getLine(),
									bns.getCol()) - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
			}
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void takeHeroes(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		Collection<AiHero> allheroes = gameMap.getHeroes();
		Iterator<AiHero> itheroes = allheroes.iterator();
		while (itheroes.hasNext()) {
			checkInterruption();
			AiHero hero = itheroes.next();
			if (hero != gameMap.getOwnHero())
				matriceCollect[hero.getLine()][hero.getCol()] += ADV;
			else
				matriceCollect[hero.getLine()][hero.getCol()] += OWNHERO;
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void takeFireHoles(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		Collection<AiFire> splashes = gameMap.getFires();
		Iterator<AiFire> itsplash = splashes.iterator();
		while (itsplash.hasNext()) {
			checkInterruption();
			AiFire splash = itsplash.next();
			matriceCollect[splash.getLine()][splash.getCol()] = FLAME;
		}
	}

	/**
	 * 
	 * @param matriceCollect
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void printMatrixtoConsole(double[][] matriceCollect)
			throws StopRequestException {
		checkInterruption();
		int i = 0;
		while (i < matriceCollect.length) {
			checkInterruption();
			int j = 0;
			while (j < matriceCollect[i].length) {
				checkInterruption();
				System.out.print("[" + matriceCollect[i][j] + "]");
				j = j + 1;
			}
			System.out.print("\n");
			i = i + 1;
		}
		System.out.print("-----\n");
	}

	/**
	 * 
	 * @param matrice
	 * 		description manquante !
	 * @param out
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void printOnScreen(double[][] matrice, AiOutput out)
			throws StopRequestException {
		checkInterruption();
		for (int i = 0; i < matrice.length; i++) {
			checkInterruption();
			for (int j = 0; j < matrice[i].length; j++) {
				checkInterruption();
				out.setTileText(i, j, Double.toString(matrice[i][j]));
				if (matrice[i][j] == 0) {
					checkInterruption();
					out.setTileColor(i, j, Color.WHITE);

				} else if (matrice[i][j] > 0) {
					checkInterruption();
					out.setTileColor(i, j, Color.GREEN);
				} else {
					checkInterruption();
					out.setTileColor(i, j, Color.RED);
				}
			}
		}
	}

	/**
	 * 
	 * @param matriceAttaque
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void aFillWithBns(double[][] matriceAttaque)
			throws StopRequestException {
		checkInterruption();
		int x = ourOwnHero.getLine();
		int y = ourOwnHero.getCol();
		int Z = gameMap.getHeight() + gameMap.getWidth();
		Collection<AiItem> allbns = gameMap.getItems();
		Iterator<AiItem> itbns = allbns.iterator();
		while (itbns.hasNext()) {
			checkInterruption();
			AiItem bns = itbns.next();
			if (bns.getType().toString() == "EXTRA_BOMB") {
				if (gameMap.getPixelDistance(x, y, bns.getLine(), bns.getCol()) == 0)
					matriceAttaque[bns.getLine()][bns.getCol()] += (int) (10
							* (3 / ourOwnHero.getBombNumberMax()) + Z - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
				else
					matriceAttaque[bns.getLine()][bns.getCol()] += (int) (10
							* (3 / ourOwnHero.getBombNumberMax())
							+ Z
							/ gameMap.getPixelDistance(x, y, bns.getLine(),
									bns.getCol()) - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
			} else {
				if (gameMap.getPixelDistance(x, y, bns.getLine(), bns.getCol()) == 0)
					matriceAttaque[bns.getLine()][bns.getCol()] += (int) (10
							* Z / (4 * ourOwnHero.getBombRange()) + Z - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
				else
					matriceAttaque[bns.getLine()][bns.getCol()] += (int) (10
							* Z
							/ (4 * ourOwnHero.getBombRange())
							+ Z
							/ gameMap.getPixelDistance(x, y, bns.getLine(),
									bns.getCol()) - Z
							/ distancePlusProcheAdversaire(bns.getLine(),
									bns.getCol()));
			}
		}
	}

	/**
	 * 
	 * @param number
	 * 		description manquante !
	 * @param mod
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	int superMod(int number, int mod) throws StopRequestException {
		checkInterruption();
		if (number >= 0) {
			return number % mod;
		} else {
			while (number < 0) {
				checkInterruption();
				number += mod;
			}
			return number;
		}
	}

	/**
	 * 
	 * @param matriceAttaque
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void aTakeHeroes(double[][] matriceAttaque)
			throws StopRequestException {
		checkInterruption();
		int Z = gameMap.getHeight() + gameMap.getWidth();
		int x, y, blast, i;
		Collection<AiHero> allheroes = gameMap.getHeroes();
		Iterator<AiHero> itheroes = allheroes.iterator();
		while (itheroes.hasNext()) {
			checkInterruption();
			AiHero hero = itheroes.next();
			x = hero.getLine();
			y = hero.getCol();
			blast = ourOwnHero.getBombRange();
			if (blast > gameMap.getHeight())
				blast = gameMap.getHeight();
			if (blast > gameMap.getHeight())
				blast = gameMap.getHeight();
			if (hero != gameMap.getOwnHero()) {
				matriceAttaque[hero.getLine()][hero.getCol()] += 0;
				for (i = 1; i < blast + 1; i++) {
					checkInterruption();
					if (matriceAttaque[x][y + i] != -400) {
						if (gameMap.getPixelDistance(ourOwnHero.getLine(),
								ourOwnHero.getCol(), hero.getLine(),
								hero.getCol()) < 1)
							matriceAttaque[x][superMod(y + i,
									gameMap.getWidth())] += (int) (BADV * (Z / 1));
						else
							matriceAttaque[x][superMod(y + i,
									gameMap.getWidth())] += (int) (BADV * (Z / gameMap
									.getPixelDistance(ourOwnHero.getLine(),
											ourOwnHero.getCol(),
											hero.getLine(), hero.getCol())));
						if (matriceAttaque[x][superMod(y + i,
								gameMap.getWidth())] < 200)
							matriceAttaque[x][superMod(y + i,
									gameMap.getWidth())] += 200;
					} else
						i = blast;
				}
				for (i = 1; i < blast + 1; i++) {
					checkInterruption();
					if (matriceAttaque[x][y - i] != -400) {
						if (gameMap.getPixelDistance(ourOwnHero.getLine(),
								ourOwnHero.getCol(), hero.getLine(),
								hero.getCol()) < 1)
							matriceAttaque[x][superMod(y - i,
									gameMap.getWidth())] += (int) (BADV * (Z / 1));
						else
							matriceAttaque[x][superMod(y - i,
									gameMap.getWidth())] += (int) (BADV * (Z / gameMap
									.getPixelDistance(ourOwnHero.getLine(),
											ourOwnHero.getCol(),
											hero.getLine(), hero.getCol())));
						if (matriceAttaque[x][superMod(y - i,
								gameMap.getWidth())] < 200)
							matriceAttaque[x][superMod(y - i,
									gameMap.getWidth())] += 200;
					} else
						i = blast;
				}
				for (i = 1; i < blast + 1; i++) {
					checkInterruption();
					if (matriceAttaque[x + i][y] != -400) {
						if (gameMap.getPixelDistance(ourOwnHero.getLine(),
								ourOwnHero.getCol(), hero.getLine(),
								hero.getCol()) < 1)
							matriceAttaque[superMod((x + i),
									gameMap.getHeight())][y] += (int) (BADV * (Z / 1));
						else
							matriceAttaque[superMod((x + i),
									gameMap.getHeight())][y] += (int) (BADV * (Z / gameMap
									.getPixelDistance(ourOwnHero.getLine(),
											ourOwnHero.getCol(),
											hero.getLine(), hero.getCol())));
						if (matriceAttaque[superMod((x + i),
								gameMap.getHeight())][y] > 200)
							matriceAttaque[superMod((x + i),
									gameMap.getHeight())][y] += 200;
					} else
						i = blast;
				}
				for (i = 1; i < blast + 1; i++) {
					checkInterruption();

					if (matriceAttaque[x - i][y] != -400) {
						if (gameMap.getPixelDistance(ourOwnHero.getLine(),
								ourOwnHero.getCol(), hero.getLine(),
								hero.getCol()) < 1)
							matriceAttaque[superMod((x - i),
									gameMap.getHeight())][y] += (int) (BADV * (Z / 1));
						else
							matriceAttaque[superMod((x - i),
									gameMap.getHeight())][y] += (int) (BADV * (Z / gameMap
									.getPixelDistance(ourOwnHero.getLine(),
											ourOwnHero.getCol(),
											hero.getLine(), hero.getCol())));
						if (matriceAttaque[superMod((x - i),
								gameMap.getHeight())][y] > 200)
							matriceAttaque[superMod((x - i),
									gameMap.getHeight())][y] += 200;
					} else
						i = blast;
				}

			} else
				matriceAttaque[hero.getLine()][hero.getCol()] += OWNHERO;
		}

	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private boolean poserBombeCollecte() throws StopRequestException {
		checkInterruption();
		boolean dropBomb = false;
		if (runAway(ourOwnHero.getTile()) == true)
			if ((!accessible(ourOwnHero) && listOfBlocksInBlast(
					ourOwnHero.getTile(), ourOwnHero.getBombRange()).size() > 0))
				// if(ourOwnHero.getBombNumberCurrent()!=0)
				dropBomb = true;

		/*
		 * if(ourOwnHero.getBombNumberCurrent()!=0) {
		 * if(isRunnable(ourOwnHero.getTile())==true){
		 * //if(gameMap.getHiddenItemsCount()>0) if(getWillBurnWalls()>0)
		 * dropBomb=true; else dropBomb=false; //else //dropBomb=false; } else
		 * dropBomb=false; } else dropBomb=false;
		 */
		return dropBomb;
	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private boolean poserBombeAttack() throws StopRequestException {
		checkInterruption();
		boolean dropBomb = false;
		if (runAway(ourOwnHero.getTile()) == true)
			if (heroesInRange(ourOwnHero.getTile(), ourOwnHero.getBombRange())
					.size() > 0
					|| (!accessible(ourOwnHero) && listOfBlocksInBlast(
							ourOwnHero.getTile(), ourOwnHero.getBombRange())
							.size() > 0))
				dropBomb = true;
		return dropBomb;

	}

	/**
	 * 
	 * @param tile
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private List<AiHero> heroesInRange(AiTile tile, int range)
			throws StopRequestException {
		checkInterruption();

		List<AiTile> ourBombRange = new ArrayList<AiTile>();
		List<AiHero> inRangeHeroes = new ArrayList<AiHero>();

		ourBombRange = getBombRangeList(tile, range);

		for (AiTile r : ourBombRange) {
			checkInterruption();
			if (r.getHeroes().size() != 0) {
				if (r.getHeroes().get(0) != ourOwnHero) {
					inRangeHeroes.add(r.getHeroes().get(0));
				}
			}
		}
		return inRangeHeroes;
	}

	/**
	 * 
	 * @param hero
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private boolean accessible(AiHero hero) throws StopRequestException {
		checkInterruption();
		boolean bool = false;
		int i = 0;
		AiTile tile = hero.getTile();
		for (Direction dir : dirTable) {
			checkInterruption();
			if (tile.getNeighbor(dir).isCrossableBy(ourOwnHero, false, false,
					false, true, true, true))
				i++;
		}
		if (i > 2)
			bool = true;
		return bool;
	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private List<AiBlock> getWillBurnWalls() throws StopRequestException {
		checkInterruption();

		possibleBurningWalls = new ArrayList<AiBlock>();
		List<AiTile> allRangeList;
		allRangeList = getAllRangeList();
		Iterator<AiTile> itRange = allRangeList.iterator();
		AiTile range;
		while (itRange.hasNext()) {
			checkInterruption();
			range = itRange.next();
			if (range.getBlocks().size() != 0) {
				if (range.getBlocks().get(0).isDestructible()) {
					possibleBurningWalls.add(range.getBlocks().get(0));
				}
			}
		}
		return possibleBurningWalls;
	}

	/**
	 * 
	 * @param tile
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getBombRangeList(AiTile tile, int range)
			throws StopRequestException {
		checkInterruption();
		List<AiTile> bombRangeList = new ArrayList<AiTile>();

		AiTile neighbourTile, tempTile = tile;
		int i = 0;
		boolean crossableItem;
		boolean crossable;

		for (Direction dir : this.dirTable) {
			checkInterruption();
			crossableItem = true;
			crossable = true;
			i = 0;
			tile = tempTile;
			while (i < range && crossable == true) {
				checkInterruption();
				neighbourTile = tile.getNeighbor(dir);
				if (neighbourTile.getItems().size() == 0) {
					crossableItem = true;
				} else {
					crossableItem = false;
				}

				if (neighbourTile.isCrossableBy(gameMap.getOwnHero())
						&& crossableItem) {
					i++;
					tile = neighbourTile;
					bombRangeList.add(neighbourTile);
				} else {
					crossable = false;
					bombRangeList.add(neighbourTile);
				}
			}
		}
		return bombRangeList;
	}

	/**
	 * 
	 * @param bomb
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public boolean runAway(AiTile bomb) throws StopRequestException {
		checkInterruption();
		boolean canIRun = false;
		boolean bool = false;

		List<AiTile> trySafeList = new ArrayList<AiTile>();
		List<AiTile> allRangeList = new ArrayList<AiTile>();
		allRangeList.addAll(getAllRangeList());

		for (Direction dir : dirTable) {
			checkInterruption();
			allRangeList.addAll(getONeighbour(bomb, dir, gameMap.getOwnHero()
					.getBombRange()));
		}
		allRangeList.add(bomb);

		for (Direction dir : dirTable) {
			checkInterruption();
			trySafeList.addAll(getONeighbour(bomb, dir, 2));
		}

		for (AiTile s1 : trySafeList) {
			checkInterruption();
			for (AiTile s2 : s1.getNeighbors()) {
				checkInterruption();
				if (s2.isCrossableBy(gameMap.getOwnHero())) {
					bool = false;
					for (AiTile d : allRangeList) {
						checkInterruption();
						if (s2 == d) {
							bool = true;
						}
					}
					if (bool == false) {
						canIRun = true;
					}
				}
			}
		}

		return canIRun;
	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getAllRangeList() throws StopRequestException {
		checkInterruption();
		List<AiTile> allRangeList;
		allRangeList = new ArrayList<AiTile>();
		List<AiBomb> bombsList = gameMap.getBombs();
		for (AiBomb bomb : bombsList) {
			checkInterruption();
			allRangeList.addAll(bomb.getBlast());
		}
		return allRangeList;
	}

	/**
	 * 
	 * @param tile
	 * 		description manquante !
	 * @param dir
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> getONeighbour(AiTile tile, Direction dir, int range)
			throws StopRequestException {
		checkInterruption();
		int i = 0;
		boolean crossable = true;
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile tempTile;
		while (i < range && crossable) {
			checkInterruption();
			tempTile = tile.getNeighbor(dir);
			if (tempTile.isCrossableBy(gameMap.getOwnHero())) {
				result.add(tempTile);
				i++;
				tile = tempTile;
			} else
				crossable = false;
		}

		return result;
	}

	/**
	 * 
	 * @param tile1
	 * 		description manquante !
	 * @param tile2
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 * @throws NullPointerException
	 * 		description manquante !
	 */
	private void getMouvementDirection(AiTile tile1, AiTile tile2)
			throws StopRequestException, NullPointerException {
		checkInterruption();
		CostCalculator cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		SuccessorCalculator succes = new BasicSuccessorCalculator();
		this.astar = new Astar(ai, ourOwnHero, cost, heuristic, succes);
		this.path = null;
		try {
			this.path = this.astar.processShortestPath(tile1, tile2);
		} catch (LimitReachedException e1) {
			//
			e1.printStackTrace();
		}

		if (this.path.getTiles().size() != 0) {
			AiPath tempPath1 = null;
			AiPath tempPath2 = this.path;
			List<AiTile> casesList = new ArrayList<AiTile>();
			casesList = this.path.getTiles();
			Iterator<AiTile> cases = casesList.iterator();
			AiTile tile;
			while (cases.hasNext()) {
				checkInterruption();
				tile = cases.next();
				if (tile != tile1) {

					try {
						tempPath1 = this.astar.processShortestPath(tile1, tile);
					} catch (LimitReachedException e) {
						//
						e.printStackTrace();
					}

					if (tempPath1.getTiles().size() != 0
							&& tempPath1.compareTo(tempPath2) == -1)
						tempPath2 = tempPath1;
				}
			}
			this.moveDir = this.gameMap.getDirection(tile1,
					tempPath2.getLastTile());
		} else {
			this.moveDir = actionDirection(tile1, tile2);
		}
	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private AiTile getMaxTile() throws StopRequestException {
		checkInterruption();
		int maxLine = 0, maxCol = 0;
		double[][] matrix = matrice;
		double max = -1;

		int line, col;
		CostCalculator cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		SuccessorCalculator succes = new BasicSuccessorCalculator();
		this.astar = new Astar(this, ourOwnHero, cost, heuristic, succes);
		this.path = null;
		for (line = 0; line < gameMap.getHeight(); line++) {
			checkInterruption();
			for (col = 0; col < gameMap.getWidth(); col++) {
				checkInterruption();

				try {
					this.path = this.astar.processShortestPath(
							ourOwnHero.getTile(), gameMap.getTile(line, col));
				} catch (LimitReachedException e) {
					//
					e.printStackTrace();
				}

				if (matrix[line][col] > max && path.getTiles().size() != 0) {
					max = matrix[line][col];
					maxLine = line;
					maxCol = col;
				}
			}
		}

		return (gameMap.getTile(maxLine, maxCol));
	}

	/**
	 * 
	 * @param ownHero
	 * 		description manquante !
	 * @param startPoint
	 * 		description manquante !
	 * @param endPoints
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public AiPath shortestPath(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		AiPath shortestPath = null;
		Astar astar;
		CostCalculator cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
		try {
			shortestPath = astar.processShortestPath(startPoint, endPoints);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
		return shortestPath;
	}

	/**
	 * 
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private boolean isModeCollecte() throws StopRequestException {
		checkInterruption();
		boolean collecte;

		int blast = ourOwnHero.getBombRange();
		int bmbnb = ourOwnHero.getBombNumberMax();
		int h = (gameMap.getHeight() + gameMap.getWidth()) / 4;

		if (bmbnb < 3 && !gameMap.getItems().isEmpty())
			collecte = true;
		else if (blast < h && !gameMap.getItems().isEmpty())
			collecte = true;
		else
			collecte = false;
		return collecte;
	}

	/**
	 * 
	 * @param tile
	 * 		description manquante !
	 * @param range
	 * 		description manquante !
	 * @return ?
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private List<AiBlock> listOfBlocksInBlast(AiTile tile, int range)
			throws StopRequestException {
		checkInterruption();

		boolean inBlast;
		List<AiTile> ourBombRange = new ArrayList<AiTile>();
		List<AiBlock> blocksInBlast = new ArrayList<AiBlock>();

		ourBombRange = getBombRangeList(tile, range);
		possibleBurningWalls = getWillBurnWalls();

		for (AiTile r : ourBombRange) {
			checkInterruption();
			if (r.getBlocks().size() != 0) {
				if (r.getBlocks().get(0).isDestructible()) {
					inBlast = false;
					for (AiBlock w : possibleBurningWalls) {
						checkInterruption();
						if (r == w.getTile()) {
							inBlast = true;
						}
					}
					if (inBlast == false) {
						blocksInBlast.add(r.getBlocks().get(0));
					}
				}
			}
		}
		return blocksInBlast;
	}
}
