package org.totalboumboum.ai.v201011.ais.avcigungor.v5;

import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * @author Ibrahim Avcı
 * @author Burak Güngör
 */
@SuppressWarnings("unused")
public class AvciGungor extends ArtificialIntelligence {

	// notre hero sur la zone
	// private AiOutput aio;
	double matrice[][];
	double matriceUpdate[][];
	private AiZone gameZone;
	private AiHero ourHero;
	public CollecteClass collectC;
	public AttackClass attackC;
	public FillingClass fc;
	AiOutput aio;
	private AiHero targetHero;
	private Astar astar;
	private AiPath path;
	private AiTile tileDest;
	private List<AiTile> possibleDest;
	private Direction moveDir;
	private WallController putBombController;
	private HeuristicCalculator heuristicCalculator;
	private MatrixCostCalculator costCalculator;
	private boolean arrived;

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException {
		checkInterruption();

		aio = getOutput();
		gameZone = getPercepts();
		ourHero = gameZone.getOwnHero();
		matriceUpdate = new double[gameZone.getHeight()][gameZone.getWidth()];
		AiAction result = new AiAction(AiActionName.NONE);
		
		this.updateCollectMatrix();
		double costMatrix[][] = new double[gameZone.getHeight()][gameZone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		this.updateCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		this.montrerLecran(matriceUpdate, aio);
		if (!collectC.findWallTiles(ourHero.getTile()).isEmpty()) { //si on est en train de trouver les murs, on continue
			chooseTarget();
			putBombController = new WallController(this);
			moveDir = putBombController.update();
			result = new AiAction(AiActionName.MOVE, moveDir);
		}
		
	/*	double costMatrix[][] = new double[gameZone.getHeight()][gameZone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		this.updateCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(this,ourHero,costCalculator,heuristicCalculator);
		arrived = false;
		
		possibleDest = collectC.findItemsTiles(ourHero.getTile());
		try {
			this.updatePath();
		} catch (LimitReachedException e) {
			// 
			//e.printStackTrace();
		}*/
	

		
		return result;

	}
	
	private void updatePath() throws StopRequestException, LimitReachedException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		path = astar.processShortestPath(getActualTile(),possibleDest);
		tileDest = path.getLastTile();
	}
	public boolean hasArrived() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!arrived)
		{	if(tileDest==null)
				arrived = true;
			else
			{	AiTile currentTile = getActualTile();
				arrived = currentTile==tileDest;			
			}
		}		
		return arrived;
	}
	public AiTile getActualTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return ourHero.getTile();
	}
	// on met a jour la matrice
	private void updateCollectMatrix() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		gameZone = getPercepts();

		collectC = new CollecteClass(this);
		matriceUpdate = collectC.getMatrice();

		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				System.out.print("[" + (int)matriceUpdate[i][j] + "]");
			}
			System.out.print("\n");
		}
		System.out.print("-----\n");

	}
	private void updateAttackMatrix() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		gameZone = getPercepts();

		attackC = new AttackClass(this);
		matriceUpdate = attackC.getMatrice();

		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				System.out.print("[" + (int)matriceUpdate[i][j] + "]");
			}
			System.out.print("\n");
		}
		System.out.print("-----\n");

	}
	void updateCostCalculator() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		for(int i=0;i<gameZone.getHeight();i++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int j=0;j<gameZone.getWidth();j++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				double cost = getCaseLevel(i, j);
				costCalculator.setCost(i,j,cost);
			}
		}
	}
	public double getCaseLevel(int i, int j) throws StopRequestException {
		checkInterruption();
		return (+1)*matriceUpdate[i][j];
	}
	private void chooseTarget() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		targetHero = null;
		List<AiHero> heros = new ArrayList<AiHero>(gameZone
				.getRemainingHeroes());
		heros.remove(ourHero);

		if (!heros.isEmpty()) {
			targetHero = heros.get(0);
			//findIsHeroAccessible(targetHero);
		}
		

	}

	private void montrerLecran(double[][] matriceUpdate, AiOutput aio)
			throws StopRequestException {
		checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				checkInterruption();
				if (matriceUpdate[i][j] == 1) {
					checkInterruption();
					aio.setTileColor(i, j, Color.lightGray);
					aio.setTileText(i, j, "IND");
				}
				if (matriceUpdate[i][j] == 2) {
					checkInterruption();
					aio.setTileColor(i, j, Color.YELLOW);
					aio.setTileText(i, j, "DES");
				}
				if (matriceUpdate[i][j] == 3) {
					checkInterruption();
					aio.setTileColor(i, j, Color.DARK_GRAY);
					aio.setTileText(i, j, "BOMBE");
				}
				if (matriceUpdate[i][j] == 4) {
					checkInterruption();
					aio.setTileColor(i, j, Color.BLUE);
					aio.setTileText(i, j, "FLM");
				}
				if (matriceUpdate[i][j] == 5) {
					checkInterruption();
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "BEB");
				}
				if (matriceUpdate[i][j] == 6) {
					checkInterruption();
					aio.setTileColor(i, j, Color.MAGENTA);
					aio.setTileText(i, j, "BEF");
				}
				if (matriceUpdate[i][j] == 7) {
					checkInterruption();
					aio.setTileColor(i, j, Color.ORANGE);
					aio.setTileText(i, j, "CS");
				}
				if (matriceUpdate[i][j] == 8) {
					checkInterruption();
					aio.setTileColor(i, j, Color.BLACK);
					aio.setTileText(i, j, "ADV");
				}
				if (matriceUpdate[i][j] == 9) {
					checkInterruption();
					aio.setTileColor(i, j, Color.WHITE);
					aio.setTileText(i, j, "NOUS");
				}
				if (matriceUpdate[i][j] == 10) {
					checkInterruption();
					aio.setTileColor(i, j, Color.RED);
					aio.setTileText(i, j, "FEU");
				}
			}
		}

	}

}
