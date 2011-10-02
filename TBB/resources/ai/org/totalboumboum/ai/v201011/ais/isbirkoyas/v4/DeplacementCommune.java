package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class DeplacementCommune {

	IsbirKoyas ai = new IsbirKoyas();
	public boolean print = false;
	private Securite securite = null;

	public DeplacementCommune(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

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
	public AiPath cheminLePlusCourt(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {
		ai.checkInterruption();

		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(ai, ai.ourHero, cost, heuristic);
		if (print)
			System.out.println("shortestpath");
		try {
			if (print)
				System.out.println(" on calcule le Shortestpath ");
			shortestPath = astar.processShortestPath(startPoint, endPoints);
			if (print)
				System.out.println("on a calcule le Shortestpath ");
		} catch (LimitReachedException e) { // 
			e.printStackTrace();
		}
		return shortestPath;
	}

	/**
	 * Methode qui detruit les murs
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param AiAction
	 *            resultat
	 * @return endPoints
	 * @throws StopRequestException
	 */
	public List<AiTile> detruire(AiZone gameZone, AiAction resultat)
			throws StopRequestException {
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		List<AiTile> endPoints = new ArrayList<AiTile>();
		Securite securite = new Securite(ai);
		while (iteratorBlocks.hasNext()) {
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible()) {
				ai.checkInterruption();
				try {

					endPoints.add(block.getTile().getNeighbor(Direction.UP));
				} catch (Exception e) {
				}
				try {
					endPoints.add(block.getTile().getNeighbor(Direction.DOWN));
				} catch (Exception e) {
				}
				try {
					endPoints.add(block.getTile().getNeighbor(Direction.LEFT));
				} catch (Exception e) {
				}
				try {
					endPoints.add(block.getTile().getNeighbor(Direction.RIGHT));
				} catch (Exception e) {
				}
			}
		}

		List<AiTile> dangerPoints = securite.casDanger(gameZone);
		for (int i = 0; i < dangerPoints.size(); i++)
			if (endPoints.contains(dangerPoints.get(i)))
				endPoints.remove(dangerPoints.get(i));

		// pour prendre les murs qui ont des bombes autour
		List<AiTile> mursInactifs = new ArrayList<AiTile>();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombes.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiBomb bombe = iteratorBombs.next();

			mursInactifs = bombe.getTile().getNeighbors();
			for (int i = 0; i < mursInactifs.size(); i++)
				if (endPoints.contains(mursInactifs.get(i)))
					endPoints.remove(mursInactifs.get(i));
		}

		if (print)
			System.out.println("DETRUIRE: EndPoints sans DANGER:" + endPoints);

		return endPoints;
	}

	/**
	 * Methode qui fait l'action de s'enfuire en cas de danger
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param AiAction
	 *            resultat
	 * @return endPoints
	 * @throws StopRequestException
	 */
	public List<AiTile> senfuire(AiZone gameZone, int m)
			throws StopRequestException {
		ai.checkInterruption();
		Securite securite = new Securite(ai);
		List<AiTile> endPoints = new ArrayList<AiTile>();
		AiTile ourTile = ai.ourHero.getTile();
		int y = ai.ourHero.getCol();
		int x = ai.ourHero.getLine();
		endPoints.remove(ourTile);
		// le coin gauche haut du jeu
		if (ai.ourHero.getCol() <= gameZone.getWidth() / 2
				&& ai.ourHero.getLine() <= gameZone.getHeight() / 2) {
			if (print)
				System.out.println("le coin gauche haut du jeu");
			for (int i = 0; i < gameZone.getHeight() / 2; i++) {
				ai.checkInterruption();
				for (int j = 0; j < gameZone.getWidth() / 2; j++) {
					ai.checkInterruption();
					if (gameZone.getTile(i, j).getBlocks().isEmpty()) {
						if (x != i && y != j) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.add(temp);
						} else if ((x != i && y == j) || (x == i && y != j))
							if (gameZone.getTileDistance(
									gameZone.getTile(i, j),
									ai.ourHero.getTile()) > m) {
								AiTile temp = gameZone.getTile(i, j);
								endPoints.add(temp);
							}

					}
				}
			}
		}
		//  le coin gauche bas du jeu
		else if (ai.ourHero.getCol() <= gameZone.getWidth() / 2
				&& ai.ourHero.getLine() >= gameZone.getHeight() / 2) {
			if (print)
				System.out.println(" le coin gauche bas du jeu");

			for (int i = gameZone.getHeight() / 2; i < gameZone.getHeight(); i++) {
				ai.checkInterruption();
				for (int j = 0; j < gameZone.getWidth() / 2; j++) {
					ai.checkInterruption();
					if (gameZone.getTile(i, j).getBlocks().isEmpty()) {
						if (x != i && y != j) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.add(temp);
						} else if ((x != i && y == j) || (x == i && y != j))
							if (gameZone.getTileDistance(
									gameZone.getTile(i, j),
									ai.ourHero.getTile()) > m) {
								AiTile temp = gameZone.getTile(i, j);
								endPoints.add(temp);
							}
					}
				}
			}
		}

		//  le coin gauche haut du jeu
		else if (ai.ourHero.getCol() >= gameZone.getWidth() / 2
				&& ai.ourHero.getLine() <= gameZone.getHeight() / 2) {

			if (print)
				System.out.println("le coin gauche haut du jeu");
			for (int i = 0; i < gameZone.getHeight() / 2; i++) {
				ai.checkInterruption();
				for (int j = gameZone.getWidth() / 2; j < gameZone.getWidth(); j++) {
					ai.checkInterruption();
					if (gameZone.getTile(i, j).getBlocks().isEmpty()) {
						if (x != i && y != j) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.add(temp);
						} else if ((x != i && y == j) || (x == i && y != j))
							if (gameZone.getTileDistance(
									gameZone.getTile(i, j),
									ai.ourHero.getTile()) > m) {
								AiTile temp = gameZone.getTile(i, j);
								endPoints.add(temp);
							}
					}
				}
			}
		}
		// le coin droite bas du jeu
		else if (ai.ourHero.getCol() >= gameZone.getWidth() / 2
				&& ai.ourHero.getLine() >= gameZone.getHeight() / 2) {
			if (print)
				System.out.println("le coin droite bas du jeu");
			for (int i = gameZone.getHeight() / 2; i < gameZone.getHeight(); i++) {
				ai.checkInterruption();
				for (int j = gameZone.getWidth() / 2; j < gameZone.getWidth(); j++) {
					ai.checkInterruption();
					if (gameZone.getTile(i, j).getBlocks().isEmpty()) {
						if (x != i && y != j) {
							AiTile temp = gameZone.getTile(i, j);
							endPoints.add(temp);
						} else if ((x != i && y == j) || (x == i && y != j))
							if (gameZone.getTileDistance(
									gameZone.getTile(i, j),
									ai.ourHero.getTile()) > m) {
								AiTile temp = gameZone.getTile(i, j);
								endPoints.add(temp);
							}
					}
				}
			}
		}

		System.out.println("Senfuire: endPoints" + endPoints);
		List<AiTile> DangerPoints = securite.casDanger(gameZone);
		if (print)
			System.out.println("Senfuire: dangerPoints" + DangerPoints);
		for (int k = 0; k < DangerPoints.size(); k++)
			if (endPoints.contains(DangerPoints.get(k)))
				endPoints.remove(DangerPoints.get(k));
		if (print)
			System.out.println("Senfuire: EndPoints sans DANGER:" + endPoints);

		return endPoints;
	}

	/**
	 * Methode calculant la nouvelle action
	 * @param  nextMove
	 * 
	 * @return la nouvelle action de notre hero
	 * 
	 * @throws StopRequestException
	 */
	public AiAction newAction(AiPath nextMove) throws StopRequestException {
		ai.checkInterruption();

		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;

		dx = (tiles.get(0).getLine()) - (this.ai.ourHero.getLine());
		// calcul de deplacement sur l'ordonne par rapport a la position de
		// l'hero et la premiere
		// case du chemin le plus court.
		dy = (tiles.get(0).getCol()) - (this.ai.ourHero.getCol());

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
	// LES METHODES D'ACCES 
	public Securite Securite() throws StopRequestException {
		ai.checkInterruption();
		return securite;

	}
}
