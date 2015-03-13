package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 *         >> Our bomberman can be considered as a bit defensive one because of
 *         the strategies choosen while attacking and defending. Also it is
 *         giving big importance not to die even in collecting the bonus and
 *         attack by tryig to choose the most secure cases.
 */

/* We made a better explanation about the use of each method in the classes. */

@SuppressWarnings("deprecation")
public class DemirciDuzokErgok extends ArtificialIntelligence {

	/** */
	private AiZone IA_ZONE = null;

	/** */
	private AiHero ourBomberman = null;

	/** */
	private AiTile ourTile = null;
	/** */
	private int q = 0;
	/** */
	private int qTest = 0;
	/** */
	private int ourX;
	/** */
	private int ourY;
	/** */
	private SafetyMap safeMap;

	/** */
	int numbBonus = 0;

	/** */
	List<AiTile> possibleDestW;
	/** */
	List<AiTile> possibleDestBonus;

	/**
	 * we declared the global variable to seperate the actions exploring the
	 * walls when the enemy is not accessible and accessing to the enemie
	 */
	private long kkk = GlobalClass.globalData;

	/** */
	Direction moveDir;

	/**
	 * The classes for defense,collecting bonus,exploring walls and ,attacking
	 * the enemies
	 */
	private BonusManager bonusManager = null;
	/** */
	private EscapeManager escapeManager = null;
	/** */
	private WallManager wallManager = null;
	/** */
	private EnemyManager enemyManager = null;
	/** */
	private WallManager2 wallManager2 = null;

	/** */
	AiPath pathB;
	/** */
	public Astar aStar;
	/** */
	public static int aStarQueueSize = 0;
	/** */
	public HeuristicCalculator heuristicCalculator;
	/** */
	public MatrixCostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		IA_ZONE = getPercepts();
		ourBomberman = IA_ZONE.getOwnHero();
		
		// init A*
		double costMatrix[][] = new double[IA_ZONE.getHeight()][IA_ZONE.getWidth()];
		for(int i=0;i<IA_ZONE.getHeight();i++)
			for(int j=0;j<IA_ZONE.getWidth();j++)
				costMatrix[i][j] = 1;
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		aStar = new Astar(this, ourBomberman, costCalculator, heuristicCalculator);
		aStar.setMaxNodes(50);
		
