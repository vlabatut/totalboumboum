package org.totalboumboum.ai.v200910.ais.mancuhanpinarer.v1_1;

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
 * Cette classe implemente l'AI du groupe rouge. Actuellement, la strategie
 * de defense est implemente. Malheureusement, notre hero rouge ne se deplace
 * pas sur la zone quand la methode processAction() est sollicite par le moteur
 * du jeu. Apres l'operation debug, nous avons remarque que notre methode 
 * calculateShortestPath renvoie un objet AiPath qui se compose d'un seul AiTile.
 * De plus, cette AiTile possede les memes coordonnes que la position initiale de l'hero.
 * A chaque appel par le moteur, nous avons la meme probleme. Alors, le hero ne se
 * deplace pas.
 * 
 * Pourriez-vous nous aider pour notre probleme? 
 *   
 * @author Koray Mançuhan
 * @author Özgün Pınarer
 *
 */
public class MancuhanPinarer extends ArtificialIntelligence {
	private final int CASE_SUR = 0;
	private final int CASE_INACCESSIBLE = 1;
	private final int CASE_SCOPE = 2;
	private AiPath nextMove;
	private AiHero ourHero;

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException {
		// avant tout: test d'interruption
		checkInterruption();
		//La perception instantanement de l'environnement
		AiZone gameZone = getPercepts();
		//Le resultat du traitement indiquant l'action suivante
		AiAction result;
		//Notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		//On implemente la strategie de defense
		this.defenseAlgorithm(gameZone);
		//On assigne la nouvelle action
		result = this.newAction();
		return result;
	}

	/**
	 * Methode initialisant notre matrice de defense avant la remplissage. 
	 * Chaque case est initialement considere comme CASE_SUR
	 * 
	 * @param matriceDefense La Matrice de Defense
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private void initialiseMatriceDefense(int[][] matriceDefense, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		int height=gameZone.getHeight();
		int width=gameZone.getWidth();
		for(int i=0; i<height; i++){
			checkInterruption();
			for(int j=0; j<width; j++){
				checkInterruption();
				matriceDefense[i][j] = CASE_SUR;
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de defense possedant une bombe et
	 * les cases qui sont dans la portee de ces bombes. Ces nouveaux cases sont represente 
	 * par CASE_INACCESSIBLE et CASE_SCOPE respectivement.
	 * 
	 * @param matriceDefense La Matrice de Defense
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBombsMatriceDefense(int[][] matriceDefense, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matriceDefense[bomb.getLine()][bomb.getCol()] = CASE_INACCESSIBLE;
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
			while (iteratorScope.hasNext()) {
				checkInterruption();
				matriceDefense[iteratorScope.next().getLine()][iteratorScope
						.next().getCol()] = CASE_SCOPE;
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de defense possedant une mur. 
	 * Les cases des mures sont represente par CASE_INACCESSIBLE
	 * 
	 * @param matriceDefense La Matrice de Defense
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBlocksMatriceDefense(int[][] matriceDefense, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			matriceDefense[block.getLine()][block.getCol()] = CASE_INACCESSIBLE;
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de defense possedant une flamme. 
	 * Les cases des flammes sont represente par CASE_INACCESSIBLE
	 * 
	 * @param matriceDefense La Matrice de Defense
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillFiresMatriceDefense(int[][] matriceDefense, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		Collection<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matriceDefense[fire.getLine()][fire.getCol()] = CASE_INACCESSIBLE;
		}
	}
	
	/**
	 * Methode calculant la liste des cases ou le hero peut aller. On prend aussi en compte les cases qui sont
	 * dans la portee des bombes. Notre hero peut se deplacer en traversant ces cases.
	 * 
	 * @param matriceDefense La Matrice de Defense
	 * @param gameZone la zone du jeu
	 * @return la liste des points finaux.
	 * @throws StopRequestException
	 */
	private List<AiTile> calculateEndPoints(int[][] matriceDefense, AiZone gameZone)throws StopRequestException{
		checkInterruption();
		//la longueur de la matrice
		int width = gameZone.getWidth();
		//la largeur de la matrice
		int height = gameZone.getHeight();
		//la liste ou les points finaux sont tenus
		List<AiTile> endPoints = new ArrayList<AiTile>();
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				if (matriceDefense[i][j] == CASE_SUR || matriceDefense[i][j] == CASE_SCOPE) {
					endPoints.add(gameZone.getTile(i, j));
				}
			}
		}
		return endPoints;
	}
	
	/**
	 * Methode implementant l'algorithme de defense.
	 * 
	 * @param gameZone la zone du jeu
	 * @throws StopRequestException
	 */
	private void defenseAlgorithm(AiZone gameZone)throws StopRequestException {
		checkInterruption();
		int width = gameZone.getWidth();
		int height = gameZone.getHeight();
		int[][] matriceDefense = new int[height][width];
		
		//initialisation de matrice de defense
		this.initialiseMatriceDefense(matriceDefense, gameZone);
		//remplissage des matrice par les 
		this.fillBombsMatriceDefense(matriceDefense, gameZone);
		this.fillBlocksMatriceDefense(matriceDefense, gameZone);
		this.fillFiresMatriceDefense(matriceDefense, gameZone);
		
		//La position actuelle de notre hero
		AiTile startPoint = ourHero.getTile();
		//Les positions finales possibles de notre hero calcule par la methode calculateEndPoints
		List<AiTile> endPoints = this.calculateEndPoints(matriceDefense, gameZone);
		//Le chemin le plus court est calcule utilisant la methode calculateShortestPath
		this.nextMove = this.calculateShortestPath(ourHero,startPoint,endPoints);
	}

	/**
	 * 
	 * Methode calculant le chemin le plus court que l'hero peut suivre.
	 * 
	 * @param ownHero l'hero sollicite par notre AI
	 * @param startPoint la position de notre hero
	 * @param endPoints les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	private AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		checkInterruption();
		//le chemin le plus court possible
		AiPath shortestPath;
		//L'objet pour implementer l'algo A*
		Astar astar;
		//Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		//Calcul de l'heuristic par la classe de l'API
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
	private AiAction newAction() throws StopRequestException {
		checkInterruption();
		List<AiTile> tiles = this.nextMove.getTiles();
		//deplacement sur l'abcisse
		double dx;
		//deplacement sur l'ordonne
		double dy;
		
		//calcul de deplacement sur l'abcisse par rapport a la position de l'hero et la premiere 
		//case du chemin le plus court. 
		dx = (this.ourHero.getPosX()) - (tiles.get(0).getPosX());
		//calcul de deplacement sur l'ordonne par rapport a la position de l'hero et la premiere 
		//case du chemin le plus court. 
		dy = (this.ourHero.getPosY()) - (tiles.get(0).getPosY());
		
		//Determine la direction ou le hero va se deplacer.
		if (dx < 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} else if (dx < 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} else if (dx == 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UP);
		} else if (dx > 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} else if (dx > 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} else if (dx == 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} else if (dx > 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		} else if (dx < 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		} else {
			return new AiAction(AiActionName.NONE);
		}

	}
}