		// init managers
		escapeManager = null;
		wallManager = null;
		enemyManager = null;
		wallManager2 = null;
	}

	/**
	 * Test : maintains the maximal frange size.
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void updateAstarQueueSize() throws StopRequestException
	{	int newSize = aStar.getQueueMaxSize();
		if(newSize>aStarQueueSize)
		{	aStarQueueSize = newSize;
//			System.out.println(">>>>>>> DemirciDuzokErgok: "+aStarQueueSize+"("+IA_ZONE.getHeight()+"x"+IA_ZONE.getWidth()+")");
		}
	}
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		if(ourBomberman==null)
			init();
		
		ourTile = ourBomberman.getTile();

		ourX = ourTile.getCol();
		ourY = ourTile.getLine();

		AiAction result = new AiAction(AiActionName.NONE);

		if (ourBomberman.hasEnded() == false) {

			q = 0;
			qTest = 0;
			moveDir = Direction.NONE;
			safeMap = new SafetyMap(IA_ZONE, this);

//			int k = 0;
			for (int i = 0; i < IA_ZONE.getHeight(); i++) {
				checkInterruption();
				for (int j = 0; j < IA_ZONE.getWidth(); j++) {
					checkInterruption();
//					if (safe_map.returnMatrix()[i][j] == safe_map.DEST_WALL)
//						k++;
				}
			}

			/*
			 * If we are not in safe case, we will escape:
			 */
			if (escapeManager != null) {

				if (escapeManager.arrive_or_not() == true) {

					escapeManager = null;

				} else {

					moveDir = escapeManager.direcition_updt();
					// System.out.println("kacisa devam");
				}
			}

			/*
			 * if we are in safe case and if we dont have bonus enough for
			 * attacking the enemie we collect the bonuses: For us,to be enough
			 * armored to attack an enemie is to have 3 bombs.
			 */
			else if (safeMap.returnMatrix()[ourBomberman.getLine()][ourBomberman
					.getCol()] != safeMap.SAFE_CASE
					&& safeMap.returnMatrix()[ourBomberman.getLine()][ourBomberman
							.getCol()] != safeMap.ENEMIE) {

				escapeManager = new EscapeManager(this);
				moveDir = escapeManager.direcition_updt();

			}

			else if (safeMap.returnMatrix()[ourY][ourX] == safeMap.SAFE_CASE
					&& (ourBomberman.getBombNumber() < 3)/*
														 * && k>0 &&
														 * our_bomberman
														 * .getBombCount()<2
														 */) {
				// System.out.println("bonus");

				boolean r = false;
				for (int i = 0; i < IA_ZONE.getHeight(); i++) {
					checkInterruption();
					for (int j = 0; j < IA_ZONE.getWidth(); j++) {
						checkInterruption();
						if (safeMap.returnMatrix()[i][j] == safeMap.BONUS)
							r = true;
					}
				}

				/*
				 * If we can access bonus directly,we will go to collect it
				 */
				if (r == true && qTest == 0) {

					if (bonusManager != null) {
						if (bonusManager.accessiblePath() == true) {
							if (bonusManager.hasArrivedB()) {

								bonusManager = null;

							} else {

								moveDir = bonusManager.directionUpdtB();

							}
						} else {
							qTest = 1;
							bonusManager = null;

						}
					} else {
						// q_test=1;
						bonusManager = new BonusManager(this);
						moveDir = bonusManager.directionUpdtB();
						// System.out.println("c");
					}

				}

				/*
				 * The bonus exists but it is in range of fire or the path which
				 * we will cross for it is in range of bombs. It is dangerous to
				 * try to access for it, so we break walls to find new bonus
				 * which are not dangerous.
				 */
				if (r == true && qTest == 1) {
					bonusManager = null;
					if (wallManager != null) {

						if (wallManager.hasArrivedB()) {

							if (wallManager.canesc() == true) {

								q = 1;

								wallManager = null;
							} else {
								q = 0;
								wallManager = null;
							}

						}

						else {
							// System.out.println("b");
							moveDir = wallManager.direcitionUpdtB();

						}

					} else {
						q = 0;
						wallManager = new WallManager(this);
						moveDir = wallManager.direcitionUpdtB();

					}
				}

				/*
				 * If no bonus exists in the zone(bonuses which are visible) we
				 * break walls to find bonus
				 */

				else if (r == false && ourBomberman.getBombCount() < 3) {

					if (wallManager != null) {

						if (wallManager.hasArrivedB()) {

							if (wallManager.canesc() == true) {

								q = 1;
								wallManager = null;
							} else {
								q = 0;

								wallManager = null;
							}

						}

						else {

							moveDir = wallManager.direcitionUpdtB();

						}

					} else {
						q = 0;
						wallManager = new WallManager(this);
						moveDir = wallManager.direcitionUpdtB();

					}
				}

			}

			/*
			 * Here, we attack or explore walls to access enemie.
			 */

			else if (IA_ZONE.getOwnHero().getBombCount() < 3) {
				if (enemyManager != null) {

					if (enemyManager.accessiblePath() == false && kkk != 1) {

						// This Method is for breaking walls when the enemie is
						// not accessible
						breakWallsForEnemy();
					}
					// We can access the enemie so we attack:
					else {
						kkk = 1;

						if (enemyManager.hasArrivedB()) {

							if (enemyManager.canesc() == true) {

								q = 1;
								enemyManager = null;

							} else {
								q = 0;
								enemyManager = null;

							}
						} else {

							moveDir = enemyManager.direcitionUpdtB();
							q = 0;
						}

					}

				}

				else {
					enemyManager = new EnemyManager(this);
					moveDir = enemyManager.direcitionUpdtB();
					q = 0;

				}
			}

			if (q == 0)
				result = new AiAction(AiActionName.MOVE, moveDir);
			else if (q == 1)
				result = new AiAction(AiActionName.DROP_BOMB);
		}
		return result;
	}

	/** Method for breaking walls when the enemie is not accessible.
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void breakWallsForEnemy() throws StopRequestException {

		checkInterruption();

		if (wallManager2 != null && IA_ZONE.getOwnHero().getBombCount() < 2) {

			if (wallManager2.hasArrivedB()) {

				if (wallManager2.canesc() == true) {
					q = 1;
					enemyManager = null;
					wallManager2 = null;
				} else {
					q = 0;
					enemyManager = null;
					wallManager2 = null;
				}

			}

			else {
				moveDir = wallManager2.direcitionUpdtB();

				q = 0;
			}

		} else {
			q = 0;
			wallManager2 = new WallManager2(this);
			moveDir = wallManager2.direcitionUpdtB();

		}
	}

}
